package com.example.controller

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
//import javax.swing.UIManager.put
import org.json.JSONObject
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.android.volley.AuthFailureError
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {
    private lateinit var queue:RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        queue = Volley.newRequestQueue(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun onClickTurnOn(view: View){
        val params = HashMap<String, String>()
        params["On"] = "1"
        params["lightOn"] = "1"
        send(params)
    }
    fun onClickTurnOff(view: View){
        val params = HashMap<String, String>()
        params["ltOn"] = "1"
        params["lightOn"] = "0"
        send(params)
    }
    private fun send(parameters:HashMap<String, String>){
        val postRequest = object: StringRequest(Request.Method.POST, "http://192.168.2.104:8766", Response.Listener {}, Response.ErrorListener{}) {
            override fun getBodyContentType():String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): Map<String, String> {
                return parameters
            }
        }
        Log.d("TAG", "Sending " + parameters.toString())
        queue.add(postRequest)
    }
}
