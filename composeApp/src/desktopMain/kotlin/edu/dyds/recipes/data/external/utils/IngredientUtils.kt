package edu.dyds.recipes.data.external.utils

fun ingredientNameFrom(ingredient: String): String? {
    val withoutNotes = ingredient.replace(PARENTHESIZED_NOTES_REGEX, " ")
    val words = withoutNotes
        .split(WHITESPACE_REGEX)
        .map { it.trim(',', '.', ';', ':') }
        .filter { it.isNotBlank() }
        .dropWhile { it.isQuantityOrUnit() }

    return words.joinToString(" ")
        .ifBlank { null }
}

fun ingredientWeightMultiplier(ingredient: String): Double {
    val normalized = ingredient.lowercase().replace(",", ".")
    val match = WEIGHT_REGEX.find(normalized) ?: return DEFAULT_WEIGHT_MULTIPLIER
    val amount = match.groupValues[1].toQuantity() ?: return DEFAULT_WEIGHT_MULTIPLIER
    val grams = when (match.groupValues[2]) {
        "kg", "l" -> amount * GRAMS_PER_KILOGRAM
        else -> amount
    }

    return grams / GRAMS_PER_CALORIE_UNIT
}

private fun String.toQuantity(): Double? {
    if (!contains("/")) return toDoubleOrNull()

    val parts = split("/")
    val numerator = parts.getOrNull(0)?.toDoubleOrNull() ?: return null
    val denominator = parts.getOrNull(1)?.toDoubleOrNull()?.takeIf { it != 0.0 } ?: return null

    return numerator / denominator
}

private fun String.isQuantityOrUnit(): Boolean {
    return matches(QUANTITY_REGEX)
            || matches(QUANTITY_WITH_UNIT_REGEX)
            || lowercase() in MEASUREMENT_UNITS
}

private const val DEFAULT_WEIGHT_MULTIPLIER = 1.0
private const val GRAMS_PER_KILOGRAM = 1000
private const val GRAMS_PER_CALORIE_UNIT = 100.0

private val PARENTHESIZED_NOTES_REGEX = Regex("""\([^)]*\)""")
private val WHITESPACE_REGEX = Regex("""\s+""")
private val WEIGHT_REGEX = Regex("""(\d+(?:\.\d+)?|\d+/\d+)\s*(kg|g|ml|l)\b""")
private val QUANTITY_REGEX = Regex("""\d+([.,/]\d+)?""")
private val QUANTITY_WITH_UNIT_REGEX = Regex("""\d+([.,]\d+)?(g|kg|ml|l)""")

private val MEASUREMENT_UNITS = setOf(
    "g", "gram", "grams", "kg", "ml", "l",
    "cup", "cups", "tbsp", "tablespoon", "tablespoons",
    "tsp", "teaspoon", "teaspoons", "oz", "ounce", "ounces",
    "lb", "lbs", "pound", "pounds", "pinch", "dash",
    "slice", "slices", "clove", "cloves", "can", "cans",
    "package", "packages", "pack", "packs"
)
