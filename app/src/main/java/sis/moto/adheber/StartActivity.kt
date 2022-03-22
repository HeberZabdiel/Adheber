package sis.moto.adheber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sis.moto.adheber.databinding.ActivityMainBinding
import sis.moto.adheber.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_start)
    }
}