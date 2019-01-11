package com.back4app.androidkotlinproject

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {

    private var usernameView: EditText? = null
    private var passwordView: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameView = findViewById(R.id.username) as EditText
        passwordView = findViewById(R.id.password) as EditText

        val login_button = findViewById(R.id.login_button) as Button

        login_button.setOnClickListener(View.OnClickListener {
            //Validating the log in data
            var validationError = false

            val validationErrorMessage = StringBuilder("Please, insert ")
            if (isEmpty(usernameView!!)) {
                validationError = true
                validationErrorMessage.append("an username")
            }
            if (isEmpty(passwordView!!)) {
                if (validationError) {
                    validationErrorMessage.append(" and ")
                }
                validationError = true
                validationErrorMessage.append("a password")
            }
            validationErrorMessage.append(".")

            if (validationError) {
                Toast.makeText(this@LoginActivity, validationErrorMessage.toString(), Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            //Setting up a progress dialog
            val dlg = ProgressDialog(this@LoginActivity)
            dlg.setTitle("Please, wait a moment.")
            dlg.setMessage("Logging in...")
            dlg.show()

            ParseUser.logInInBackground(usernameView!!.text.toString(), passwordView!!.text.toString()) { parseUser, e ->
                if (parseUser != null) {
                    dlg.dismiss()
                    alertDisplayer("Sucessful Login", "Welcome back " + usernameView!!.text.toString() + "!")

                } else {
                    dlg.dismiss()
                    ParseUser.logOut()
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        val signup_button = findViewById(R.id.signup_button) as Button

        signup_button.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
    }

    private fun isEmpty(text: EditText): Boolean {
        return if (text.text.toString().trim { it <= ' ' }.length > 0) {
            false
        } else {
            true
        }
    }

    private fun alertDisplayer(title: String, message: String) {
        val builder = AlertDialog.Builder(this@LoginActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.cancel()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
        val ok = builder.create()
        ok.show()
    }
}
