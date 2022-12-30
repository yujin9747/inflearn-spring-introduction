package yujin.yujinspring.service;

import yujin.yujinspring.domain.Member;
import yujin.yujinspring.repository.MemberRepository;
import yujin.yujinspring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

/*
* repository를 이용해 비즈니스 로직을 작성
* 예를 들어, 회원가입 시 같은 이름의 회원은 가입하지 못하도록 중복 체크
* 서비스 메소드는 더 비즈니스와 관련된 용어들을 사용해서 naming하는 것을 추천함.
* command+shift+t 를 통해 create new test 를 할 수 있음.
* */
public class MemberService {

    /*
    * MemberRepository를 새로 생성하지 않고, 외부에서 받아서 쓰도록 수정함.
    * 이런 것을 DI -> 의존성 주입 이라고 한다.
    * */
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*회원 가입*/
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);  // 추가
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 같은 이름 회원 X
        // Optional이니 ifPresent 확인 해줘야 함
        // 그냥 꺼내서 확인하고 싶으면 get() 하면됨. -> 권장 X
        // ctl+t를 통해 extract method 할 수 있음.
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /*
    * 전체 회원 조회
    * */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /*
    * id로 회원 찾기
    * */
    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }


}
