package sis.moto.adheber.ui.login

/**
 * Data validation state of the sign up form.
 */
data class SignUpFormState (
    val nameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordConfirmError: Int? = null,
    val isDataValid: Boolean = false
)