package koscom.infra.langgraphj.io.db;

import java.util.List;
import java.util.Map;

public interface DbClient {
    int executeUpdate(String sql, List<Object> params) throws Exception;
    List<Map<String, Object>> executeQuery(String sql, List<Object> params) throws Exception;

    // DDL도 결국 update로 통일 가능(RETURN -1 등)
    default int executeDDL(String sql) throws Exception {
        return executeUpdate(sql, List.of());
    }
}
