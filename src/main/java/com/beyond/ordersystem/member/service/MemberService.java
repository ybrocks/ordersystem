package com.beyond.ordersystem.member.service;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dtos.*;
import com.beyond.ordersystem.member.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket1}")
    private String bucket;

//    생성자가 하나밖에 없을떄에는 Autowired생략가능
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, S3Client s3Client){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }
    public void save(MemberCreateDto dto, MultipartFile profileImage){
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(member);

    }

    @Transactional(readOnly = true)
    public MemberDetailDto findById(Long id){
        Optional<Member> optMember = memberRepository.findById(id);
        Member member = optMember.orElseThrow(()-> new NoSuchElementException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }

    @Transactional(readOnly = true)
    public MemberDetailDto myinfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Member> optMember = memberRepository.findByEmail(email);
        Member member = optMember.orElseThrow(()-> new NoSuchElementException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }
    @Transactional(readOnly = true)
    public List<MemberListDto> findAll(){
        return memberRepository.findAll().stream().map(a-> MemberListDto.fromEntity(a))
                .collect(Collectors.toList());
    }

    public Member login(MemberLoginDto dto){
        Optional<Member> opt_member = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(!opt_member.isPresent()){
            check=false;
        }else {
            if(!passwordEncoder.matches(dto.getPassword(), opt_member.get().getPassword())){
                check =false;
            }
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return opt_member.get();
    }
}
