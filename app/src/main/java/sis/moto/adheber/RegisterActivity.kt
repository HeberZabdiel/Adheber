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
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sis.moto.adheber.ui.login.*
import kotlinx.android.synthetic.main.activity_register.*


@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]
        auth = Firebase.auth

        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)

        signUpViewModel.signFormState.observe(this@RegisterActivity, Observer {
            val signUpState = it ?: return@Observer
            // disable login button unless both username / password is valid
            sign_up.isEnabled = signUpState.isDataValid
            if (signUpState.nameError != null) {
                name.error = getString(signUpState.nameError)
            }
            if (signUpState.emailError != null) {
                email.error = getString(signUpState.emailError)
            }
            if (signUpState.passwordError != null) {
                password.error = getString(signUpState.passwordError)
            }
            if (signUpState.passwordConfirmError != null) {
                password_confirm.error = getString(signUpState.passwordConfirmError)
            }
        })
        signUpViewModel.signUpResult.observe(this@RegisterActivity, Observer {
            val signUpResult = it ?: return@Observer
            loading_sign_up.visibility = View.GONE
            if (signUpResult.error != null) {
                showSignUpFailed(signUpResult.error)
            }
            if (signUpResult.success != null) {
                sign_up.isEnabled = true
                sign_up.setOnClickListener {
                    updateUiWithUser(1,
                        signUpResult.success,
                        email.text.toString(),
                        password.text.toString()
                    )
                }
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //finish()
        })
        name.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
            signUpViewModel.sign_up(
                name.text.toString(), email.text.toString(),
                password.text.toString(), password_confirm.text.toString()
            )
        }
        email.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
            signUpViewModel.sign_up(
                name.text.toString(), email.text.toString(),
                password.text.toString(), password_confirm.text.toString()
            )
        }
        password.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                password_confirm.text.toString()
            )
            signUpViewModel.sign_up(
                name.text.toString(), email.text.toString(),
                password.text.toString(), password_confirm.text.toString()
            )
        }

        password_confirm.apply {
            afterTextChanged {
                signUpViewModel.signUpDataChanged(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    password_confirm.text.toString()
                )
                signUpViewModel.sign_up(
                    name.text.toString(), email.text.toString(),
                    password.text.toString(), password_confirm.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signUpViewModel.sign_up(
                            name.text.toString(),
                            email.text.toString(),
                            password.text.toString(),
                            password_confirm.text.toString()
                        )
                }
                false
            }

        }
    }

    private fun updateUiWithUser(tipo: Int,model: SignUpInUserView, emailText: String, passwordText: String) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        loading_sign_up.visibility = View.VISIBLE
        if(tipo == 1) {
            // [START sign_in_with_email]
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
            // [END sign_in_with_email]
        }
        //Acceso con Google
        if(tipo == 2) {
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
        Thread.sleep(2000)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

        val start = Intent(this, MainActivity::class.java)
        startActivity(start)
        finish()
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
        updateUI(currentUser)
    }

    private fun reload() {

    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
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
        private const val TAGG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAGG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAGG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]
    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAGG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAGG, "Google sign in failed", e)
            }
        }
    }
    // [END onactivityresult]
}