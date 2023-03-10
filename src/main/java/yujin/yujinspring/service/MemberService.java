package yujin.yujinspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
//@Service -> 자동으로 컴포넌트 스캔하는 방법 사용할 때
@Transactional // jpa 사용해서 db 연동할 때 추가함. jpa는 모든 데이타가 변경이 될 때 transaction 안에서 실행이 되어야 함.
public class MemberService {

    /*
    * MemberRepository를 새로 생성하지 않고, 외부에서 받아서 쓰도록 수정함.
    * 이런 것을 DI -> 의존성 주입 이라고 한다.
    * */
    private final MemberRepository memberRepository;
    // DI의 3가지 방법 중, 필드 주입 방 -> 별로 좋지 않음.
    // @Autowired private MemberRepository memberRepository;

    // DI의 3가지 방법 중, 생성자 주입 방식 -> 생성자 주입 방식을 이용하는 것이 가장 좋음.
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // DI의 3가지 방법 중, setter 주입 방식 -> 단점 : public하게 노출됨.
    // 이거 할 때는 memberRepository를 선언할 때 final 빼야 함.
    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
     */

    /*회원 가입*/
    public Long join(Member member) {

        /*회원 가입 시간 추적 코드 추가함 --> 아래와 같은 코드를 10000개가 넘는 메소드에 넣으려면 ?  비효율적.
        * 따라서 AOP 방식이 필요함. -> "공통관심사항"에 AOP 적용!
        * 공통관심사항  vs  핵심관심사 분리
        * */

        long start  = System.currentTimeMillis();

        try {
            validateDuplicateMember(member); // 중복 회원 검증
            memberRepository.save(member);  // 추가
            return member.getId();
        } finally {
            // logic이 끝난 후의 시간을 찍기 위해 finally 사용
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs + "ms");
        }

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
