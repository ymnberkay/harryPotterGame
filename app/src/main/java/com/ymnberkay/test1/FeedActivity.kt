package com.ymnberkay.test1

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ymnberkay.test1.databinding.ActivityFeedBinding
import com.ymnberkay.test1.models.BoardSize
import com.ymnberkay.test1.models.MemortCard
import com.ymnberkay.test1.models.MemoryGame


class FeedActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Feed Activity"
    }

    private lateinit var memoryGame: MemoryGame
    private lateinit var binding: ActivityFeedBinding
    private var boardSize: BoardSize = BoardSize.MEDIUM
    private lateinit var adapter: MemoryBoardAdapter
    private var ms: Long = 45000

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        countDownTimer = object : CountDownTimer(ms, 1000) {
            override fun onTick(p0: Long) {
                binding.textViewTimes.text = "Times : " + p0 / 1000
            }

            override fun onFinish() {
                binding.textViewTimes.text = "Time : 0"

                AlertDialog.Builder(this@FeedActivity)
                    .setTitle("Time is over.")
                    .setPositiveButton("OK") { dialog, which ->
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }

        }

        //boardSetup()
        getData(8)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> {
                // oyunu yeniden başlatılması için kullanılıyor
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlert("Quit game ?", null, View.OnClickListener {
                    })
                } else {
                    //boardSetup()
                }
                return true
            }
            R.id.menuDifficult -> {
                showNewDifficultDialog()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun showNewDifficultDialog() {
        val sizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val sizeOfRg = sizeView.findViewById<RadioGroup>(R.id.radioGroup)

        when (boardSize) {
            BoardSize.EASY -> sizeOfRg.check(R.id.radioButtonEasy)
            BoardSize.MEDIUM -> sizeOfRg.check(R.id.radioButtonMedium)
            BoardSize.HARD -> sizeOfRg.check(R.id.radioButtonHard)
        }
        showAlert("Choose new difficult", sizeView, View.OnClickListener {
            boardSize = when (sizeOfRg.checkedRadioButtonId) {

                R.id.radioButtonEasy -> BoardSize.EASY
                R.id.radioButtonMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD

            }
            getData(boardSize.getWidth())

        })
    }

    private fun showAlert(
        titleOfAlert: String,
        viewofAlert: View?,
        clickListenerOfPositive: View.OnClickListener
    ) {
        AlertDialog.Builder(this@FeedActivity)
            .setTitle(titleOfAlert)
            .setView(viewofAlert)
            .setNegativeButton("NO", null)
            .setPositiveButton("OK") { dialog, which ->
                clickListenerOfPositive.onClick(null)
            }.show()
    }

    private fun boardSetup(cards: List<MemortCard>) {
        binding.textViewPairs.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.color_progress_none
            )
        )
        memoryGame = MemoryGame(boardSize, cards.toMutableList())
        //binding.recyclerViewBoard.adapter =
        //recyclerview oluşturmak için gerekli

        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardCliked(position: Int) {
                    Log.i(TAG, "Card clicked $position")
                    updateGameWithFlip(position)
                }

            })
        //adapter recyclerview oluşturmak için gerekli
        binding.recyclerViewBoard.adapter = adapter
        //layout manager recyclerview oluşturmak için gerekli
        binding.recyclerViewBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }

    @SuppressLint("RestrictedApi")
    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            // oyun kazanıldı alert göster burada sayacı durdur ve puanları alert ekranına yazdır
            AlertDialog.Builder(this@FeedActivity)
                .setTitle("You Won.")
                .setPositiveButton("OK") { dialog, which ->
                    finish()
                }
                .setCancelable(false)
                .show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {

            return
        }

        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Fount a match! Num pairs found: ${memoryGame.numPairsFound}")

            binding.textViewPairs.text =
                "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                AlertDialog.Builder(this@FeedActivity)
                    .setTitle("You won.")
                    .setPositiveButton("OK") { dialog, which ->
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        binding.textViewPoints.text = "Points: ${memoryGame.getNumMoves()}"

        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.let {
            it.cancel()
        }
    }


    private fun getData(index: Int) {
        val docRef = Firebase.firestore.collection("Cards")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    countDownTimer?.start()
                    Log.d(
                        "MemoryGame.TAG",
                        "DocumentSnapshot data: ${document.documents.get(0).data?.get("Adi")}"
                    )
                    val boardItems = mutableListOf<MemortCard>()
                    val boardItemCpy = mutableListOf<MemortCard>()
                    val docs = document.documents.toMutableList()
                    docs.shuffle()
                    for (item in 1..index) {
                        boardItems.add(
                            MemortCard(
                                docs.get(item).data.hashCode(),
                                false,
                                false,
                                docs.get(item).data?.get("Adi").toString(),
                                docs.get(item).data?.get("Evi").toString(),
                                docs.get(item).data?.get("Kart Resmi").toString(),
                                20
                            )
                        )
                        boardItemCpy.add(
                            MemortCard(
                                docs.get(item).data.hashCode(),
                                false,
                                false,
                                docs.get(item).data?.get("Adi").toString(),
                                docs.get(item).data?.get("Evi").toString(),
                                docs.get(item).data?.get("Kart Resmi").toString(),
                                20
                            )
                        )
                    }
                    boardItems.shuffle()
                    boardItemCpy.shuffle()
                    boardSetup(boardItems + boardItemCpy)
                } else {
                }
            }.addOnFailureListener { exception ->
                countDownTimer?.cancel()
                finish()
            }
    }

}

/*val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this,R.color.color_progress_none),
                ContextCompat.getColor(this,R.color.color_progress_full)
            ) as Int
            binding.textViewPairs.setTextColor(color)*/