package dev.terry1921.nenektrivia.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RoomConvertersTest {

    private val converters = RoomConverters()

    @Test
    fun `fromStringList with valid list returns valid JSON string`() {
        val list = listOf("apple", "banana", "cherry")
        val expected = "[\"apple\",\"banana\",\"cherry\"]"
        val result = converters.fromStringList(list)
        assertEquals(expected, result)
    }

    @Test
    fun `fromStringList with null returns null`() {
        val result = converters.fromStringList(null)
        assertNull(result)
    }

    @Test
    fun `fromStringList with empty list returns empty JSON array`() {
        val list = emptyList<String>()
        val expected = "[]"
        val result = converters.fromStringList(list)
        assertEquals(expected, result)
    }

    @Test
    fun `toStringList with valid JSON string returns valid list`() {
        val json = "[\"apple\",\"banana\",\"cherry\"]"
        val expected = listOf("apple", "banana", "cherry")
        val result = converters.toStringList(json)
        assertEquals(expected, result)
    }

    @Test
    fun `toStringList with null returns null`() {
        val result = converters.toStringList(null)
        assertNull(result)
    }

    @Test
    fun `toStringList with empty JSON array returns empty list`() {
        val json = "[]"
        val expected = emptyList<String>()
        val result = converters.toStringList(json)
        assertEquals(expected, result)
    }
}
