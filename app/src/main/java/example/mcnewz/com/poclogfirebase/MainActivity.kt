package example.mcnewz.com.poclogfirebase

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.jaredrummler.android.device.DeviceName
import kotlin.collections.ArrayList
import com.google.firebase.database.FirebaseDatabase




class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var database: DatabaseReference
    private lateinit var uid: String

    private val TAG = "Test"
    var list: ArrayList<Econsent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInstance()
        getDeviceData()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        database = FirebaseDatabase.getInstance().getReference("econsents")

        list = ArrayList()

        crashFunction()
        setupView()
    }

    private fun setupView() {
        // 1 Select all
//        database.addListenerForSingleValueEvent(queryEventListener)

        val query = database.orderByChild("name").equalTo("maniat")
        query.addListenerForSingleValueEvent(queryEventListener)

        //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"
//        val query = FirebaseDatabase.getInstance().getReference("Artists")
//            .orderByChild("id")
//            .equalTo("-LAJ7xKNj4UdBjaYr8Ju")
//
//        //3. SELECT * FROM Artists WHERE country = "India"
//        val query3 = database
//            .orderByChild("country")
//            .equalTo("India")
//
//        //4. SELECT * FROM Artists LIMIT 2
//        val query4 =database.limitToFirst(2)
//
//
//        //5. SELECT * FROM Artists WHERE age < 30
//        val query5 = database
//            .orderByChild("age")
//            .endAt(29.0)
//
//
//        //6. SELECT * FROM Artists WHERE name = "A%"
//        val query6 = database
//            .orderByChild("name")
//            .startAt("A")
//            .endAt("A\uf8ff")

    }


    private val queryEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            list?.clear()
            if (dataSnapshot.exists()) {
                for (data in dataSnapshot.children) {
                    if (data != null) {
                        val xx = data.getValue(Econsent::class.java)!!
                        list?.add(xx)
                        Log.d("testttt", xx.toString())
                        tv_text.text = list?.toString()
                        if (xx.name == "manit"){
                            Log.d("testttt", "true")
                        }
                    }else{
                        Log.d("testttt", "not Found")
                    }
                }
            }else{
                Log.d("testttt", "not exist")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    private fun crashFunction() {
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            Crashlytics.getInstance().crash() // Force a crash
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun saveCreateLog() {
        val id = database.push().key
        val data = Econsent("test", "ssssssss")

        if (id != null) {
            database.child(id).setValue(data)
                .addOnSuccessListener {
                    this.toast("success")
                }
                .addOnFailureListener { e ->
                    this.toast("fail : ${e.message}")
                }
        }


    }

    private fun initInstance() {
        btn_submit.setOnClickListener {
            val number = (1..1000).random()
            writeLogFirebase("Helloooooo : $number")
            saveCreateLog()
        }
    }


    private fun writeLogFirebase(number: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Hello")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "NEWZ")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "tttttttttttttttt")
        bundle.putString("user_hello", number)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }


    // Utils
    fun Context.toast(resId: String, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, resId, duration).show()

    fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) + start

    // Device Detail
    fun getDeviceData() {
        var detail = ""
        DeviceName.with(this).request { info, error ->
            val manufacturer = info.manufacturer  // "Samsung"
            val name = info.marketName            // "Galaxy S8+"
            val model = info.model                // "SM-G955W"
            val codename = info.codename          // "dream2qltecan"
            val deviceName = info.name       // "Galaxy S8+"
            // FYI: We are on the UI thread.
            detail = "$manufacturer | $name | $model | $codename | $deviceName"
            this.toast("data : " + detail)
        }
    }
}
