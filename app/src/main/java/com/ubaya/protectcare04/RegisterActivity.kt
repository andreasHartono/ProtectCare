package com.ubaya.protectcare04

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    val REQUEST_SELECT_CONTACT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textFullName.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = ContactsContract.Contacts.CONTENT_TYPE
            }
            startActivityForResult(intent, REQUEST_SELECT_CONTACT)
        }

        btnRegister.setOnClickListener {
            var name = textFullName.editText?.text.toString()
            var vaccinate = textVaccinate.editText?.text.toString()
            var username = txtUsername.editText?.text.toString()
            var password = textRegisterPass.editText?.text.toString()
            var repeat = textRepeatPassword.editText?.text.toString()
            if (password == repeat )
            {
                val queque = Volley.newRequestQueue(this)
                var link ="https://ubaya.fun/native/160419047/project/register.php"
                val stringRequest = object: StringRequest(
                    Method.POST,link,
                    Response.Listener {
                        Log.d("checkParams",it)
                        val result = JSONObject(it)
                        if (result.getString("result")=="OK")
                        {
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    },
                    Response.ErrorListener {
                        Log.e("paramsError",it.message.toString())
                    }){
                    override fun getParams(): MutableMap<String, String> {
                        var params =HashMap<String,String>()
                        params["name"] = name
                        params["username"]= username
                        params["password"]= password
                        params["vaccinate"] = vaccinate
                        return params
                    }
                }
                queque.add(stringRequest)
            }
            else
            {
                //Toast.makeText(this,"Password is not same with Repeat Password",Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                with(builder) {
                    setMessage("Password is not same with Repeat Password")
                    setPositiveButton("OK",null)
                    create().show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_SELECT_CONTACT -> {
                    val contactUri = data?.data
                    val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                    val cursor =
                        contactUri?.let { contentResolver.query(it,projection,null,null,null) }
                    if(cursor != null && cursor.moveToFirst()) {
                        val name = cursor.getString(0)
                        textFullName.editText?.setText(name)
                    }
                }
            }
        }
    }
}