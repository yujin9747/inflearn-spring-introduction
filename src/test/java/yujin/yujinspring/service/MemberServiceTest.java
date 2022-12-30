package yujin.yujinspring.service;

import org.junit.jupiter.api.*;
import yujin.yujinspring.domain.Member;
import yujin.yujinspring.repository.MemoryMemberRepository;

import static org.junit.jupiter.api.Assertions.*;
// tip : test 코드는 과감하게 메소드 이름을 한글로 해도 상관 없음. 영어권 사람이랑 일하는거 아니면.
// test 코드는 빌드될 때 포함되지 않음.
// given, when, then 주석을 사용하면 좋음 --> 테스트는 대부분 이 구조로 이루어 지기 때문에 편함.
// 여기서도 데이터를 하나의 테스트가 끝날 때마다 지워줘야 함.
class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    /*문제점 : repository test 에서 생성한 MemoryMemberRepository와 instance를 따로 생성함.
      현재는 map이 static으로 되어 있어서 문제는 없지만 다음과 같이 바꿔주는 것이 좋음
      MemberService에서 MemoryRepository의 instance를 생성하지 말고, 외부에서 넣어주도록 수정
      BeforeEach를 통해 테스트 동작하기 전에 넣어주도록 설정.
      */

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
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
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        Long id1 = memberService.join(member1);
        // memberService.join()을 했을 때 IllegalStateException이 터져야 한다는 것을 assert 해주는 문장. 
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        // 반환값으로 받은 exception으로 exception message도 검증 가능
        Assertions.assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");

        /* try-catch 이용하는 방법
        try{
            Long id2 = memberService.join(member2);
            fail(); // exception이 발생해야 하는데, 발생하지 않았으니 fail()이라고 표시해주는 것.
        } catch (IllegalStateException e) {
            Assertions.assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");  // 에러 메세지가 제대로 생성되었는지 확인
        }
        */


    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}