package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1).size(10).build();
        // PageRequestDTO를 이용하기 때문에 생성할 때는 1페이지부터 처리할 수 있다.
        // 정렬은 상황에 맞게 Sort 객체를 생성해서 전달하는 형태로 사용

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);
        // GuestbookServiceImpl에서 Page<Guestbook>했던 것이 List<GeustBookDTO>로
        // 정상적으로 변환되어서 출력 결과에 GuestbookDTO 타입으로 출력된다.

        System.out.println("PREV: " +resultDTO.isPrev());
        // page(1)이므로 이전으로 가는 링크는 필요 없어 'false'로 출력
        System.out.println("NEXT: " +resultDTO.isNext());
        // 다음 페이지로 가는 링크 필요해서 'true'로 출력
        System.out.println("TOTAL: " +resultDTO.getTotalPage());
        // 현재 실습까지 데이터가 302개 있어서 전체 페이지의 개수는 '31'로 나옴

        System.out.println("-------------------------------------------");
        for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("-------------------------------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}
