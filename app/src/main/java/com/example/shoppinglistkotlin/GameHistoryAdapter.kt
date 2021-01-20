package com.example.shoppinglistkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*

class GameHistoryAdapter(private val gameHistoryStatistics: List<GameHistoryStats>) : RecyclerView.Adapter<GameHistoryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = gameHistoryStatistics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(gameHistoryStatistics[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(gameHistoryStats: GameHistoryStats) {
            //itemView.ivHistoryComputerHand.setImageResource(gameHistoryStats.computerHand)
            //itemView.ivHistoryPlayerHand.setImageResource(gameHistoryStats.playerHand)
            itemView.tvHistoryWinLose.text = "Score: " + gameHistoryStats.computerHand.toString()
            itemView.tvHistoryDate.text = gameHistoryStats.date.toString()

            when (gameHistoryStats.stars) {
                1 -> itemView.ivStar1gold.setImageResource(R.drawable.star)
                2 -> {
                    itemView.ivStar1gold.setImageResource(R.drawable.star)
                    itemView.ivStar2gold.setImageResource(R.drawable.star)
                }
                3 -> {
                    itemView.ivStar1gold.setImageResource(R.drawable.star)
                    itemView.ivStar2gold.setImageResource(R.drawable.star)
                    itemView.ivStar3gold.setImageResource(R.drawable.star)
                }
                else -> { // Note the block
                    print("starCount is wrong")
                }
            }

           // itemView.tvProduct.text = gameHistoryStats.name
           // itemView.tvQuantity.text = gameHistoryStats.quantity.toString()
        }
    }
}