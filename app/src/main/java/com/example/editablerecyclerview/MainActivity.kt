package com.example.editablerecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.editablerecyclerview.databinding.ActivityMainBinding
import com.example.editablerecyclerview.model.Message

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            val messages = mutableListOf<Message>()
            for (i in 1..20) {
                messages.add(Message(i.toLong(), "message $i", System.currentTimeMillis() + i))
            }

            val messageAdapter = MessageDifferAdapter()
            messageAdapter.submitList(messages)
            adapter = messageAdapter
        }

        binding.updateDataButton.setOnClickListener {
            val messages = mutableListOf<Message>()
            messages.add(Message(3, "message 33", System.currentTimeMillis() + 33))
            messages.add(Message(5, "message 55", System.currentTimeMillis() + 55))
            messages.add(Message(7, "message 77", System.currentTimeMillis() + 77))
            messages.add(Message(12, "message 1212", System.currentTimeMillis() + 1212))
            messages.add(Message(14, "message 1414", System.currentTimeMillis() + 1414))

            (binding.recyclerView.adapter as MessageDifferAdapter).replaceItems(messages)
        }

        binding.editButton.setOnClickListener {
            if (binding.editButton.isSelected.not()) {
                (binding.recyclerView.adapter as MessageDifferAdapter).setEditMode(true)
                binding.editButton.isSelected = true
            } else {
                (binding.recyclerView.adapter as MessageDifferAdapter).setEditMode(false)

                val changedMessageList =
                    (binding.recyclerView.adapter as MessageDifferAdapter).changedMessageList.groupBy { message ->
                        message.messageId
                    }.map {
                        it.value.last()
                    }

                binding.editButton.isSelected = false
            }
        }
    }
}