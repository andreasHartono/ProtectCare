package com.ubaya.protectcare04

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import kotlinx.android.synthetic.main.fragment_check_out.*
import kotlinx.android.synthetic.main.fragment_check_out.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CheckOutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckOutFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val shareFile = "com.ubaya.protectcare04"
        var shared:SharedPreferences? = activity?.getSharedPreferences(shareFile,Context.MODE_PRIVATE)
        var username = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()
        var vaccine = shared?.getString(LoginActivity.SHARED_OBJECT_TWO,null).toString()
        val queque = Volley.newRequestQueue(activity)
        var link = "https://ubaya.fun/native/160419047/project/body_checkout.php"
        val stringRequest = object : StringRequest(
            Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if(result.getString("result")=="OK")
                {
                    val data = result.getJSONArray("data")
                    for(i in 0 until  data.length())
                    {
                        var checkOutObj = data.getJSONObject(i)
                        with(checkOutObj) {
                            if(vaccine == "1")
                            {
                                cardCheckOut.setCardBackgroundColor(Color.YELLOW)
                            }
                            else if(vaccine == "2")
                            {
                                cardCheckOut.setCardBackgroundColor(Color.GREEN)
                            }
                            textViewPlaceOut.text = getString("name")
                            var ciFormat = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(getString("checkIn"))
                            var dateFormat = SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm")
                            textViewDateTime.text = dateFormat.format(ciFormat.time).toString()
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.e("paramsError",it.message.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String,String>()
                params["username"] = username
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
        return inflater.inflate(R.layout.fragment_check_out, container, false).apply {



            buttonCheckOut.setOnClickListener{
                val shareFile = "com.ubaya.protectcare04"
                var shared: SharedPreferences? = activity?.getSharedPreferences(shareFile, Context.MODE_PRIVATE)
                var username = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()
                val today = Calendar.getInstance()
                val year = today.get(Calendar.YEAR)
                val month = today.get(Calendar.MONTH)
                val day = today.get(Calendar.DAY_OF_MONTH)
                val hour = today.get(Calendar.HOUR_OF_DAY)
                val minute = today.get(Calendar.MINUTE)
                today.set(year, month, day, hour, minute)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val checkOutDate = dateFormat.format(today.time).toString()
                val queque = Volley.newRequestQueue(activity)
                var link ="https://ubaya.fun/native/160419047/project/checkout.php"
                val stringRequest = object: StringRequest(
                    Method.POST,link,
                    Response.Listener {
                        Log.d("checkParams",it)
                        val result = JSONObject(it)
                        if (result.getString("result")=="OK")
                        {
                            activity?.supportFragmentManager?.beginTransaction()?.let{
                                it.replace(R.id.containerCheckIn, CheckInFragment())
                                it.commit()
                            }
                        }
                    },
                    Response.ErrorListener {
                        Log.e("paramsError",it.message.toString())
                    }){
                    override fun getParams(): MutableMap<String, String> {
                        var params =HashMap<String,String>()
                        params["username"] = username
                        params["checkOut"] = checkOutDate

                        return params
                    }
                }
                queque.add(stringRequest)

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
         * @return A new instance of fragment CheckOutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CheckOutFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}