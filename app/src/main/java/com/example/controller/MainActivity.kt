package com.example.controller

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.postDelayed
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.content_main.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    var url = "http://192.168.2.104:8766"
    lateinit var textvw:TextView
    lateinit var edittxt:EditText
    private lateinit var queue:RequestQueue
    var sendingColorQueue= 0
    lateinit var rgbEnvelope: ColorEnvelope
    val continuousSender = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
        edittxt= findViewById(R.id.editText)
        textvw= findViewById(R.id.tvw)
        fab.setOnClickListener { view ->
            sendRaw(edittxt.text.toString())
            //Log.d("TAG", "l;ask")
        }
        queue = Volley.newRequestQueue(this)
        updateTextView()

        val delayOver = object:Runnable { override fun run() {
            if(sendingColorQueue == 1) {
                sendColor()
            }
            if(sendingColorQueue > 0) {
                sendingColorQueue -= 1
                continuousSender.postDelayed(this, 220)
            }
            Log.i("sending", sendingColorQueue.toString())

        } }
        continuousSender.postDelayed(delayOver, 1000)
        colorPickerView.setColorListener(object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                rgbEnvelope = envelope
                if(sendingColorQueue ==0){continuousSender.postDelayed(delayOver, 1000)}
                if(sendingColorQueue<2){
                    sendingColorQueue=2
                    sendColor()
                }
            }
        })
    }

    fun sendColor(){
        val rgbSelected = HashMap<String, String>()
        rgbSelected["r"] = rgbEnvelope.argb[1].toString()
        rgbSelected["g"] = rgbEnvelope.argb[2].toString()
        rgbSelected["b"] = rgbEnvelope.argb[3].toString()
        send(rgbSelected)
    }
    private fun updateTextView(){
        textvw.text = "Sending commands to $url"
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
        params["lightOn"] = "1"
        send(params)
    }
    fun onClickTurnOff(view: View){
        val params = HashMap<String, String>()
        params["lightOn"] = "0"
        send(params)
    }
    fun onClickModifyUrl(v:View) {

        val text = edittxt.text.toString()
        if(!text.contains('=')) {
            if (text.split('.').lastIndex == 4) {
                url =
                    if (editText.text.toString()[0] == 'h') {  // text is http://192.168.2.104:8555
                        text
                    } else {
                        "http://$text"
                    }
            } else {
                var urlArr = url.split('.', ':').toMutableList()
                if (text.contains(':')) {
                    if (text[0] == ':') {       // text is :8555
                        urlArr[5] = text.removePrefix(":")
                        Log.i("TAG", text)
                    } else {    // text is 104:8555
                        urlArr[4] = text.split(':')[0]
                        urlArr[5] = text.split(':')[1]
                    }
                } else {        // text is 104
                    urlArr[4] = text
                }
                url =
                    "${urlArr[0]}:${urlArr[1]}.${urlArr[2]}.${urlArr[3]}.${urlArr[4]}:${urlArr[5]}"
            }
            edittxt.text.clear()
            updateTextView()
        }
    }
    private fun sendRaw(text:String){
        val postRequest = object: StringRequest(Request.Method.GET, "$url/?$text", Response.Listener {}, Response.ErrorListener{}){}
        queue.add(postRequest)
        Log.d("TAG", "Contacting $url/?$text")
    }
    private fun send(parameters:HashMap<String, String>){
        val postRequest = object: StringRequest(Request.Method.POST, url, Response.Listener {}, Response.ErrorListener{}) {
            override fun getBodyContentType():String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): Map<String, String> {
                return parameters
            }
        }
        Log.d("sending", "Sending $parameters.toString()")
        queue.add(postRequest)

    }
}
