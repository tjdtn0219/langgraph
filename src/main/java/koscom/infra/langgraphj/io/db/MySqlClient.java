package koscom.infra.langgraphj.io.db;


import java.util.List;
import java.util.Map;

public final class MySqlClient implements DbClient {

    // 의존성 주입받는 쪽에서 따로 구현체 구현해서 사용하는 방법도 가능
    // 즉 DBClient를 상속받아 구현

    @Override
    public int executeUpdate(String sql, List<Object> params) {
        // TODO: JDBC 구현 예정
        return 0;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, List<Object> params) {
        // TODO: JDBC 구현 예정
        // (예시) name이 "장성수"면 결과 1개를 반환하는 것처럼 시뮬레이션
        if (params != null && !params.isEmpty() && "장성수".equals(params.get(0))) {
            return List.of(Map.of("NAME", "장성수", "SCHEDULE", "2026-02-19 10:00 회의"));
        }
        return List.of();
    }
}
