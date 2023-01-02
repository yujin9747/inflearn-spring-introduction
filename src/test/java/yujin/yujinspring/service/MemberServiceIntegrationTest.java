package yujin.yujinspring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import yujin.yujinspring.domain.Member;
import yujin.yujinspring.repository.MemberRepository;
import yujin.yujinspring.repository.MemoryMemberRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**순수히 Java 코드만 가지고 메모리 기반으로 테스트한 MemberServiceTest와 달리
 * Spring이 가지고 있는 DataSource와 통합해서 Test하는 것.(통합테스트)
 * Java 코드만 가지고 하는 테스트(단위테스트)는 왜 필요한가?
 * -> 실행 속도가 빠름. 테스트 케이스가 많을 때 유용함. 사실 단위테스트를 잘 할 줄 아는 것이 중요함.
 * **/

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    // 다른 코드라면 dependency injection해서 쓰면 좋지만, 테스트기 때문에 @Autowired로 해도 상관 없고, 더 편함.
    @Autowired  MemberService memberService;
    @Autowired MemberRepository memberRepository;

    /** 이제는 리포지토리 instance를 직접 생성하는 것이 아니라, Spring으로 부터 받아와서 테스트 해야 함. -> @SpringBootTest
//    @BeforeEach
//    public void beforeEach() {
//        memberRepository = new MemoryMemberRepository();
//        memberService = new MemberService(memberRepository);
//    }**/

    /** 필요 없음 -> @Transactional 때문에 없어도 됨!!
     * DB의 transaction 개념 참고하기
     * DB에 커밋?이 되어야 반영이 되는건데
     * @Transactional을 달면 테스트가 끝나면, roll-back을 해서 DB에 반영을 안 함.
     * 따라서 반복 테스트 가능
//    @AfterEach
//    public void afterEach() {
//        memberRepository.clearStore();
//    }**/

    @Test
    // @Commit -> 실제로 디비에 반영해서 확인하고 싶으면 Commit annotation 추가해주면 됨
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("spring1");

        // when
        Long id = memberService.join(member);

        // then
        Member result = memberService.findOne(id).get();
        Assertions.assertEquals(result.getName(), member.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("spring2");

        Member member2 = new Member();
        member2.setName("spring2");

        // when
        Long id1 = memberService.join(member1);
        // memberService.join()을 했을 때 IllegalStateException이 터져야 한다는 것을 assert 해주는 문장. 
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        // 반환값으로 받은 exception으로 exception message도 검증 가능
        Assertions.assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");


    }
}