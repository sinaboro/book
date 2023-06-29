package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//단위 테스트(Controller , Filter, ControllerAdvice 띄우기)

//JUnit4버전에서는 @RunWith(SpringRunner.class)를 붙인다..
//그러나 JUnit5버전에서는 @WebMvcTest 어노테이션이 @RunWith(SpringRunner.class)를 대신하는
//@ExtendWith(SpringExtension.class)를 포함하고있으므로 붙일 필요성이 없다. 즉 @RunWith(SpringRunner.class) 이 것 추가안해도된다.

@WebMvcTest
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean  //IOc환경에 bean등록
	private BookService bookService;
	
	
	//BDDMockito 패턴 given, when, then
	@Test
	public void sava_테스트() throws Exception {
		
		Book book = new Book(null,"스프링 따라하기" ,"코스");
		//given(테스트하기위한 준비)
		String content = new ObjectMapper().writeValueAsString(book);
//		log.info("json : {}",content);
		
		//when(bookService.저장하기(book)
		when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기" ,"코스"));
		
		//when(테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then(검증)
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("스프링 따라하기"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void findAll_테스트() throws Exception{
		//given  ==> 데이타가 존재하지않기 때문에 미리 넣어준다.
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링부트 따라하기", "코스"));
		books.add(new Book(2L,"리액트 따라하기", "코스"));
		when(bookService.모두가져오기()).thenReturn(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id=1L;
		when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "코스"));
		
		//when
		ResultActions actions = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("자바 공부하기"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void update_테스트() throws Exception{
		//given
		Long id=1L;
		Book book = new Book(null,"Vue 따라하기" ,"코스");
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookService.수정하기(book, id)).thenReturn(new Book(1L,"Vue 따라하기" ,"코스"));
		
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("Vue 따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void delete_테스트() throws Exception{
		//given
		Long id=1L;
		when(bookService.삭제하기(id)).thenReturn("OK");
		
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
				.accept(MediaType.TEXT_PLAIN_VALUE));
		
		//then
		resultAction
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		MvcResult mvcResult  = resultAction.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals("OK", result);
	}
	
	
}
