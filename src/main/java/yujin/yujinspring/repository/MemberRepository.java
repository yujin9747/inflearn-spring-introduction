package yujin.yujinspring.repository;
import yujin.yujinspring.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    // Optional : null일 경우, null을 그대로 반환하지 않고 Optional로 감싸서 반환함.
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
