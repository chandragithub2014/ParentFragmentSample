package colruyt.android.parentfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import colruyt.android.parentfragment.views.ParentFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val parentFragment = ParentFragment.newInstance("","")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,parentFragment).commit()
    }
}