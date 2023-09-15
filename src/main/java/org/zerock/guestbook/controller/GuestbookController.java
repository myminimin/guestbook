package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")   // http://localhost/questbook/*
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 어노테이션
public class GuestbookController {

    private final GuestbookService service; // 꼭 final로 선언

    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list..........." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }   // Get 방식에서는 화면을 보여주고

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..." + dto);

        // 새로 추가된 엔티티의 번호
        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg", gno);
        // addFlashAttribute()는 단 한 번만 데이터(msg)를 전달하는 용도

        return "redirect:/guestbook/list";
    }   // Post 방식에서는 처리 후에 목록 페이지로 이동하도록 설계

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        // 나중에 다시 목록 페이지로 돌아가는 데이터를 같이 저장하기 위해서 PageRequestDTO를 파라미터로 같이 사용함

        log.info("gno: " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
        // Model에 GuestbookDTO 객체를 담아서 전달

    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,  // 수정해야하는 글의 정보를 가짐
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,   // 기존의 페이지 정보를 유지하기 위함
                         RedirectAttributes redirectAttributes) {   // 리다이렉트로 이동하기 위함

        log.info("post modify...................................");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());
        // modify->read로 넘어갈 때 page, gno와 검색해서 들어왔을 경우 type, keyword까지 수정한 글의 read 페이지로 넘어가게끔 한다.

        return "redirect:/guestbook/read";
        // 수정 작업이 진행된 이후에는 조회 페이지로 이동
    }
    
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        
        log.info("gno: " + gno);
        
        service.remove(gno);
        
        redirectAttributes.addFlashAttribute("msg", gno);
        // 몇 번 게시글을 삭제했습니다. modal창으로 보여주기 위함
        
        return "redirect:/guestbook/list";
        // 삭제 후 다시 목록의 첫 페이지로 이동
    }
}
