// 이 파일을 app/src/main/java/com/example/mongcare/util/FirebaseReadTemperatureExample.kt 위치로 이동하세요.
package com.example.mongcare.util

import com.google.firebase.database.*
import kotlin.math.roundToInt

object FirebaseReadTemperatureExample {
    fun readTemperature(onResult: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/temperature")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val temperature = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onResult(temperature)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    fun addTemperatureListener(onChanged: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/temperature")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val temperature = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onChanged(temperature)
            }
            override fun onCancelled(error: DatabaseError) {
                onChanged(null)
            }
        })
    }
}