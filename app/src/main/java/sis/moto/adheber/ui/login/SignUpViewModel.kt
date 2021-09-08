package sis.moto.adheber.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sis.moto.adheber.R
import sis.moto.adheber.data.LoginRepository
import sis.moto.adheber.data.Result
import sis.moto.adheber.data.SignUpRepository

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel()  {
    private val _sign_upForm = MutableLiveData<SignUpFormState>()
    val signFormState: LiveData<SignUpFormState> = _sign_upForm

    private val _sign_upResult = MutableLiveData<SignUpResult>()
    val loginResult: LiveData<SignUpResult> = _sign_upResult

    /*fun sign_up(name: String, email: String, password: String, password_confirm: String) {
        // can be launched in a separate asynchronous job
        //val   loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }*/

    fun signUpDataChanged(name: String, email: String, password: String, password_confirm: String) {
        if (!isNameValid(name)) {
            _sign_upForm.value = SignUpFormState(nameError = R.string.invalid_name)
        } else if (!isEmailValid(email)) {
            _sign_upForm.value = SignUpFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _sign_upForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordConfirmValid(password,password_confirm)) {
            _sign_upForm.value = SignUpFormState(passwordConfirmError = R.string.invalid_password_confirm)
        } else {
            _sign_upForm.value = SignUpFormState(isDataValid = true)
        }
    }
    // A placeholder name validation check
    private fun isNameValid(name: String): Boolean {
        return name.length > 10
    }
    // A placeholder email validation check
    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isPasswordConfirmValid(password: String, password_confirm: String): Boolean {
        return password == password_confirm
    }
}