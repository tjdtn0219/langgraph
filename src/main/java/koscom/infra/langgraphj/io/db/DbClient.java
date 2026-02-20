package koscom.infra.langgraphj.io.db;

import java.util.List;
import java.util.Map;

public interface DbClient {
    List<Doc> executeQuery(String sql, List<Object> params) throws Exception;
}
