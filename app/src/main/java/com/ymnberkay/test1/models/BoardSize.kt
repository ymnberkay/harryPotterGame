package com.ymnberkay.test1.models

enum class BoardSize(val numCards: Int) {
    EASY(4),
    MEDIUM(16),
    HARD(36);


    fun getWidth(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 4
            HARD -> 6
        }
    }

    fun getHeight(): Int {
        return numCards / getWidth()
    }

    fun getNumPairs(): Int {
        return numCards / 2
    }
}