package yujin.yujinspring.repository;

import org.junit.jupiter.api.AfterEach;
import yujin.yujinspring.domain.Member;

import java.util.*;

// MemberRepository interface의 구현체
public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public Member save(Member member) {
        member.setId(++sequence);   // id를 세팅해준 후, map에 추가
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));  // null이 반환될 가능성이 있으므로 Optional로 감싸서 반환함
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); // 하나라도 찾으면 반환. 알아서 Optional로 반환해줘서 안감싸도 됨.
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
