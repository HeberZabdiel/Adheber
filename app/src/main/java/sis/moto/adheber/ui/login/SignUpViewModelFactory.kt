package sis.moto.adheber.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sis.moto.adheber.data.SignUpDataSource
import sis.moto.adheber.data.SignUpRepository

class SignUpViewModelFactory: ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                signUpRepository = SignUpRepository(
                    dataSource = SignUpDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}