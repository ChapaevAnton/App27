package com.w4eret1ckrtb1tch.app27

import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MainActivity : AppCompatActivity() {

    private val listItems = mutableListOf<String>().apply {
        for (i in 0..10) {
            add("item$i")
        }
    }
    private lateinit var text: TextView

    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.edit_text)
        text = findViewById(R.id.text)
        val inputText: TextInputEditText = findViewById(R.id.inputText)
        val inputTextLayout: TextInputLayout = findViewById(R.id.textInputLayout)


        // TODO: 17.07.2021 27.5. Toolbar SearchView
        val from = arrayOf("items")
        val to = intArrayOf(R.id.search_item)
        mAdapter = SimpleCursorAdapter(
            this,
            R.layout.search_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        // TODO: 16.07.2021 27.4 InputFilter
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

        // TODO: 16.07.2021 Input Mask https://github.com/RedMadRobot/input-mask-android
        val inputMask = MaskedTextChangedListener("+7 ([000]) [000]-[00]-[00]", inputText)
        inputText.addTextChangedListener(inputMask)
        inputTextLayout.onFocusChangeListener = inputMask


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
        //editText.addTextChangedListener(inputMask)
        //editText.onFocusChangeListener = inputMask

    }


    // TODO: 17.07.2021 27.5. Toolbar SearchView
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)
        val itemMenu = menu?.findItem(R.id.search_bar)
        val searchView = itemMenu?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //по нажатию на кнопку поиск
                if (listItems.contains(query)) {
                    text.text = "true"
                    text.setTextColor(Color.GREEN)
                } else {
                    text.text = "false"
                    text.setTextColor(Color.RED)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //по мере заполнения
//                if (listItems.contains(newText)) {
//                    text.text = "true"
//                    text.setTextColor(Color.GREEN)
//                } else {
//                    text.text = "false"
//                    text.setTextColor(Color.RED)
//                }
                newText?.let { populateAdapter(it) }
                return false
            }
        })

        searchView.suggestionsAdapter = mAdapter

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor: Cursor = mAdapter.getItem(position) as Cursor
                val searchString: String = cursor.getString(cursor.getColumnIndex("items"))
                searchView.setQuery(searchString, false)
                searchView.clearFocus()
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun populateAdapter(query: String) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "items"))
        for (i in listItems.indices) {
            if (listItems[i].lowercase().startsWith(query.lowercase())) {
                cursor.addRow(
                    arrayOf(
                        i,
                        listItems[i]
                    )
                )
            }
        }
        mAdapter.changeCursor(cursor)
    }


}