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
public class MemberDetailDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    public static MemberDetailDto fromEntity(Member member){
        return MemberDetailDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
