package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ListAsteroidItemBinding

class AsteroidsDetailAdapter(val clickListener: AsteroidClickListener) : ListAdapter<Asteroid, AsteroidsDetailAdapter.ViewHolder>(DiffCallBack()) {

    inner class ViewHolder(val binding: ListAsteroidItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ListAsteroidItemBinding = DataBindingUtil.inflate(inflater, R.layout.list_asteroid_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemAsteroid = getItem(position)
        holder.bind(itemAsteroid)
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

}

class AsteroidClickListener (val itemClick: (item : Asteroid) -> Unit) {
    fun onClick(asteroidItem: Asteroid) = itemClick(asteroidItem)

}