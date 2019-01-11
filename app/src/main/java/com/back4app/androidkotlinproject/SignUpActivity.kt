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

import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SignUpCallback

class SignUpActivity : AppCompatActivity() {
    private var usernameView: EditText? = null
    private var passwordView: EditText? = null
    private var passwordAgainView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val back_button = findViewById(R.id.back_btn) as Button

        back_button.setOnClickListener(View.OnClickListener {
            val dlg = ProgressDialog(this@SignUpActivity)
            dlg.setTitle("Please, wait a moment.")
            dlg.setMessage("Returning to the login section...")
            dlg.show()
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            dlg.dismiss()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
        usernameView = findViewById<View>(R.id.username) as EditText
        passwordView = findViewById<View>(R.id.password) as EditText
        passwordAgainView = findViewById(R.id.passwordAgain) as EditText

        val signup_button = findViewById<Button>(R.id.signup_button)
        signup_button.setOnClickListener(View.OnClickListener {
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
            if (isEmpty(passwordAgainView!!)) {
                if (validationError) {
                    validationErrorMessage.append(" and ")
                }
                validationError = true
                validationErrorMessage.append("your password again")
            } else {
                if (!isMatching(passwordView!!, passwordAgainView!!)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ")
                    }
                    validationError = true
                    validationErrorMessage.append("the same password twice.")
                }
            }
            validationErrorMessage.append(".")

            if (validationError) {
                Toast.makeText(this@SignUpActivity, validationErrorMessage.toString(), Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            //Setting up a progress dialog
            val dlg = ProgressDialog(this@SignUpActivity)
            dlg.setTitle("Please, wait a moment.")
            dlg.setMessage("Signing up...")
            dlg.show()

            val user = ParseUser()
            user.username = usernameView!!.text.toString()
            user.setPassword(passwordView!!.text.toString())
            user.signUpInBackground { e ->
                if (e == null) {
                    dlg.dismiss()
                    alertDisplayer("Sucessful Login", "Welcome " + usernameView!!.text.toString() + "!")

                } else {
                    dlg.dismiss()
                    ParseUser.logOut()
                    Toast.makeText(this@SignUpActivity, "", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isEmpty(text: EditText): Boolean {
        return if (text.text.toString().trim { it <= ' ' }.length > 0) {
            false
        } else {
            true
        }
    }

    private fun isMatching(text1: EditText, text2: EditText): Boolean {
        return if (text1.text.toString() == text2.text.toString()) {
            true
        } else {
            false
        }
    }

    private fun alertDisplayer(title: String, message: String) {
        val builder = AlertDialog.Builder(this@SignUpActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.cancel()
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
        val ok = builder.create()
        ok.show()
    }
}
