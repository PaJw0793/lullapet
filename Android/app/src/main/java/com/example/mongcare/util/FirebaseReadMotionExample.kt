// 이 파일을 app/src/main/java/com/example/mongcare/util/FirebaseReadMotionExample.kt 위치로 이동하세요.
package com.example.mongcare.util

import com.google.firebase.database.*

object FirebaseReadMotionExample {
    fun readMotion(onResult: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/motion")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val motion = snapshot.getValue(Double::class.java)
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
                val motion = snapshot.getValue(Double::class.java)
                onChanged(motion)
            }
            override fun onCancelled(error: DatabaseError) {
                onChanged(null)
            }
        })
    }
}

// 사용 예시
fun main() {
    FirebaseReadMotionExample.readMotion { motion ->
        println("motion: $motion")
    }
}
