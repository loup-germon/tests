package com.example.demo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll


class TestCypher : FunSpec({

    test("test cypher A + 2"){
        cypher('A',2) shouldBe 'C'
    }

    test("test cypher A + 5"){
        cypher('A',5) shouldBe 'F'
    }

    test("test cypher lowercase") {
        shouldThrow<IllegalArgumentException> { cypher('a',5) }
    }

    test("test cypher lowercase") {
        shouldThrow<IllegalArgumentException> { cypher('a',5) }
    }
    test("test cypher is letter") {
        shouldThrow<IllegalArgumentException> { cypher('%',5) }
    }
    test("test cypher negative offset") {
        shouldThrow<IllegalArgumentException> { cypher('G',-1) }
    }
    test("test cypher zero offset") {
        cypher('G',0) shouldBe 'G'
    }
    test("test cypher loop") {
        cypher('Z',1) shouldBe 'A'
    }
    test("test cypher offset loop") {
        cypher('A',26) shouldBe 'A'
    }
    test("test offset double loop") {
        cypher('B',54) shouldBe 'D'
    }
    test("check all letter characters with offset 0") {
        checkAll(Arb.char('A'..'Z'))
        {
            a -> cypher(a, 0) shouldBe a
        }
    }
    test("check all multiples of 26 don't change the letter") {
        checkAll(Arb.int(min = 0, max = 200))
        {
            multiple -> cypher('A', 26 * multiple) shouldBe 'A'
        }
    }

})