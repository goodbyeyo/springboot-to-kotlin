package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.JavaUser
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val bookService: BookService,
){
    @Test
    @DisplayName("책 등록 테스트")
    fun saveBookTest() {
        bookService.saveBook(BookRequest("죄와벌", BookType.SOCIETY))
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("죄와벌")
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
        bookRepository.deleteAll()
    }

    @Test
    @DisplayName("책 대여 정상 동작")
    fun loanBookTest() {
        bookRepository.save(Book.fixture("노인과 바다"))
        val savedUser = userRepository.save(User("상욱", 30))
        val request = BookLoanRequest("상욱", "노인과 바다")

        bookService.loanBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("노인과 바다")
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].user.name).isEqualTo(savedUser.name)
        assertThat(results[0].user.age).isEqualTo(savedUser.age)
    }

    @Test
    @DisplayName("이미 대출된 책이면 대출 실패")
    fun loanBookFailTest() {
        bookRepository.save(Book.fixture("노인과 바다"))
        val savedUser = userRepository.save(User("상욱", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "노인과 바다"))

        assertThrows<IllegalArgumentException>{
            bookService.loanBook(BookLoanRequest("다현", "노인과 바다"))
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @Test
    @DisplayName("책 반납 정상 동작")
    fun returnBookTest() {
        val savedUser = userRepository.save(User("wook", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "노인과 바다"))
        val request = BookReturnRequest("wook", "노인과 바다")

        bookService.returnBook(request)

        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }



}