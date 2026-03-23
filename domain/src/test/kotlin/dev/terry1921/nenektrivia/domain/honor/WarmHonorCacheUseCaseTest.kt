package dev.terry1921.nenektrivia.domain.honor

import dev.terry1921.nenektrivia.network.honor.HonorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WarmHonorCacheUseCaseTest {

    private val honorRepository: HonorRepository = mock()
    private val useCase = WarmHonorCacheUseCase(honorRepository)

    @Test
    fun invoke_whenForceRefreshSucceeds_doesNotUseFallback() = runTest {
        whenever(honorRepository.fetchHonor(forceRefresh = true)).thenReturn(emptyList())

        useCase()

        verify(honorRepository).fetchHonor(forceRefresh = true)
        verify(honorRepository, never()).fetchHonor(forceRefresh = false)
    }

    @Test
    fun invoke_whenForceRefreshFails_usesFallbackCacheFetch() = runTest {
        whenever(honorRepository.fetchHonor(forceRefresh = true)).thenThrow(
            IllegalStateException("network error")
        )
        whenever(honorRepository.fetchHonor(forceRefresh = false)).thenReturn(emptyList())

        useCase()

        verify(honorRepository).fetchHonor(forceRefresh = true)
        verify(honorRepository).fetchHonor(forceRefresh = false)
    }

    @Test
    fun invoke_whenForceRefreshAndFallbackFail_swallowsException() = runTest {
        whenever(honorRepository.fetchHonor(forceRefresh = true)).thenThrow(
            IllegalStateException("network error")
        )
        whenever(honorRepository.fetchHonor(forceRefresh = false)).thenThrow(
            IllegalStateException("cache error")
        )

        useCase()

        verify(honorRepository).fetchHonor(forceRefresh = true)
        verify(honorRepository).fetchHonor(forceRefresh = false)
    }
}
