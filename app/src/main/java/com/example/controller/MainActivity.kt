package com.example.controller

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.content_main.*
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent
import android.content.SharedPreferences
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
//region constants
    private val alarmEarlierBySeconds:Int =60
    private val colorDelayMillis:Long = 250
    //endregion

// region variables
    private lateinit var textvw:TextView
    private lateinit var edittxt:EditText
    private lateinit var sharedPreferences:SharedPreferences

    var url = ""


    private lateinit var queue:RequestQueue
    var sendingColorQueue = 0
    lateinit var argbEnvelope: ColorEnvelope
    val continuousSender = Handler()
    // endregion

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getPreferences(MODE_PRIVATE)
        edittxt = findViewById(R.id.editText)
        textvw= findViewById(R.id.tvw)

        MailButton.setOnClickListener {
            sendRaw(edittxt.text.toString())
        }
        queue = Volley.newRequestQueue(this)
        ActivityCompat.requestPermissions(this, Array(1){"com.urbandroid.sleep.READ"}, 2)
        url  =  sharedPreferences.getString("url","http://192.168.2.105:8766")
        updateTextView()

        //region ColorPicker, Slider
        val delayOver = object:Runnable { override fun run() {
            //this runs every time the countdown is over
            if(sendingColorQueue == 1) {
                sendColor()
            }
            if(sendingColorQueue > 0) {
                sendingColorQueue -= 1
                continuousSender.postDelayed(this, colorDelayMillis)
            }
            Log.i("sending", sendingColorQueue.toString())
        } }
        continuousSender.postDelayed(delayOver, 1000)
        colorPickerView.setColorListener(object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                argbEnvelope = envelope
                if(sendingColorQueue<2){
                    if(sendingColorQueue ==0)
                        {continuousSender.postDelayed(delayOver, colorDelayMillis)}
                    sendingColorQueue=2
                    sendColor()
                }
            }
        })
        colorPickerView.setLifecycleOwner(this)
        colorPickerView.preferenceName= "MyColorPicker"
        val alphaSlideBar: AlphaSlideBar = findViewById(R.id.alphaSlideBar)
        colorPickerView.attachAlphaSlider(alphaSlideBar)
        //endregion

        edittxt.setOnKeyListener(object : View.OnKeyListener {
            // close virtual keyboard on enter
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                    // Perform action on key press
                    onClickModifyUrl(v)
                    return true
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        with (sharedPreferences.edit()) {
            putString("url", url)
            apply()
        }
    }

    fun sendColor(){
        val argbSelected = HashMap<String, String>()
        argbSelected["bri"] = (argbEnvelope.argb[0]*105/255-5).toString()
        argbSelected["r"] = argbEnvelope.argb[1].toString()
        argbSelected["g"] = argbEnvelope.argb[2].toString()
        argbSelected["b"] = argbEnvelope.argb[3].toString()
        send(argbSelected)
    }
    private fun updateTextView(){
        textvw.text = getString(R.string.destination, url)
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

    fun onClickTurnOn(@Suppress("UNUSED_PARAMETER")v: View){
        val params = HashMap<String, String>()
        params["lightOn"] = "1"
        send(params)


    }
    fun onClickTurnOff(v:View){
        val params = HashMap<String, String>()

        Snackbar.make(v, "Alarm set " + nextAlarmSeconds()/3600 + " hours " + nextAlarmSeconds()%3600/60 + " minutes from now", Snackbar.LENGTH_LONG)
            .setAction("Action", null).setDuration(5000).show()
        params["alarmIn"] = (nextAlarmSeconds()-alarmEarlierBySeconds).toString()
        params["lightOn"] = "0"
        send(params)
    }
    fun onClickModifyUrl(v:View) {
        val text = edittxt.text.toString()
        if(!text.contains('=')) {
            if (text.split('.').lastIndex == 3) {
                url =
                    if (editText.text.toString()[0] == 'h') {  // text is http://192.168.2.104:8555
                        text
                    } else {                                    // text is 192.168.2.104:8555
                        "http://$text"
                    }
            } else {
                val urlArr = url.split('.', ':').toMutableList()
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
            val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            updateTextView()
        }
        else {
            sendRaw(edittxt.text.toString())


        }
    }
    private fun sendRaw(text:String){
        val postRequest = object: StringRequest(Method.GET, "$url/?$text", Response.Listener {}, Response.ErrorListener{}){}
        queue.add(postRequest)
        Log.d("TAG", "Contacting $url/?$text")
    }
    private fun send(parameters:HashMap<String, String>){
        val postRequest = object: StringRequest(Method.POST, url, Response.Listener {}, Response.ErrorListener{}) {
            override fun getBodyContentType():String {
                return "application/x-www-form-urlencoded"
            }

            override fun getParams(): Map<String, String> {
                return parameters
            }
        }
        Log.d("sending", "Sending $parameters")
        queue.add(postRequest)

    }

    private fun nextAlarmSeconds():Int {
        val projection = arrayOf(Alarm.Columns.HOUR,  Alarm.Columns.ALARM_TIME, Alarm.Columns.ENABLED)
        val cursor = managedQuery(Alarm.Columns.CONTENT_URI, projection, Alarm.Columns.ENABLED + " = " + "1", null, Alarm.Columns.ALARM_TIME + " ASC")
        cursor.moveToFirst()
        return ((cursor.getString(1).toLong() - System.currentTimeMillis())/1000).toInt()
    }
}
