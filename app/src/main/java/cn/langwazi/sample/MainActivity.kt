package cn.langwazi.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.langwazi.loadingbutton.LoadingButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingButton.setOnClickListener({
            loadingButton.setStatus(LoadingButton.STATUS_LOADING)
            Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show()
        })

        stop.setOnClickListener({
            loadingButton.setStatus(LoadingButton.STATUS_NORMAL)
        })

    }
}
