package main.kotlin.day21

import main.kotlin.util.readInputForDay
import main.kotlin.util.readSmallInputForDay


fun main() {
    val lines = readInputForDay(21)
    val allFood = parseInput(lines)
    val ingredients = allFood.map { it.ingredients }.flatten().toSet()
    val allergenMap = createMapForAllergens(allFood)
    println(allergenMap)
    val noAllergens = getIngredientsWithoutAllergen(allergenMap, ingredients)
    val occurencesPart1 = countOccurences(noAllergens, allFood)
    println(occurencesPart1)

    val dangerousIngr = findDangerousIngredients(allergenMap)
    println(dangerousIngr)

}

fun findDangerousIngredients(allergenMap: Map<String, Set<String>>): String {
    val mutAllergenMap = allergenMap.toMutableMap()
    //allergens is key for ingredients
    val ingredientsAllergens = mutableMapOf<String, String>()
    while (true) {
        if (ingredientsAllergens.size == allergenMap.size) {
            break
        }
        for ((allergen, ingredients) in mutAllergenMap) {
            if (ingredients.size == 1) {
                ingredientsAllergens[allergen] = ingredients.first()
                for ((al, ing) in mutAllergenMap) {
                    val ingMut = ing.toMutableSet()
                    ingMut.remove(ingredients.first())
                    mutAllergenMap[al] = ingMut
                }
            }
        }
    }
    return ingredientsAllergens.toSortedMap().map { it.value }.joinToString(separator = ",") { it }

}

fun createMapForAllergens(allFood: List<Food>): Map<String, Set<String>> {
    val totalAllergenMap = mutableMapOf<String, Set<String>>()
    for (food in allFood) {
        val allergenMap = mutableMapOf<String, Set<String>>()
        for (allergen in food.allergens) {
            allergenMap[allergen] = food.ingredients
        }
        for ((k, v) in allergenMap) {
            if (!totalAllergenMap.containsKey(k)) {
                totalAllergenMap[k] = v
            } else {
                val totalSet = totalAllergenMap[k]!!
                totalAllergenMap[k] = v.intersect(totalSet)
            }
        }
    }
    return totalAllergenMap
}

fun getIngredientsWithoutAllergen(allergenMap: Map<String, Set<String>>, ingredients: Set<String>): Set<String> {
    val cleanIngredients = ingredients.toMutableSet()
    for ((_, v) in allergenMap) {
        cleanIngredients.removeAll(v)
    }
    return cleanIngredients
}

fun countOccurences(noAllergens: Set<String>, allFood: List<Food>): Int {
    var count = 0
    for (ingr in noAllergens) {
        count += allFood.map { it.ingredients }.count { it.contains(ingr) }
    }
    return count
}

fun parseInput(lines: List<String>): List<Food> {
    val foodList = mutableListOf<Food>()
    for (line in lines) {
        val split = line.split("(contains ")
        val ingredients = split[0].split(" ").filterNot { it == "" }
        val allergens = split[1].split(", ").toMutableList()
        allergens[allergens.lastIndex] = allergens.last().replace(")", "")
        foodList.add(Food(ingredients.toSet(), allergens.toSet()))
    }
    return foodList
}

data class Food(val ingredients: Set<String>, val allergens: Set<String>)