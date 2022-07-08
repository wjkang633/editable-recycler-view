package com.example.editablerecyclerview

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.editablerecyclerview.MessageDifferAdapter.MessageViewHolder
import com.example.editablerecyclerview.databinding.ItemMessageAdapterBinding
import com.example.editablerecyclerview.model.Message

class MessageDifferAdapter : ListAdapter<Message, MessageViewHolder>(diffUtil) {

    private var isEditMode = false

    init {
        setHasStableIds(true)
    }

    var changedMessageList = mutableListOf<Message>()

    inner class MessageViewHolder(private val binding: ItemMessageAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.messageBodyEditText.setText(message.messageBody)

            if (isEditMode) {
                binding.messageDeleteButton.isVisible = true
                binding.messageBodyEditText.isEnabled = true
            } else {
                binding.messageDeleteButton.isVisible = false
                binding.messageBodyEditText.isEnabled = false
            }

            binding.messageBodyEditText.addTextChangedListener { text: Editable? ->
                if (message.timestamp == itemId) {
                    val newMessage = message.copy(messageBody = text.toString())
                    if (newMessage.messageBody != message.messageBody) {
                        changedMessageList.add(newMessage)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding =
            ItemMessageAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun replaceItems(dataList: MutableList<Message>) {
        submitList(dataList)
    }

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode

        if (isEditMode) {
            changedMessageList.clear()
        }

        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).timestamp
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }


            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.messageId == newItem.messageId
            }
        }
    }
}