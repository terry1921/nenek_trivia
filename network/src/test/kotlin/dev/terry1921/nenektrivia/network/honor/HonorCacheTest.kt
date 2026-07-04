package dev.terry1921.nenektrivia.network.honor

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import dev.terry1921.nenektrivia.model.honor.HonorModel
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HonorCacheTest {

    private lateinit var context: Context
    private lateinit var honorCache: HonorCache
    private lateinit var gson: Gson

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        gson = Gson()
        deleteDataStoreFiles()
        honorCache = HonorCache(context, gson)
    }

    @After
    fun tearDown() {
        deleteDataStoreFiles()
    }

    private fun deleteDataStoreFiles() {
        val datastoreDir = File(context.filesDir, "datastore")
        if (datastoreDir.exists()) {
            datastoreDir.deleteRecursively()
        }
    }

    @Test
    fun `readIfFresh returns null when no data exists`() = runTest {
        val result = honorCache.readIfFresh(ttlMillis = 1000)
        assertThat(result).isNull()
    }

    @Test
    fun `write saves data and readIfFresh retrieves it within TTL`() = runTest {
        val items = listOf(
            HonorModel(id = "1", username = "User1", points = 100),
            HonorModel(id = "2", username = "User2", points = 200)
        )

        honorCache.write(items)

        val result = honorCache.readIfFresh(ttlMillis = 10000)
        assertThat(result).isEqualTo(items)
    }

    @Test
    fun `readIfFresh returns null when data is older than TTL`() = runTest {
        val items = listOf(HonorModel(id = "1", username = "User1", points = 100))

        honorCache.write(items)

        val result = honorCache.readIfFresh(ttlMillis = -1)
        assertThat(result).isNull()
    }
}
