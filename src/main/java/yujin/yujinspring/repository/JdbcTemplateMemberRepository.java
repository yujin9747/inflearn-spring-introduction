package yujin.yujinspring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import yujin.yujinspring.domain.Member;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 실제로 실무에서도 많이 쓰임
 * SQL문은 직접 작성. JDBC에서의 반복적인 코드를 많이 제거해 줌.
 * **/
//@Repository // @Autowired에 에러가 떠서 넣기는 했는데,, 강의에서는 넣지 않아도 잘 되었음.
public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;


    // 따로 injection을 받을 수 있는 것은 아니므로 Constructor로 받아와야 함.
    // tip : 생성자가 하나 뿐일 때는 Autowired 생략 가능
    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public Member save(Member member) {
        // SimpleJdbcInsert 를 이용해서 쿼리문을 짜지도 않고 save 할 수 있음.
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        // db 생성 시 자동 생성된 Key를 받아와서 member에 set 해줌.
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }


    // 순수 Jdbc 방식으로 했을 때와 비교하면 코드의 라인이 엄청 줄어든 것을 확인할 수 있음.
    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        // Optional로 반환하기 위해 findAny()사용
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    // 쿼리 결과를 리턴하기 위한 메소드
    private RowMapper<Member> memberRowMapper() {
        // rs: 쿼리 결과가 담긴 ResultSet
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
