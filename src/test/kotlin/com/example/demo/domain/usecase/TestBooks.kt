package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.WriteToDb
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.mockk

class FakeDb : WriteToDb {
    private val db = mutableListOf<Book>()
    override fun findAll(): List<Book> {
        return db
    }

    override fun insert(value: Book) {
        db.add(value)
    }
}


class MockKDbTest : FunSpec({
    test("insert in db")
    {
        val fake = FakeDb()
        val service = ServiceLibrary(fake)
        val book1 = Book("stephen king", "it")
        service.insert(book1)
        service.findAll().size shouldBe 1
        service.findAll().first().author shouldBe "stephen king"
        service.findAll().first().title shouldBe "it"
    }
    test("empty strings tests")
    {
        val fake = FakeDb()
        val service = ServiceLibrary(fake)
        val book1 = Book("", "it")
        val book2 = Book("dsfdfsfd", "")
        val book3 = Book("   ", "eee")
        val book4 = Book("eee", "    ")

        shouldThrow<IllegalArgumentException> { service.insert(book1) }
        shouldThrow<IllegalArgumentException> { service.insert(book2) }
        shouldThrow<IllegalArgumentException> { service.insert(book3) }
        shouldThrow<IllegalArgumentException> { service.insert(book4) }
    }
    test("alphabetical order") {
        val fake = FakeDb()
        val service = ServiceLibrary(fake)
        val book1 = Book("Maupassant", "Asddg")
        val book2 = Book("Balzac", "aSddH")
        val book3 = Book("Stephen King", "az zz")
        val book4 = Book("Stephen King", "Party no")

        service.insert(book3)
        service.insert(book2)
        service.insert(book1)
        service.insert(book4)
        service.findAll()[0] shouldBe book1
        service.findAll()[1] shouldBe book2
        service.findAll()[2] shouldBe book3
        service.findAll()[3] shouldBe book4
    }

    test("property test") {
        val fake = FakeDb()
        val service = ServiceLibrary(fake)

        // Generator for non-empty strings
        val nonEmptyStringArb = Arb.string(minSize = 1, maxSize = 20).filter { value -> value.trim().isNotEmpty() }

        val bookArbitrary = arbitrary {
            val author = nonEmptyStringArb.bind()
            val title = nonEmptyStringArb.bind()
            Book(title, author)
        }

        val bookListArb = Arb.list(bookArbitrary, 1..5)
        checkAll(30, bookListArb) {
            bookList -> service.insertAll(bookList)
            val getList = service.findAll()
            bookList.forEach { book -> getList.shouldContain(book) }

        }
    }

})