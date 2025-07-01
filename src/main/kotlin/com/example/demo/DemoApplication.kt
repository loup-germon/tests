package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
}

fun cypher(letter: Char, offset: Int) : Char {
	require(letter.isUpperCase())
	require(offset >= 0)
	require(letter.isLetter())

	val  cToI: Int = letter.toInt()
	val offsetChar = ((cToI + offset) - 'A'.toInt()) % 26 + 'A'.toInt()

	return offsetChar.toChar();
}
