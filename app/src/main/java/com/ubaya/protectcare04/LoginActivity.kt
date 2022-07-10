package com.ubaya.protectcare04

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    companion object{
        val SHARED_OBJECT = "SHARED_OBJECT"
        val SHARED_OBJECT_TWO = "SHARED_OBJECT_TWO"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val shareFile = "com.ubaya.protectcare04"
        var shared:SharedPreferences = getSharedPreferences(shareFile,Context.MODE_PRIVATE)
        var editor:SharedPreferences.Editor = shared.edit()

        var user = shared.getString(SHARED_OBJECT,null)
        if(user!=null)
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonLogin.setOnClickListener {
            var username =textUsername.editText?.text.toString()
            var password = textLoginPass.editText?.text.toString()
            val queque = Volley.newRequestQueue(this)
            var link ="https://ubaya.fun/native/160419047/project/login.php"
            val stringRequest = object:StringRequest(Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if (result.getString("result")=="success")
                {
                    var data = result.getJSONObject("data")
                    editor.putString(SHARED_OBJECT,username)
                    editor.putString(SHARED_OBJECT_TWO,data.getString("vaccinate"))
                    editor.apply()
                    val intent = Intent(this,MainActivity::class.java)

                    /*val sendIntent = Intent().apply {
                        putExtra("SHARED_OBJECT",username)
                        putExtra("SHARED_OBJECT_TWO",data.getString("vaccinate"))
                    }*/
                    //setResult(RESULT_OK,sendIntent)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    //Toast.makeText(this,"Incorrect Username or Password",Toast.LENGTH_SHORT).show()
                    val builder = AlertDialog.Builder(this)
                    with(builder) {
                        setMessage("Invalid Login, please input username or password correctly!")
                        setPositiveButton("OK",null)
                        create().show()
                    }
                }
            },
            Response.ErrorListener {
                Log.e("paramsError",it.message.toString())
            }){
                override fun getParams(): MutableMap<String, String> {
                    var params =HashMap<String,String>()
                    params["username"]= username
                    params["password"]= password
                    return params
                }
            }
            queque.add(stringRequest)
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}