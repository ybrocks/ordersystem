package com.beyond.ordersystem.member.service;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.dtos.MemberCreateDto;
import com.beyond.ordersystem.member.dtos.MemberDetailDto;
import com.beyond.ordersystem.member.dtos.MemberListDto;
import com.beyond.ordersystem.member.dtos.MemberLoginDto;
import com.beyond.ordersystem.member.repository.MemberRepository;
import com.beyond.ordersystem.order.repository.OrderRepository;
import com.beyond.ordersystem.product.repository.ProductRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket1}")
    private String bucket;

    @Autowired
    public MemberService(MemberRepository memberRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    //    회원가입
    public void save(MemberCreateDto dto, MultipartFile profileImage) {

        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이메일 중복");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        Member memberDB = memberRepository.save(member);

        if (profileImage != null) {
            String fileName = "user-" + member.getId() + "-profileimage-" + profileImage.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(profileImage.getContentType())
                    .build();
            try {
                s3Client.putObject(request, RequestBody.fromBytes(profileImage.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
            member.updateProfileImageUrl(imgUrl);
        }
    }

    //    회원목록조회
    @Transactional(readOnly = true)
    public List<MemberListDto> findAll() {
        return memberRepository.findAll().stream()
                .map(a -> MemberListDto.fromEntity(a))
                .collect(Collectors.toList());
    }

    //    회원상세조회
    @Transactional(readOnly = true)
    public MemberDetailDto findById(Long id) {
        Optional<Member> optMember = memberRepository.findById(id);
        Member member = optMember.orElseThrow(() -> new NoSuchElementException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }

    //    내정보조회
    @Transactional(readOnly = true)
    public MemberDetailDto myInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Member> optMember = memberRepository.findByEmail(email);
        Member member = optMember.orElseThrow(() -> new NoSuchElementException("entity is not found"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }

    //    로그인
    public Member login(MemberLoginDto dto) {
        Optional<Member> optMember = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!optMember.isPresent()) {
            check = false;
        } else {
            if (!passwordEncoder.matches(dto.getPassword(), optMember.get().getPassword())) {
                check = false;
            }
        }
        if (!check) {
            throw new IllegalArgumentException("이메일 또는 비밀번호 불일치");
        }
        return optMember.get();
    }
}
