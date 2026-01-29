package com.beyond.ordersystem.member.dtos;

import com.beyond.ordersystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberLoginDto {
    private String email;
    private String password;

    public MemberLoginDto fromEntity(Member member){
        return MemberLoginDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
