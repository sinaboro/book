package com.cos.book.domain;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

/*단위 테스트 (DB관련된 Bean이 IOC에 등록되면됨)
 * Replace.ANY : 가찌 디비로 테스트 --단위테스트
 * Replace.NONE : 실제 디비로 테스트 - 통합테스트
 * @DataJpaTest  //BookRepository들을 다 IOC에 등록해줌
 */

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) //
@DataJpaTest  
public class BookRepositoryUnitTest {

}
