package com.dps0340.attman

object SYMPTOMS {
    val english = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    val englishFull = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    val korean = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
    val mapped = englishFull.zip(korean)
}