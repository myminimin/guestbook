package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor    // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    /* JPA 처리를 위해서 GuestbookRepository를 주입하고 클래스 선언 시에
       @RequiredArgsConstructor를 이용해서 자동으로 주입한다.*/
    private final GuestbookRepository repository; // 반드시 final로 선언


    /* 서비스 계층에서는 파라미터를 DTO 타입으로 받기 때문에 JPA로 처리하기 위해서는
       엔티티 타입의 객체로 변환하는 작업이 반드시 필요하다! */
    
    // dtoToEntity()를 이용해서 파라미터로 전달되는 dto를 entity 타입으로 변환한다.
    @Override
    public Long register(GuestbookDTO dto) {    // 파라미터로 받은 DTO 타입의 dto를

        log.info("DTO----------------------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);
        // entity 타입으로 변경하는 작업

        log.info(entity);

        repository.save(entity);
        // save()를 통해서 저장하고

        return entity.getGno();
        // 해당 엔티티가 가지는 gno 값을 반환
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        Page<Guestbook> result = repository.findAll(pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        // entityToDTO()를 이용해서 java.util.Function을 생성하고 이를 PageResultDTO로 구성

        return new PageResultDTO<>(result, fn);
        // PageResultDTO에는 JPA의 처리 결과인 Page<Entity> = result 와
        // Function = fn 을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고,
        // 화면에 페이지 처리와 필요한 값들을 생성한다.
    }


}
