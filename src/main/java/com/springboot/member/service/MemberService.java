package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//트랜잭션 적용
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member){
        //중복 이메일 여부 확인
        verifyExistsEmail(member.getEmail());

        return  memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId());

        //이름, 번호, 상태만 변경하는하도록 설정
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findMember.setPhone(phone));
        Optional.ofNullable(member.getName()).ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));

        return memberRepository.save(findMember);
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size){
        return memberRepository.findAll(PageRequest.of(page, size, Sort.by("memberId")
                .descending()));
    }

    public void deleteMember(long memberId){
        Member findMember = findVerifiedMember(memberId);

        //db에서 삭제하는거 보다는 상태를 변경하는게 좋다
//        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
//        memberRepository.save(findMember);

        memberRepository.delete(findMember);
    }

    private void verifyExistsEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        //만약 멤버 객체가 값이 있다면(이메일이 존재한다면) true -> 예외를 던진다.
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    public Member findVerifiedMember(long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        //만약 예외가 발생했을 경우 비즈니스로직 예외를 던진다.
        Member findMemeber = member.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return findMemeber;
    }
}
