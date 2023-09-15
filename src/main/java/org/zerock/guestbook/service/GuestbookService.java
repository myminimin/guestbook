package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);
    // 방명록의 작성 처리

    GuestbookDTO read(Long gno);
    // 방명록의 조회 처리 (리턴 타입은 GuestbookDTO, 파라미터는 Long타입의 gno)

    void remove(Long gno);
    // 방명록의 삭제 처리

    void modify(GuestbookDTO dto);
    // 방명록의 수정 처리

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);
    // PageRequestDTO를 파라미터로, PageResultDTO를 리턴 타입으로 사용



    /* Java 8 버전부터는 default라는 키워르를 이용하면 '인터페이스->추상 클래스->구현 클래스'의
    형태로 구현되던 방식에서 추상 클래스를 생략하는 것이 가능해진다.*/
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    } // default 기능을 활용해서 구현클래스에서 동작할 수 있는 dtoToEntity()를 구성
   

    default GuestbookDTO entityToDto(Guestbook entity) {

        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    } // 엔티티 객체를 DTO 객체로 변환하는 entityToDto()

}
