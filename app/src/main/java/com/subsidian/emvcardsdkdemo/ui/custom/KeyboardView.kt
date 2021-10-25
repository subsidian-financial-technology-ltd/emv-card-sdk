package com.subsidian.emvcardsdkdemo.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.IdRes
import com.subsidian.emvcardsdkdemo.R
import java.lang.Exception

class KeyboardView : FrameLayout, View.OnClickListener {

    private var mCurrencyField: CurrencyEditText? = null
    private var mStoreButton: Button? = null

    val inputText: String
        get() = mCurrencyField!!.text.toString()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.keyboard, this)
        initViews()
    }

    private fun initViews() {
        mCurrencyField = layoutView(R.id.currency_field)
        mCurrencyField!!.setText("0.00")
        mStoreButton = layoutView(R.id.store_btn)
        layoutView<View>(R.id.t9_key_0).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_1).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_2).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_3).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_4).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_5).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_6).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_7).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_8).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_9).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_clear).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_backspace).setOnClickListener(this)
        layoutView<View>(R.id.t9_key_add_sale).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        // handle number button click
        if (v.tag != null && "number_button" == v.tag) {
            mCurrencyField!!.append((v as TextView).text)
            return
        }
        when (v.id) {
            R.id.t9_key_clear -> {
            // handle clear button
//                clearCurrentAmount()
            }
            R.id.t9_key_backspace -> { // handle backspace button
                // delete one character
                val editable = mCurrencyField!!.text
                val charCount = editable!!.length
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount)
                }
            }
        }
    }

    protected fun <T : View> layoutView(@IdRes id: Int): T {
        return super.findViewById<View>(id) as T
    }

    fun getButtonView(@IdRes id: Int): View {
        return findViewById<View>(id)
    }

    fun clearCurrentAmount(value: String?) {
        val editable = mCurrencyField!!.text
        try {
            val charCount = editable!!.length
            if (charCount > 0) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrencyTextView(): CurrencyEditText {
        return mCurrencyField!!
    }
}
