package com.ymnberkay.test1.models

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ymnberkay.test1.utils.defaultIcons

class MemoryGame (private val boardSize: BoardSize){

    val cards: List<MemortCard>
    var numPairsFound = 0
    private var db = Firebase.firestore
    //puan algoritması yazılacak
    private var numCardFlips= 0
    private var indexOfSingleSelectedCard: Int? = null
    init {
        val chosenImages = defaultIcons.shuffled().take(boardSize.getNumPairs())
        val randomImages = (chosenImages + chosenImages).shuffled()
        cards = randomImages.map { MemortCard(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        var foundMatch = false
        if (indexOfSingleSelectedCard == null){
            // hiçbir kart yada 2 kart çevirilmiş
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else {
            // 1 tane kart çevirilmiş
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard = null
        }
        card.isOpen = !card.isOpen
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean{
        if ( cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched){
                card.isOpen = false
            }

        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isOpen
    }

    fun getNumMoves(): Int {
        //buraya puan algoritaması yazılacak
        return numCardFlips / 2
    }

}