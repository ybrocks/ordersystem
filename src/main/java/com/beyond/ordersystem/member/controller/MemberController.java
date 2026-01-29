package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.common.auth.JwtTokenProvider;
import com.beyond.ordersystem.member.dtos.MemberCreateDto;
import com.beyond.ordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
//    회원가입
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestPart("member") @Valid MemberCreateDto dto,
                                   @RequestPart("profileImage")MultipartFile profileImage){
        memberService.save(dto, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
//    회원목록조회
    @GetMapping("/list")

}
