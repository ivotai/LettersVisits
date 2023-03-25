package com.unicorn.lettersVisits.ui.act

import android.graphics.Color
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.net.utils.scopeNetLife
import com.drake.softinput.hasSoftInput
import com.drake.softinput.hideSoftInput
import com.drake.softinput.setWindowSoftInput
import com.drake.statusbar.immersive
import com.drake.statusbar.statusPadding
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.databinding.ActOpenAiBinding
import com.unicorn.lettersVisits.databinding.ItemChatAiBinding
import com.unicorn.lettersVisits.databinding.ItemChatUserBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import splitties.resources.color


class OpenAiAct : BaseAct<ActOpenAiBinding>() {
    override fun initStatusBar() {
        immersive(darkMode = true)
        binding.rv.statusPadding()
    }

    @OptIn(BetaOpenAI::class)
    override fun initViews() {
        binding.apply {
            //
            btnSend.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.main_color), Color.BLACK, 0.3f
            )
        }
        setWindowSoftInput(float = binding.RConstraintLayout, onChanged = {
            Log.d("SoftInput", "visibility = ${hasSoftInput()}")
        })

        binding.rv.linear().setup {

            addType<ChatMessage> {
                when (role) {
                    ChatRole.User -> {
                        R.layout.item_chat_user
                    }
                    ChatRole.Assistant, ChatRole.System -> {
                        R.layout.item_chat_ai
                    }
                    else -> {
                        throw IllegalStateException("Unknown Chat Role")
                    }
                }
            }

            onBind {
                val item = getModel<ChatMessage>()
                when (item.role) {
                    ChatRole.User -> {
                        val binding = getBinding<ItemChatUserBinding>()
                        binding.tv.text = item.content
                    }
                    ChatRole.Assistant, ChatRole.System -> {
                        val binding = getBinding<ItemChatAiBinding>()
                        binding.tv.text = item.content
                    }
                }

            }
        }


    }

    private val key = "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"
    private val modelId = "gpt-3.5-turbo"
    private val openAI = OpenAI(
        OpenAIConfig(
            token = key, host = OpenAIHost("https://openai.proxy.mailjob.net")
        )
    )

    @OptIn(BetaOpenAI::class)
    private val messages = mutableListOf<ChatMessage>()

    @OptIn(BetaOpenAI::class)
    override fun initIntents() {
        binding.apply {
            btnSend.setOnClickListener {
                val userContent = et.text.toString()
                if (userContent.isEmpty()) return@setOnClickListener

                et.setText("")
                hideSoftInput()

                val userMessage = ChatMessage(role = ChatRole.User, content = userContent)
                messages.add(userMessage)
                // todo 更好的追加数据的方式
                rv.bindingAdapter.addFooter(userMessage)
                binding.rv.smoothScrollToPosition(binding.rv.adapter!!.itemCount - 1)

                scopeNetLife {

                    try {
                        val completion = openAI.chatCompletion(
                            ChatCompletionRequest(
                                model = ModelId(modelId), messages = messages
                            )
                        )
                        val aiContent = completion.choices[0].message?.content ?: ""
                        val aiMessage = ChatMessage(role = ChatRole.Assistant, content = aiContent)
                        messages.add(aiMessage)
                        rv.bindingAdapter.addFooter(aiMessage)
                        rv.smoothScrollToPosition(rv.adapter!!.itemCount - 1)


                    } catch (e: Exception) {
                        // do nothing
                    }
                }
            }
        }


        val aiMessage =
            ChatMessage(role = ChatRole.System, content = "我是人工智能助手，请提问任何你想知道的，回复可能需要一段时间，可以连续提问。")
        messages.add(aiMessage)
        binding.rv.bindingAdapter.addFooter(aiMessage)
        binding.rv.smoothScrollToPosition(binding.rv.adapter!!.itemCount - 1)

    }


}