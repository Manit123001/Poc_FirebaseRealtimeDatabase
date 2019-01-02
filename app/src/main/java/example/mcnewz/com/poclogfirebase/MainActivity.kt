package example.mcnewz.com.poclogfirebase

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val context: Context = applicationContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context.toast("Hello")
    }

    fun Context.toast(resId: String, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, resId, duration).show()

}
