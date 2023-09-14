package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
