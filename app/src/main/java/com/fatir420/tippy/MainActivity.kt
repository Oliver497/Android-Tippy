package com.fatir420.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

// class name
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentageLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekbarTip)
        tvTipPercentageLabel = findViewById(R.id.tvTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentageLabel.text = "$INITIAL_TIP_PERCENT%"

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                every time progress changed on a seekBar, (user is scrubbing)
//                print out curr value shown in logcat
                Log.i(TAG, "OnProgressChanged $progress") //log statement
//                tvTipPercentageLabel.text = progress.toString() me
                tvTipPercentageLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

//        object-ex of how to create anonymous class, class that are one time use only
//        mostly used to implement interfaces
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // msg is a hint of what function is being used
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        // check if tipPercent is not an INT

        val tipDescription: String = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        // check if tipDescription is not String

        tvTipDescription.text = tipDescription
//        update tipDescription based on tipPercent
//        compute a color based off of an Integer: Interpolation
        val color = ArgbEvaluator().evaluate(
//            tipPercent<int> to <float> = 0.0 / seekBarTip.max = 30
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)

    }

    private fun computeTipAndTotal() {
//        this text is empty, clear out values and return (so nothing else is executed)
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
//        1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble() // we need a double
        val tipPercent = seekBarTip.progress
//        2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
//        3. Update the UI
//          - give me only two decimal places
        tvTipAmount.text = "%2f".format(tipAmount)
        tvTotalAmount.text = "%2f".format(totalAmount)
    }
}

/*
    Extensions to this basic tippy calculator:
    1) Split bill by in users total and divide evenly by total users
    2) Round the final amount up or down
    3) Change the design up
 */