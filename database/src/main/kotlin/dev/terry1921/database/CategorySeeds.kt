package dev.terry1921.database

import dev.terry1921.database.entity.Category

object CategorySeeds {
    val default =
        listOf(
            Category(name = "Arte"),
            Category(name = "Deportes"),
            Category(name = "General"),
            Category(name = "Geografía"),
            Category(name = "Historia"),
        )
}
