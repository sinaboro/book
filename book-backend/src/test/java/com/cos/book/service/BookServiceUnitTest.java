package com.cos.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cos.book.domain.BookRepository;

/*단위 테스트(Service와 관련된 대상만 IOC메모리 띄운다)
 * 단위 테스트 이므로 BookService이것만 있으면된다. 그러나 db저장하기 위해서는 
 * BookRepository 이것도 필요하다 . 이것도 추가하면 통합테스트 랑 차이가 없어진다.
 * 따라서 BookRepository 가짜 객체로 만들어서 테스트 할 수 있다.
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@Mock  //Mock를 이용해서 가짜 객체를 생성
	private BookRepository bookRepository;
	
	@InjectMocks //BookService객체가 만들어 질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다.
	private BookService bookService;
}
