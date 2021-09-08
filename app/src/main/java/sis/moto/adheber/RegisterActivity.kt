package sis.moto.adheber

//import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sis.moto.adheber.ui.login.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val name: EditText = findViewById(R.id.name)
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val password_confirm: EditText = findViewById(R.id.password_confirm)
        val sign_up: Button = findViewById(R.id.sign_up)
        val loading: ProgressBar = findViewById(R.id.loading_sign_up)
        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)

        signUpViewModel.signFormState.observe(this@RegisterActivity, Observer {
            val loginState = it ?: return@Observer
            // disable login button unless both username / password is valid
            sign_up.isEnabled = loginState.isDataValid
            if (loginState.nameError != null) {
                name.error = getString(loginState.nameError)
            }
            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
            if (loginState.passwordConfirmError != null) {
                password_confirm.error = getString(loginState.passwordConfirmError)
            }
        })
        signUpViewModel.loginResult.observe(this@RegisterActivity, Observer {
            val signUpResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (signUpResult.error != null) {
                showSignUpFailed(signUpResult.error)
            }
            if (signUpResult.success != null) {
                updateUiWithUser(signUpResult.success, email.text.toString(),password.text.toString())
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })
        name.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
        }
        email.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
        }
        password.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.signUpDataChanged(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    password_confirm.text.toString()
                )
            }

            /*setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }*/

            /*sign_up.setOnClickListener {
                signUpViewModel.login(username.text.toString(), password.text.toString())
            }*/

        }
    }

    private fun updateUiWithUser(model: SignUpInUserView, email: String, password: String) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        val progress: ProgressBar = findViewById(R.id.loading)
        progress.visibility = View.VISIBLE
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(RegisterActivity.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(RegisterActivity.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
        Thread.sleep(4000)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

        val start = Intent(this, MainActivity::class.java)
        startActivity(start)
    }

    private fun updateUI(user: FirebaseUser?) {

    }
    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }
    private fun reload() {

    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
    companion object {
        private const val TAG = "EmailPassword"
    }

}