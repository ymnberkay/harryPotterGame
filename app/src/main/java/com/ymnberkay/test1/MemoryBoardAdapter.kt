package com.ymnberkay.test1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.ymnberkay.test1.models.BoardSize
import com.ymnberkay.test1.models.MemortCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemortCard>,
    private val cardClickListener: CardClickListener
) : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener {
        fun onCardCliked(position: Int)
    }
    //view holderi başlatılıyor.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cardWidth = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    // oncreateHolder dan dönen verileri bağlar
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
    //listenin eleman sayısı = kart sayısı
    override fun getItemCount() = boardSize.numCards

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int){
            val memoryCard: MemortCard = cards[position]
            imageButton.setImageResource(if (memoryCard.isOpen) memoryCard.identifier else R.drawable.download)
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
            val colorStateList = if(memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton,colorStateList)
            imageButton.setOnClickListener {
                Log.i(TAG,"Clicked on position $position")
                cardClickListener.onCardCliked(position)

            }

        }
    }
}
