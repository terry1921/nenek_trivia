package dev.terry1921.nenektrivia.model.category

data class Category(val id: Int = 0, val name: String) {
    companion object {
        val Art = Category(0, "Art")
        val Sports = Category(1, "Sports")
        val General = Category(2, "General")
        val Geography = Category(3, "Geography")
        val History = Category(4, "History")

        val allCategories: List<Category> by lazy {
            listOf(
                Art,
                Sports,
                General,
                Geography,
                History
            )
        }
    }
}
