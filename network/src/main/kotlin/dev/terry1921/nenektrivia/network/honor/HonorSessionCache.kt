package dev.terry1921.nenektrivia.network.honor

import dev.terry1921.nenektrivia.model.honor.HonorModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HonorSessionCache @Inject constructor() {
    @Volatile
    private var items: List<HonorModel>? = null

    fun read(): List<HonorModel>? = items

    fun write(items: List<HonorModel>) {
        this.items = items
    }

    fun clear() {
        items = null
    }
}
