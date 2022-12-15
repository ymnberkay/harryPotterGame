package com.ymnberkay.test1

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.ymnberkay.test1.databinding.ActivityFeedBinding
import com.ymnberkay.test1.models.BoardSize
import com.ymnberkay.test1.models.MemortCard
import com.ymnberkay.test1.models.MemoryGame
import com.ymnberkay.test1.utils.defaultIcons


class FeedActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Feed Activity"
    }

    private lateinit var memoryGame: MemoryGame
    private lateinit var binding: ActivityFeedBinding
    private  var boardSize: BoardSize = BoardSize.EASY
    private lateinit var adapter: MemoryBoardAdapter
    private var ms: Long = 45000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        object : CountDownTimer(ms,1000) {
            override fun onTick(p0: Long) {
                binding.textViewTimes.text = "Times : " + p0/1000
            }

            override fun onFinish() {
                binding.textViewTimes.text = "Time : 0"

                var alert = AlertDialog.Builder(this@FeedActivity)
                alert.setTitle("Time is over.")
                alert.setMessage("Restart The Game ?")
                alert.setPositiveButton("yes") { dialog, which ->
                    //restart the game
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(this@FeedActivity,"Game is over",Toast.LENGTH_LONG).show()
                }
                alert.show()
            }

        }.start()
        boardSetup()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> {
                // oyunu yeniden başlatılması için kullanılıyor
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlert("Quit game ?",null,View.OnClickListener {
                        boardSetup()
                    })
                }
                else {
                    boardSetup()
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
        val sizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val sizeOfRg = sizeView.findViewById<RadioGroup>(R.id.radioGroup)

        when(boardSize) {
            BoardSize.EASY -> sizeOfRg.check(R.id.radioButtonEasy)
            BoardSize.MEDIUM -> sizeOfRg.check(R.id.radioButtonMedium)
            BoardSize.HARD -> sizeOfRg.check(R.id.radioButtonHard)
        }
        showAlert("Choose new difficult",sizeView,View.OnClickListener {
            boardSize= when (sizeOfRg.checkedRadioButtonId) {

                R.id.radioButtonEasy -> BoardSize.EASY
                R.id.radioButtonMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD

            }
            boardSetup()

        })
    }

    private fun showAlert(titleOfAlert: String, viewofAlert: View?, clickListenerOfPositive: View.OnClickListener) {
        AlertDialog.Builder(this@FeedActivity)
            .setTitle(titleOfAlert)
            .setView(viewofAlert)
            .setNegativeButton("NO",null)
            .setPositiveButton("OK"){ dialog, which ->
                clickListenerOfPositive.onClick(null)
            }.show()
    }

    private fun boardSetup(){
        binding.textViewPairs.setTextColor(ContextCompat.getColor(this,R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
        //binding.recyclerViewBoard.adapter =
        //recyclerview oluşturmak için gerekli
        adapter = MemoryBoardAdapter(this,boardSize,memoryGame.cards,object : MemoryBoardAdapter.CardClickListener{
            override fun onCardCliked(position: Int) {
                Log.i(TAG, "Card clicked $position")
                updateGameWithFlip(position)
            }

        })
        //adapter recyclerview oluşturmak için gerekli
        binding.recyclerViewBoard.adapter = adapter
        binding.recyclerViewBoard.setHasFixedSize(true)
        //layout manager recyclerview oluşturmak için gerekli
        binding.recyclerViewBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }

    @SuppressLint("RestrictedApi")
    private fun updateGameWithFlip(position: Int) {
        if(memoryGame.haveWonGame()){
            // oyun kazanıldı alert göster burada sayacı durdur ve puanları alert ekranına yazdır
            var alert = AlertDialog.Builder(this@FeedActivity)
            alert.setTitle("You already won")
            alert.setMessage("Restart The Game ?")
            alert.setPositiveButton("yes") { dialog, which ->
                //restart the game
                val intent = intent
                finish()
                startActivity(intent)
            }
            alert.setNegativeButton("No") { dialog, which ->
                Toast.makeText(this@FeedActivity,"İnfo",Toast.LENGTH_LONG).show()
            }
            alert.show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){

            return
        }

        if(memoryGame.flipCard(position)){
            Log.i(TAG, "Fount a match! Num pairs found: ${memoryGame.numPairsFound}")

            binding.textViewPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()){
                var alert = AlertDialog.Builder(this@FeedActivity)
                alert.setTitle("You already won")
                alert.setMessage("Restart The Game ?")
                alert.setPositiveButton("yes") { dialog, which ->
                    //restart the game
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(this@FeedActivity,"İnfo",Toast.LENGTH_LONG).show()
                }
                alert.show()
            }
        }
        binding.textViewPoints.text = "Points: ${memoryGame.getNumMoves()}"

        adapter.notifyDataSetChanged()
    }

}

/*val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this,R.color.color_progress_none),
                ContextCompat.getColor(this,R.color.color_progress_full)
            ) as Int
            binding.textViewPairs.setTextColor(color)*/