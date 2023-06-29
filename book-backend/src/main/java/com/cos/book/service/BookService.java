package com.cos.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	
	@Transactional //서비스 함수가 종료될 때 commit 할지 rollback할지 트랙잭션관리한다.
	public Book 저장하기(Book book) {
		return bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)
	public Book 한건가져오기(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("ID를 확인해주세요"));
	}
	
	@Transactional(readOnly = true)
	public List<Book> 모두가져오기(){
		return bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Book book, Long id) {
		//더티체킹 update치기
		Book bookEntity =  bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("ID를 확인해주세요")); //영속화->영속화 컨텍스트 보관
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		
		return bookEntity;
	}//함수 종료=> 트랜잭션 종료 = > 영속화되어있는 데이터를 db로 갱신(flush) =>commit ==> 더티체킹
	
	@Transactional
	public String 삭제하기(Long id) {
		bookRepository.deleteById(id);
		return "OK";
				
	}
}
