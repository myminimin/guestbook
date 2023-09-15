package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    /* 화면에서 전달되는 page라는 파라미터와 size라는 파라미터를 수집하는 역할을 한다.
       JPA쪽에서 사용하는 Pageable 타입의 객체를 생성하는 것이 진짜 목적.
    */

    // 페이징용
    private int page;
    private int size;
    
    // 검색용
    private String type;    // 검색 조건
    private String keyword; // 검색 키워드

    // 페이지 번호 등은 기본값을 가지는 것이 좋기 때문에 1과 10이라는 값을 이용한다
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    /* JPA를 이용하는 경우에는 페이지 번호가 0부터 시작한다는 점을 감안해서
       1페이지의 경우 0이 될 수 있도록 page-1을 하는 형태로 작성,
       정렬은 나중에 다양한 상황에서 쓰기 위해서 별도의 파라미터로 받도록 설계함
     */
    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1, size, sort);
    }
}
