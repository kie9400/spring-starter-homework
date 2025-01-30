package com.springboot.member.controller;

import com.springboot.member.dto.MemberPatchDto;
import com.springboot.member.dto.MemberPostDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import com.springboot.response.MultiResponseDto;
import com.springboot.response.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v2/members")
@Validated
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    //서비스 계층과 데이터 엑세스계층 생성자로 DI주입
    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberPostDto memberPostDto){
        //요청(requestBody)를 받은 Dto를 mapper를 통해 엔티티로 변환
        //이후 서비스 계층을 통해 멤버를 생성
        Member member = memberService.createMember(mapper.memberPostDtoToMember(memberPostDto));
        return new ResponseEntity<>(mapper.memberToMemberResponseDto(member), HttpStatus.CREATED);
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberPatchDto memberPatchDto){
        memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchDtoToMember(memberPatchDto));

        return new ResponseEntity<>(new SingleResponseDto<>(member),HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member member = memberService.findMember(memberId);
        return new ResponseEntity(new SingleResponseDto<>(mapper.memberToMemberResponseDto(member)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page, @Positive @RequestParam int size){
        //요청으로 받아오는 page는 1이지만, 페이지네이션은 페이지 번호가 0부터 시작[인덱스]
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        //페이지에서 content(실제 데이터)만 받아서 List형태로 변환
        List<Member> members = pageMembers.getContent();

        return new ResponseEntity(new MultiResponseDto<>
                (mapper.membersToMemberResponseDtos(members), pageMembers), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
