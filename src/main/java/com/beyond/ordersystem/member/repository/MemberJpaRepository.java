package com.beyond.ordersystem.member.repository;

import com.beyond.ordersystem.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sound.midi.MetaMessage;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    private final EntityManager entityManager;

    @Autowired
    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//    회원가입
    public void save(Member member){
        entityManager.persist(member);
    }

//    회원목록조회
    public List<Member> findAll(){
        List<Member> memberList = entityManager.createQuery("select a from Member a", Member.class).getResultList();
        return memberList;
    }

//    회원상세조회
    public Optional<Member> findById(Long id){
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

//    email 조회
    public Optional<Member> findByEmail(String email){
        List<Member> memberList = entityManager.createQuery("select a from Member a where a.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
        Member member = null;
        if (memberList.size()!=0){
            member = memberList.get(0);
        }
        return Optional.ofNullable(member);
    }
}
