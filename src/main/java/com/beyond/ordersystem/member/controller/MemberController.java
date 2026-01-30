package com.beyond.ordersystem.member.controller;

import com.beyond.ordersystem.member.dtos.*;
import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.service.MemberService;
import com.beyond.ordersystem.common.auth.JwtTokenProvider;
import com.beyond.ordersystem.common.dtos.CommonErrorDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestPart("member") @Valid MemberCreateDto dto,
                                    @RequestPart("profileImage") MultipartFile profileImage){


        memberService.save(dto, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberListDto> findAll(){
        List<MemberListDto> dtoList = memberService.findAll();
        return dtoList;
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            MemberDetailDto dto = memberService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            CommonErrorDto dto = CommonErrorDto.builder()
                    .status_code(404)
                    .error_message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal){
        System.out.println(principal);
        MemberDetailDto dto = memberService.myinfo();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }



    @PostMapping("/doLogin")
    public String login(@RequestBody MemberLoginDto dto){
        Member member = memberService.login(dto);
        String token = jwtTokenProvider.createToken(member);
        return token;
    }

}
