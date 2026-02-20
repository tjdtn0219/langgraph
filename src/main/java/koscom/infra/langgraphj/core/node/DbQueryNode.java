package koscom.infra.langgraphj.core.node;

import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.io.db.Doc;
import koscom.infra.langgraphj.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public final class DbQueryNode implements Node {

    private final String id;
    private final String sql;
    private final List<String> paramStateKeys; // 예: ["name"]
    private final String outputStateKey;       // 예: "queryResult"

    public DbQueryNode(String id, String sql, List<String> paramStateKeys, String outputStateKey) {
        this.id = Objects.requireNonNull(id);
        this.sql = Objects.requireNonNull(sql);
        this.paramStateKeys = Objects.requireNonNull(paramStateKeys);
        this.outputStateKey = Objects.requireNonNull(outputStateKey);
    }

    @Override public String id() { return id; }

    @Override
    public NodeResult execute(State state, ExecutionContext ctx) throws Exception {
        List<Object> params = new ArrayList<>();
        for (String key : paramStateKeys) {
            params.add(state.get(key)); // Main에서 미리 state 세팅된 값을 기준으로 사용
        }

        // ✅ 실제 JDBC Connection / PreparedStatement 로직은 DbClient 내부에서 추후 구현 예정
        List<Doc> rows = ctx.db().executeQuery(sql, params);

        if (rows != null && !rows.isEmpty()) {
            // 여기서는 단순히 rows.toString()으로 저장(실제론 JSON 직렬화 등을 붙일 수 있음)
            state.put(outputStateKey, rows.toString());
        } else {
            state.put(outputStateKey, "NONE");
        }

        return NodeResult.ok("db-saved=" + outputStateKey);
    }
}