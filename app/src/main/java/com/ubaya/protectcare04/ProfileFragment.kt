package com.ubaya.protectcare04

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shareFile = "com.ubaya.protectcare04"
        var shared:SharedPreferences? = activity?.getSharedPreferences(shareFile,Context.MODE_PRIVATE)

        var user = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()

        val queque = Volley.newRequestQueue(activity)
        var link ="https://ubaya.fun/native/160419047/project/profile.php"
        val stringRequest = object: StringRequest(
            Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if (result.getString("result")=="OK")
                {
                    val profile = result.getJSONArray("data")
                    for (i in 0 until profile.length())
                    {
                        var profileObj = profile.getJSONObject(i)
                        with(profileObj){
                            textName.text = getString("name")
                            textDoses.text = getString("vaccinate")
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.e("paramsError",it.message.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                var params =HashMap<String,String>()
                params["username"]= user
                return params
            }
        }
        queque.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false).apply {
            fabLogout.setOnClickListener{
                activity.let {
                    val shareFile = "com.ubaya.protectcare04"
                    var shared: SharedPreferences? = activity?.getSharedPreferences(shareFile, Context.MODE_PRIVATE)
                    var editor: SharedPreferences.Editor? = shared?.edit()
                    editor?.putString(LoginActivity.SHARED_OBJECT,null)
                    editor?.apply()
                    var intent = Intent(it,LoginActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}