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




    /*
    *
    *パーミッションに関する記事
    * https://qiita.com/kaleidot725/items/fa31476d7b7076265b3d
    *
     */

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

        /*
        *
        * フォアグラウンドサービスに関する記事
        * https://unluckysystems.com/%E3%83%90%E3%83%83%E3%82%AF%E3%82%B0%E3%83%A9%E3%82%A6%E3%83%B3%E3%83%89%E3%81%A7%E5%8B%95%E4%BD%9C%E3%81%95%E3%81%9B%E3%82%8B%E3%81%9F%E3%82%81%E3%81%AE%E3%83%95%E3%82%A9%E3%82%A2%E3%82%B0%E3%83%A9/
        *
         */

        //サービスの開始
        binding.ibeeconStartButton.setOnClickListener {
            //intentのインスタンス化
            val intent = Intent(this, ForegroundIbeaconOutputServise::class.java)
            //値をintentした時に受け渡しをする用
            intent.putExtra("UUID",binding.uuidEditTextbox.text.toString())
            intent.putExtra("MAJOR",binding.majarEditTextbox.text.toString())
            intent.putExtra("MINOR",binding.minorEditTextbox.text.toString())

            //サービスの開始
            //パーミッションの確認をする。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && EasyPermissions.hasPermissions(this, *permissions)) {
                startForegroundService(intent)
            }
        }

        //サービスを止める。
        binding.ibeaconStopButton.setOnClickListener {
            val targetIntent = Intent(this, ForegroundIbeaconOutputServise::class.java)
            stopService(targetIntent)
        }






    }
}