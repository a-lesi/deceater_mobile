package ch.enbile.deceater.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import ch.enbile.deceater.app.data.LoginRepository
import ch.enbile.deceater.app.data.Result

import ch.enbile.deceater.app.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, activity: LoginActivity) {
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            val result= LoginResult(success = LoggedInUserView(result.data.username, result.data.firstName, result.data.lastname, result.data.credential))
            activity.runOnUiThread {
                _loginResult.value = result
            }
        } else {
            val result = LoginResult(error = R.string.login_failed)
            activity.runOnUiThread {
                _loginResult.value = result
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}