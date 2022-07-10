package com.ubaya.protectcare04

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CheckInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckInFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun user_check(username:String){
        val queque = Volley.newRequestQueue(activity)
        var link = "https://ubaya.fun/native/160419047/project/user_checkin.php"
        val stringRequest = object : StringRequest(
            Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if(result.getString("result")=="OK")
                {
                    buttonCheckIn.visibility = View.INVISIBLE
                    var checkOutFragment = CheckOutFragment()
                    activity?.supportFragmentManager?.beginTransaction()?.let {
                        it.replace(R.id.containerCheckIn, checkOutFragment)
                        it.commit()
                    }
                }
            }, Response.ErrorListener {
                Log.e("paramsError", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["username"] = username
                return params
            }
        }
        queque.add(stringRequest)
    }



    override fun onResume() {
        super.onResume()
        GlobalData.places.clear()
        GlobalData.places.add(Place("0000","Select a place..."))
        val shareFile = "com.ubaya.protectcare04"
        var shared: SharedPreferences? = activity?.getSharedPreferences(shareFile, Context.MODE_PRIVATE)
        var username = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()
        user_check(username)
        val queque = Volley.newRequestQueue(activity)
        var link ="https://ubaya.fun/native/160419047/project/place.php"
        val stringRequest = object: StringRequest(
            Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if (result.getString("result")=="OK")
                {
                    val dataPlace = result.getJSONArray("data")
                    for (i in 0 until dataPlace.length())
                    {
                        var placeObj = dataPlace.getJSONObject(i)
                        with(placeObj){
                            var location = Place(
                                getString("uniqueCode"),
                                getString("name")
                            )
                            GlobalData.places.add(location)
                        }
                    }
                }
            },
            Response.ErrorListener {
                Log.e("paramsError",it.message.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                var params =HashMap<String,String>()
                params["username"]
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
        return inflater.inflate(R.layout.fragment_check_in, container, false).apply {
            activity?.let {
                var adapter = ArrayAdapter(it,android.R.layout.simple_list_item_1,GlobalData.places)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerChoice.adapter = adapter
                val shareFile = "com.ubaya.protectcare04"
                var shared: SharedPreferences? = activity?.getSharedPreferences(shareFile, Context.MODE_PRIVATE)
                var username = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()
                var vaccine = shared?.getString(LoginActivity.SHARED_OBJECT_TWO,null).toString()
                buttonCheckIn.setOnClickListener {
                    var i = spinnerChoice.selectedItemPosition
                    //Toast.makeText(context, GlobalData.places[i].uniqueCode  + " = " + textUniqueCode.editText?.text.toString(),Toast.LENGTH_SHORT).show()
                    if(GlobalData.places[i].uniqueCode == textUniqueCode.editText?.text.toString())
                    {
                        val today = Calendar.getInstance()
                        val year = today.get(Calendar.YEAR)
                        val month = today.get(Calendar.MONTH)
                        val day = today.get(Calendar.DAY_OF_MONTH)
                        val hour = today.get(Calendar.HOUR_OF_DAY)
                        val minute = today.get(Calendar.MINUTE)
                        today.set(year, month, day, hour, minute)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val checkInDate = dateFormat.format(today.time).toString()
                        val queque = Volley.newRequestQueue(activity)
                        var link ="https://ubaya.fun/native/160419047/project/checkin.php"
                        val stringRequest = object: StringRequest(
                            Method.POST,link,
                            Response.Listener {
                                Log.d("checkParams",it)
                                val result = JSONObject(it)
                                if (result.getString("result")=="OK")
                                {
                                    buttonCheckIn.visibility = View.INVISIBLE
                                    var checkOutFragment = CheckOutFragment()
                                    activity?.supportFragmentManager?.beginTransaction()?.let{
                                        it.replace(R.id.containerCheckIn, checkOutFragment)
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
                                params["uniqueCode"] = textUniqueCode.editText?.text.toString()
                                params["checkIn"] = checkInDate
                                params["checkOut"]= "0000-00-00 00:00:00"
                                params["vaccinate"] = vaccine
                                return params
                            }
                        }
                        queque.add(stringRequest)

                    }
                    else
                    {
                        Toast.makeText(context,"Your Unique Code is incorrect, please ask the security officer to get the unique code " +
                                "and input correctly!",Toast.LENGTH_SHORT).show()
                    }
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
         * @return A new instance of fragment CheckInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CheckInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}