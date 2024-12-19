package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemListBinding
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity

//class StoryAdapter(private var listStory: List<ListStoryItem>) :
//    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
//
//    inner class StoryViewHolder(private val binding: ItemListBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(story: ListStoryItem) {
//            binding.itemTitle.text = story.name
//            Glide.with(itemView.context)
//                .load(story.photoUrl)
//                .into(binding.itemImage)
//
//            itemView.setOnClickListener {
//                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
//                intentDetail.putExtra("ID", story.id.toString())
//                itemView.context.startActivity(intentDetail)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
//        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return StoryViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        holder.bind(listStory[position])
//    }
//
//    override fun getItemCount(): Int = listStory.size
//
//    fun updateData(newList: List<ListStoryItem>) {
//        val diffCallback = object : DiffUtil.Callback() {
//            override fun getOldListSize(): Int = listStory.size
//            override fun getNewListSize(): Int = newList.size
//
//            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                return listStory[oldItemPosition].id == newList[newItemPosition].id
//            }
//
//            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                return listStory[oldItemPosition] == newList[newItemPosition]
//            }
//        }
//
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        listStory = newList
//        diffResult.dispatchUpdatesTo(this)
//    }
//}

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.itemTitle.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.itemImage)

            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra("ID", story.id.toString())
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

