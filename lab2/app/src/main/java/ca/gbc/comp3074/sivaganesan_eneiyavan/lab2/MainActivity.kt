package ca.gbc.comp3074.sivaganesan_eneiyavan.lab2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0
    private var step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        val tvOutput = findViewById<TextView>(R.id.tvOutput)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnStep = findViewById<Button>(R.id.btnStep)

        // Add (+): Increases output by step amount (1 by default, 2 after Step is pressed)
        btnPlus.setOnClickListener {
            count += step
            tvOutput.text = count.toString()
        }

        // Subtract (-): Decreases output by step amount (1 by default, 2 after Step is pressed)
        btnMinus.setOnClickListener {
            count -= step
            tvOutput.text = count.toString()
        }

        // Reset: Sets output to 0 and resets step behavior back to 1
        btnReset.setOnClickListener {
            count = 0
            step = 1
            tvOutput.text = count.toString()
        }

        // Step: Changes step behavior to increase/decrease by 2
        btnStep.setOnClickListener {
            step = 2
        }
    }
}