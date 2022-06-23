package io.github.com.harutiro.ibeaconforegroundoutputtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.com.harutiro.ibeaconforegroundoutputtest.databinding.ActivityMainBinding
import io.github.com.harutiro.ibeaconforegroundoutputtest.service.ForegroundIbeaconOutputServise
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    //bindingのインスタンス化
    private lateinit var binding: ActivityMainBinding

    //intent用のID指定。Intentから戻ってくる時に使うとかなんとか。
    private val PERMISSION_REQUEST_CODE = 1


    //許可して欲しいパーミッションの記載、
    //Android１２以上ではBlueToothの新しいパーミッションを追加する。
    val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE

        )
    }else{
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //パーミッション確認
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            // パーミッションが許可されていない時の処理
            EasyPermissions.requestPermissions(this, "パーミッションに関する説明", PERMISSION_REQUEST_CODE, *permissions)
        }

        binding.ibeeconStartButton.setOnClickListener {
            val intent = Intent(this, ForegroundIbeaconOutputServise::class.java)
            intent.putExtra("UUID",binding.uuidEditTextbox.text.toString())
            intent.putExtra("MAJOR",binding.majarEditTextbox.text.toString())
            intent.putExtra("MINOR",binding.minorEditTextbox.text.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && EasyPermissions.hasPermissions(this, *permissions)) {
                startForegroundService(intent)
            }
        }

        binding.ibeaconStopButton.setOnClickListener {
            val targetIntent = Intent(this, ForegroundIbeaconOutputServise::class.java)
            stopService(targetIntent)
        }






    }
}