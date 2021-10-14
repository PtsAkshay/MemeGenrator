package com.pebbletechsolutions.memegenrator.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat




class Permissions() : AppCompatActivity() {

     fun hasPermissions(context: Context?, vararg PERMISSIONS: Array<String>): Boolean {
        if (context != null && PERMISSIONS != null) {
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission.toString()) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Calling Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Calling Permission is denied", Toast.LENGTH_SHORT).show()
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS Permission is denied", Toast.LENGTH_SHORT).show()
            }
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission is denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}