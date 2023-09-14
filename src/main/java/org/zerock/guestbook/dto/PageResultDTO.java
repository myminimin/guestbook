package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    // 다양한 곳에서 사용할 수 있도록 제너릭 타입을 이용해서 DTO와 EN이라는 타입을 지정

    private List<DTO> dtoList; // DTO 리스트

    private int totalPage;  // 총 페이지 번호
    private int page;       // 현재 페이지 번호
    private int size;       // 목록 사이즈
    private int start, end; // 시작 페이지 번호, 끝 페이지 번호
    private boolean prev, next; // 이전, 다음
    private List<Integer> pageList; // 페이지 번호 목록

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        /* Function<EN, DTO>는 엔티티 객체들을 DTO로 변환해 주는 기능이다.
           위와 같은 구조를 이용하면 나중에 어떤 종류의 Page<E> 타입이 생성되더라도,
           PageResultDTO를 이용해서 처리할 수 있다는 장점이 있다. */

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());

    } // Page<Entity> 타입을 이용해서 생성할 수 있는 생성자

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;   // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
