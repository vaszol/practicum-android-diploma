package ru.practicum.android.diploma.data.db.converter

object KeySkillsTypeConverter {
    private const val DELIMITER = ";"

    fun fromKeySkills(keySkills: List<String>?): String =
        keySkills?.joinToString(DELIMITER) ?: ""

    fun toKeySkills(keySkillsString: String?): List<String> =
        keySkillsString?.split(DELIMITER)?.filter { it.isNotEmpty() } ?: emptyList()
}
