package com.nk.tokitelist.data.models

enum class Rating(val rat: Int) {
    SHIT(0),
    BAD(1),
    POOR(2),
    OK(3),
    GOOD(4),
    PERFECT(5);

    companion object {
        fun fromInt(value: Int) = Rating.values().first { it.rat == value }
    }

}