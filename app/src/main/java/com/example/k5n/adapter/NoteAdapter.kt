package com.example.k5n.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.k5n.R
import com.example.k5n.databinding.NoteLayoutBinding
import com.example.k5n.model.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val itemBinding: NoteLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    // List warna border (drawable)
    private val borderDrawables = listOf(
        R.drawable.border_red,
        R.drawable.border_blue,
        R.drawable.border_green,
        R.drawable.border_yellow,
        R.drawable.border_purple,
        R.drawable.border_pink
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.apply {
            noteTitle.text = currentNote.noteTitle
            noteDesc.text = currentNote.noteDesc

            // Format waktu
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            noteTimestamp.text = sdf.format(Date(currentNote.timestamp))

            // Jalankan animasi berkedip
            val blink = android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context, R.anim.blink)
            noteTimestamp.startAnimation(blink)

            // Acak background border
            val drawableResId = borderDrawables[Random.nextInt(borderDrawables.size)]
            noteContainer.background = ContextCompat.getDrawable(holder.itemView.context, drawableResId)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentNote)
        }
    }


    private var onItemClickListener: ((Note) -> Unit)? = null

    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }
}
