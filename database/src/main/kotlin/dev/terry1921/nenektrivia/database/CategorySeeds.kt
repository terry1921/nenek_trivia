package dev.terry1921.nenektrivia.database

import dev.terry1921.nenektrivia.database.entity.Category

object CategorySeeds {
    val default =
        listOf(
            Category(id = 0, name = "Arte"),
            Category(id = 1, name = "Deportes"),
            Category(id = 2, name = "General"),
            Category(id = 3, name = "Geografía"),
            Category(id = 4, name = "Historia")
        )
}
