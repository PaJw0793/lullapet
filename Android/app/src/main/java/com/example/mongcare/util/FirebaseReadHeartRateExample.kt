// 이 파일을 app/src/main/java/com/example/mongcare/util/FirebaseReadHeartRateExample.kt 위치로 이동하세요.
package com.example.mongcare.util

import com.google.firebase.database.*
import kotlin.math.roundToInt

object FirebaseReadHeartRateExample {
    fun readHeartRate(onResult: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/heartRate")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val heartRate = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onResult(heartRate)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    fun addHeartRateListener(onChanged: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/heartRate")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val heartRate = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onChanged(heartRate)
            }
            override fun onCancelled(error: DatabaseError) {
                onChanged(null)
            }
        })
    }
}