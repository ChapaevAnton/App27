package com.w4eret1ckrtb1tch.app27

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText)
        val text: TextView = findViewById(R.id.text)


        // TODO: 16.07.2021 27.4
        val inputFilter = InputFilter { source, start, end, spanned, dStart, dEnd ->
            if (source.length <= 10) return@InputFilter source
            spanned.toString()
        }

        val inputFilter1 = InputFilter { source, start, end, spanned, dStart, dEnd ->
            if (source.contains("e", true)) return@InputFilter spanned
            source.toString()
        }

        val inputFilter2 = InputFilter.AllCaps()
        val inputFilter3 = InputFilter.LengthFilter(5)

        editText.filters = arrayOf(inputFilter2)

        // TODO: 16.07.2021 27.3 imeOptions
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val str = "${textView.text} search"
                text.text = str
                true
            } else false

        }

        // TODO: 16.07.2021 27.2 TextWatcher
        val textWatcher = object : TextWatcher {

            var length_before = 0

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //перед изменение текста
                length_before = p0?.length ?: 0
            }

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
                //в момент изменения текста
                text.text = str

            }

            override fun afterTextChanged(str: Editable?) {
                //после изменения текста
                str?.let {
                    if (length_before < it.length) {

                        if (str.length == 3 || str.length == 7)
                            str.append("-")

                        if (str.length > 3) {
                            if (Character.isDigit(str[3]))
                                str.insert(3, "-")
                        }
                        if (str.length > 7) {
                            if (Character.isDigit(str[7]))
                                str.insert(7, "-")
                        }
                    }
                }

            }

        }

        editText.addTextChangedListener(textWatcher)

    }
}