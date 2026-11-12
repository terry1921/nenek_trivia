package dev.terry1921.nenektrivia.domain.account

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.terry1921.nenektrivia.database.auth.AuthRepository
import dev.terry1921.nenektrivia.database.auth.LocalAccountDeletion
import dev.terry1921.nenektrivia.network.auth.AccountProvider
import dev.terry1921.nenektrivia.network.auth.FirebaseAccountDeletion
import dev.terry1921.nenektrivia.network.honor.HonorCache
import dev.terry1921.nenektrivia.network.honor.HonorSessionCache
import dev.terry1921.nenektrivia.network.leaderboard.LeaderboardCache
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class DeleteAccountUseCase @Inject constructor(
    @ApplicationContext context: Context,
    private val accounts: AuthRepository,
    private val local: LocalAccountDeletion,
    private val remote: FirebaseAccountDeletion,
    private val honorCache: HonorCache,
    private val honorSessionCache: HonorSessionCache,
    private val leaderboardCache: LeaderboardCache
) {
    private val pending = context.getSharedPreferences("account_deletion", Context.MODE_PRIVATE)
    private val mutex = Mutex()

    suspend fun request(): AccountDeletionRequest {
        val uid = checkNotNull(accounts.getActiveUser()?.id)
        return AccountDeletionRequest(uid, remote.providers(uid))
    }

    suspend operator fun invoke(uid: String, provider: AccountProvider, token: String) =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                check(accounts.getActiveUser()?.id == uid && remote.currentUid == uid)
                remote.delete(uid, provider, token) {
                    check(pending.edit().putString(PENDING_UID, uid).commit())
                }
                clearLocalData(uid)
            }
        }

    suspend fun finishInterruptedCleanup() = withContext(Dispatchers.IO) {
        mutex.withLock {
            val uid = pending.getString(PENDING_UID, null) ?: return@withLock false
            if (remote.currentUid == uid) return@withLock false
            clearLocalData(uid)
            true
        }
    }

    private suspend fun clearLocalData(uid: String) {
        honorSessionCache.clear()
        honorCache.clear()
        leaderboardCache.clear()
        local.delete(uid)
        check(pending.edit().remove(PENDING_UID).commit())
    }

    private companion object {
        const val PENDING_UID = "uid"
    }
}

data class AccountDeletionRequest(val uid: String, val providers: Set<AccountProvider>)
