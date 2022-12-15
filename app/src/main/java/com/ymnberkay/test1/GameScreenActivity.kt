package com.ymnberkay.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ymnberkay.test1.databinding.ActivityFeedBinding
import com.ymnberkay.test1.databinding.ActivityGameScreenBinding

class GameScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        binding = ActivityGameScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonOnePlayer.setOnClickListener {
            val intent = Intent(this,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonTwoPlayer.setOnClickListener {
            val intent = Intent(this,Feed2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}