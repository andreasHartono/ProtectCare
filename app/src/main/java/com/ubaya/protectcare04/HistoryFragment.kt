package com.ubaya.protectcare04

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    var arrayHistory:ArrayList<History> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        arrayHistory.clear()
        val shareFile = "com.ubaya.protectcare04"
        var shared: SharedPreferences? = activity?.getSharedPreferences(shareFile, Context.MODE_PRIVATE)
        var username = shared?.getString(LoginActivity.SHARED_OBJECT,null).toString()
        val queque = Volley.newRequestQueue(activity)
        var link ="https://ubaya.fun/native/160419047/project/history.php"
        val stringRequest = object: StringRequest(
            Method.POST,link,
            Response.Listener {
                Log.d("checkParams",it)
                val result = JSONObject(it)
                if (result.getString("result")=="OK")
                {
                   val data = result.getJSONArray("data")
                    for (i in 0 until data.length())
                    {
                        var historyObj = data.getJSONObject(i)
                        with(historyObj){
                            var history = History(
                                getString("name"),
                                getString("checkIn"),
                                getString("checkOut"),
                                getInt("vaccinate")
                            )
                            arrayHistory.add(history)
                        }
                    }
                    updateList()
                }
            },
            Response.ErrorListener {
                Log.e("paramsError",it.message.toString())
            }){
            override fun getParams(): MutableMap<String, String> {
                var params =HashMap<String,String>()
                params["username"]= username
                return params
            }
        }
        queque.add(stringRequest)
    }
    private fun updateList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        HistoryView?.let {
            it.layoutManager = linearLayoutManager
            it.setHasFixedSize(true)
            it.adapter = HistoryAdapter(arrayHistory)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HistoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}