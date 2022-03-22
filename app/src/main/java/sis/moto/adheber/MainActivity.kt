package sis.moto.adheber

//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sis.moto.adheber.databinding.ActivityMainBinding
import sis.moto.adheber.databinding.ActivityRegisterBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_main)
    }
}