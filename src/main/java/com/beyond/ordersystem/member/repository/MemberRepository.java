package com.beyond.ordersystem.member.repository;


import com.beyond.ordersystem.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


//SpringDataJpa를 사용하기 위해서는 JpaRepository인터페이스를 상속해야하고, 상속시에 Entity명과 pk타입을 제네릭에 설정
//JpaRepository를 상속함으로서 JpaRepository의 주요기능(각종CRUD)상속
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
//    save, findById, findAll, delete 등은 사전에 구현

//    그외에 다른컬럼으로 조회할때에는 findBy+컬럼명 형식으로 선언하면 실행시점 자동구현.
    Optional<Member> findByEmail(String email);
    List<Member> findAllByName(String name);
}
