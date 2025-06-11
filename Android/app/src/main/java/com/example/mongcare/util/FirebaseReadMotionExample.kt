// 이 파일을 app/src/main/java/com/example/mongcare/util/FirebaseReadMotionExample.kt 위치로 이동하세요.
package com.example.mongcare.util

import com.google.firebase.database.*
import kotlin.math.roundToInt

object FirebaseReadMotionExample {
    fun readMotion(onResult: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/motion")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val motion = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onResult(motion)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    fun addMotionListener(onChanged: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/motion")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 값을 소수점 한 자리로 변환
                val motion = snapshot.getValue(Double::class.java)?.let { (it * 10).roundToInt() / 10.0 }
                onChanged(motion)
            }
            override fun onCancelled(error: DatabaseError) {
                onChanged(null)
            }
        })
    }
}