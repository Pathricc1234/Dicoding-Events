package com.dicoding.dicodingevent.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.database.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.databinding.ItemVerticalBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity

class FinishedAdapter : ListAdapter<FinishedEventEntity, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(val binding: ItemVerticalBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event: FinishedEventEntity){
            binding.tvEventName.text = event.name
            binding.tvEventDesc.text = event.summary
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.ivEventImage)

            itemView.setOnClickListener{
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra("event_id", event.id.toString())
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FinishedEventEntity> =
            object : DiffUtil.ItemCallback<FinishedEventEntity>() {
                override fun areItemsTheSame(oldItem: FinishedEventEntity, newItem: FinishedEventEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: FinishedEventEntity, newItem: FinishedEventEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}