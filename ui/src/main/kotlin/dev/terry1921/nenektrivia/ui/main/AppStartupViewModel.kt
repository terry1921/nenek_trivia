package dev.terry1921.nenektrivia.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.terry1921.nenektrivia.domain.honor.WarmHonorCacheUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppStartupViewModel @Inject constructor(warmHonorCacheUseCase: WarmHonorCacheUseCase) :
    ViewModel() {
    init {
        viewModelScope.launch {
            warmHonorCacheUseCase()
        }
    }
}
