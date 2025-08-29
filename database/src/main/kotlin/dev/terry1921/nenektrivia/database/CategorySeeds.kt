package dev.terry1921.nenektrivia.database

import dev.terry1921.nenektrivia.database.entity.Category

object CategorySeeds {
    val default =
        listOf(
            Category(name = "Arte"),
            Category(name = "Deportes"),
            Category(name = "General"),
            Category(name = "Geografía"),
            Category(name = "Historia")
        )
}
