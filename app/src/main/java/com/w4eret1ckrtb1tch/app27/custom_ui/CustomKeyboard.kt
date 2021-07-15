package com.w4eret1ckrtb1tch.app27.custom_ui

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import com.w4eret1ckrtb1tch.app27.R

class CustomKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    override fun onCreateInputView(): View {

        val keyboardView =
            layoutInflater.inflate(R.layout.custom_keyboard, null) as KeyboardView
        val keyboard = Keyboard(this, R.xml.custom_pad)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)

        return keyboardView
    }

    override fun onPress(p0: Int) {
    }

    override fun onRelease(p0: Int) {
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        currentInputConnection?.commitText(primaryCode.toString(), 1)
    }

    override fun onText(p0: CharSequence?) {
    }

    override fun swipeLeft() {
    }

    override fun swipeRight() {
    }

    override fun swipeDown() {
    }

    override fun swipeUp() {
    }
}