package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("title..." +i)
                    .content("Content..." +i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);
        // 꼭 존재하는 번호로 테스트를 해야한다

        if(result.isPresent()){

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title.....");
            guestbook.changeContent("Changed Content.......");

            guestbookRepository.save(guestbook);
            // BaseEntity의 modDate는 최종 수정 시간이 반영되기 때문에 특정한 엔티티를 수정한 후 save()해야 동작한다!
        }
    }


    /* 단일 항목 검색 테스트 : 제목에 '1'이라는 글자가 있는 엔티티들을 검색 */
    @Test
    public void testQuery1() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;
        // Q도메인 클래스를 이용해 엔티티 클래스에 선언된 title, content 같은 필드들을 변수로 활용

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();
        // BooleanBuilder 는 where문에 들어가는 조건들을 넣어주는 컨테이너라고 간주하면 된다.

        BooleanExpression expression = qGuestbook.title.contains(keyword);
        // 원하는 조건은 필드 값과 같이 결합해서 생성.

        builder.and(expression);
        // 만들어진 조건은 where문에 and나 or같은 키워드와 결합시킨다.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        // BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있다.
        //

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    /* 다중 항목 검색 : 제목 혹은 내용에 특정한 키워드가 있고, gno가 0보다 크다 */
    @Test
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);
        // 1. exTitle과 exContent을 결합

        builder.and(exAll);
        // 2. 1을 builder.and(exAll)에 추가하고

        builder.and(qGuestbook.gno.gt(0L));
        // 3. 1,2 이후에 'gno가 0보다 크다'라는 조건을 추가

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    /* 다중 항목 검색 : '제목 + 내용 + 작성자' 만들어 보기! */
    @Test
    public void testQuery3() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exWriter = qGuestbook.writer.contains(keyword); // 작성자 추가

        BooleanExpression exAll = exTitle.or(exContent).or(exWriter);
        // 1. exTitle과 exContent, exWriter까지 결합

        builder.and(exAll);
        // 2. 1을 builder.and(exAll)에 추가하고

        builder.and(qGuestbook.gno.gt(0L));
        // 3. 1,2 이후에 'gno가 0보다 크다'라는 조건을 추가

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
