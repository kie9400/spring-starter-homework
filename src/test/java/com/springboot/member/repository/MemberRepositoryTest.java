package com.springboot.member.repository;

import com.springboot.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveMemberTest() {
        // given
        Member member = new Member();
        member.setEmail("shg123@naver.com");
        member.setName("송호근");
        member.setPhone("010-1234-5678");
        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);

        // when
        Member savedMember = (Member) memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        // then
        assertTrue(findMember.isPresent());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getPhone(), savedMember.getPhone());
    }
}