package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

//통합 테스트(모든 Bean들을 똑같이 IOC올리고 테스트 하는 것)

//@SpringBootTest => Book프로젝트 관련 모든 정보가 올라간다.
//WebEnvironment.MOCK => 실제톰켓으로 올리는 것이 아니고, 다른 톰캣으로 테스트
//WebEnvironment.RANDOM_PORT => 실제 톰캣으로 테스트
//AutoConfigureMockMvc => MockMvc Ioc에 등록해줌
//@Transactional => 각각의 테스트 함수가 종료될 때마다 특랜잭션을 rollback해주는 어노테이션

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)   //실제톰켓으로 올리는 것이 아니고, 다른 톰캣으로 테스트
public class BookControllerIntegreTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach  //아래 테스트들이 실행되기전에 선제적으로 실행되서 id카운터를 1로 변경
	public void inti() {
		entityManager.createNativeQuery("alter table book alter column id restart with 1").executeUpdate();
	}
	
	@Test
	public void sava_테스트() throws Exception {
		
		Book book = new Book(null,"스프링 따라하기" ,"코스");
		//given(테스트하기위한 준비)
		String content = new ObjectMapper().writeValueAsString(book);
//		log.info("json : {}",content);
		
		//when(bookService.저장하기(book)
//		when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기" ,"코스"));
		
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
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기2", "코스"));
//		when(bookService.모두가져오기()).thenReturn(books);
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id=2L;

		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기2", "코스"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions actions = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("리액트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception{
		//given
		Long id=1L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기2", "코스"));
		bookRepository.saveAll(books);
		
		Book book = new Book(null,"Vue 따라하기" ,"코스");
		String content = new ObjectMapper().writeValueAsString(book);
		
		
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
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기2", "코스"));
		bookRepository.saveAll(books);
		
		
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
