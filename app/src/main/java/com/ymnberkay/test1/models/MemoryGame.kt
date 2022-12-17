package com.ymnberkay.test1.models

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ymnberkay.test1.utils.defaultIcons

class MemoryGame(private val boardSize: BoardSize, private val cards: MutableList<MemortCard>) {

    companion object {
        private const val TAG = "Memory game kt"
    }


    var numPairsFound = 0
    private var db = Firebase.firestore
    //puan algoritması yazılacak

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        /*val docRef =  Firebase.firestore.collection("Cards").
        addSnapshotListener(object: EventListener<QuerySnapshot> {
             override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?){
                if (error != null){
                    Log.i(TAG,error.message.toString())
                    return
                }
                 for (dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        cardlar.add(dc.document.toObject(MemortCard::class.java))
                    }
                 }
            }
        })

        val chosenImages = defaultIcons.shuffled().take(boardSize .getNumPairs())
        val randomImages = (chosenImages + chosenImages).shuffled()
        cards = randomImages.map { MemortCard(it) }

         */

    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]

        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // hiçbir kart yada 2 kart çevirilmiş
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // 1 tane kart çevirilmiş
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isOpen = !card.isOpen
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
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
        return numPairsFound
    }



}


