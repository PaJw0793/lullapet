package com.example.mongcare.util

import com.google.firebase.database.*

object FirebaseReadHeartRateExample {
    fun readHeartRate(onResult: (Double?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("sensor/Wearable/device1/sensorData/heartRate")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val heartRate = snapshot.getValue(Double::class.java)
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
                val heartRate = snapshot.getValue(Double::class.java)
                onChanged(heartRate)
            }
            override fun onCancelled(error: DatabaseError) {
                onChanged(null)
            }
        })
    }
}

// 사용 예시
fun main() {
    FirebaseReadHeartRateExample.readHeartRate { heartRate ->
        println("heartRate: $heartRate")
    }
}
