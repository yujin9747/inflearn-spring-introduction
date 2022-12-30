package yujin.yujinspring.repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import yujin.yujinspring.domain.Member;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
// Test class를 먼저 만든 후, 구현 클리스를 만들어 테스트 하는 방식을 "Test Driven Development"라고 함
// MemoryMemberRepository에서 구현한 함수들을 테스트 하는 클래스
class MemoryMemberRepositoryTest {  // class level에서 한번에 여러개의 테스트를 실행할 수도 있음.
    MemoryMemberRepository repository = new MemoryMemberRepository();

    // test가 끝날 때마다 repository를 비워주기 위한 call-back 메소드
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    @Test
    public void save(){
        Member member = new Member();
        member.setName("yujin");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();  // Optional에서 값을 빼오기 위해 get 사용
        Assertions.assertEquals(member, result);    // 같을 경우, 문제 없을 경우 아무 메세지도 나오지 않음.
        // Assertions.assertEquals(member, null);  다를 경우 나오는 메세지 확인해보
        // 이외에도 다양한 Assertions 가 있으니 사용해보기
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        Assertions.assertEquals(member1, result);

    }

    @Test
    public void findAll() {
        // member1이 findByName에서도 생성되었음.
        // 따라서 테스트 하나가 끝날 때마다 데이터를 clean하는 작업도 필요함. -> afterEach
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        Assertions.assertEquals(2 , result.size());
    }
}
