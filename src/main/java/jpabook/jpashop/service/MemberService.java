package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //전체 기본이 readOnly로 걸림. 나머지는 overwrite함.
//@AllArgsConstructor //버전3) 모든 필드로 생성자를 만들어주는 역할
@RequiredArgsConstructor //버전) final이 달려있는 필드만 가지고 생성자를 만들어줌.
public class MemberService {

    private final MemberRepository memberRepository; // final로 해놔야 생성자에 초기값이 할당안되면 에러내준다. 중요!

//    @Autowired //버전1) setter 주입을 해줘야, MOCk객체를 파라미터로 넣어서 바꾸면서 테스트 가능!
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired //버전2) 생성자 주입을 해야 객체를 바꿀 수가 없게 만들 수 있는 장점이 있음!
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //sign up
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //validation 멀티스레드가 동시에 실행하면 통과 가능. 안전장치로 DB에서 memberName을 UK로 걸어주는 게 좋음
        memberRepository.save(member); //em.persis 하는 순간 객체에 id 값을 채워줌.
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //get all users
//    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
