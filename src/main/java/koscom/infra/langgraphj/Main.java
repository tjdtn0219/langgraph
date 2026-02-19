package koscom.infra.langgraphj;

import koscom.infra.langgraphj.core.edge.Edge;
import koscom.infra.langgraphj.core.engine.ExecutionReport;
import koscom.infra.langgraphj.core.engine.GraphEngine;
import koscom.infra.langgraphj.core.graph.LangGraph;
import koscom.infra.langgraphj.core.node.AiCallNode;
import koscom.infra.langgraphj.core.node.ConditionRoutingNode;
import koscom.infra.langgraphj.core.node.DbQueryNode;
import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.diagram.ConsoleDiagramPrinter;
import koscom.infra.langgraphj.io.ai.AiClient;
import koscom.infra.langgraphj.io.ai.GptClient;
import koscom.infra.langgraphj.io.db.DbClient;
import koscom.infra.langgraphj.io.db.MySqlClient;
import koscom.infra.langgraphj.runtime.DebugPrintHook;
import koscom.infra.langgraphj.runtime.ExecutionContext;

import java.util.List;

public class Main {
    // State Key들을 “미리 결정” (요구사항)
    private static final String K_PROMPT = "prompt";
    private static final String K_NAME_CANDIDATE = "nameCandidate";
    private static final String K_NAME = "name";
    private static final String K_ROUTE_VALID = "route.validName";
    private static final String K_QUERY_RESULT = "queryResult";
    private static final String K_FINAL_PROMPT = "finalPrompt";
    private static final String K_FINAL_AI = "finalAiResponse";

    public static void main(String[] args) throws Exception {

        // ✅ AiClient: 실제 OpenAI 호출은 추후 구현 예정
        AiClient aiClient = new GptClient();

        // ✅ DbClient: 실제 JDBC 연결/쿼리 실행은 추후 구현 예정
        DbClient dbClient = new MySqlClient();

        ExecutionContext ctx = new ExecutionContext(aiClient, dbClient, new DebugPrintHook());

        // ✅ 초기 State를 “미리 결정하고 시작” (요구사항)
        State state = new State();
        state.put(K_PROMPT, "");          // 실행 중 AiCallNode가 채울 예정
        state.put(K_NAME_CANDIDATE, "");  // 1단계 AI 응답 저장
        state.put(K_NAME, "");            // 조건 만족 시 확정 이름 저장
        state.put(K_ROUTE_VALID, "");     // Y/N
        state.put(K_QUERY_RESULT, "");    // DB 조회 결과 or NONE
        state.put(K_FINAL_PROMPT, "");    // 최종 AI 프롬프트
        state.put(K_FINAL_AI, "");        // 최종 AI 응답

        // ---- Node 1) AI: 이름 추출 프롬프트 생성 -> prompt 저장 -> nameCandidate 저장
        AiCallNode aiExtract = new AiCallNode(
                "ai_extract",
                s -> "장성수 사원의 가장 최근의 일정을 뽑아줘. 라는 문장에서 이름으로 해당하는 것을 추출해줘.",
                K_PROMPT,
                K_NAME_CANDIDATE
        );

        // ---- Node 2) Condition: nameCandidate 길이==3 이면 name 확정 + route.validName=Y, 아니면 N
        ConditionRoutingNode condLen = new ConditionRoutingNode(
                "cond_len",
                s -> {
                    String cand = s.get(K_NAME_CANDIDATE);
                    return cand != null && cand.length() == 3;
                },
                K_ROUTE_VALID, "Y", "N",
                // onTrue: name에 후보값 등록 (요구사항 Step3)
                s -> s.put(K_NAME, s.get(K_NAME_CANDIDATE)),
                // onFalse: 필요하면 초기화/카운트 증가 등 가능
                null
        );

        // ---- Node 3) DB Query: SELECT ... WHERE NAME = state.get(name)
        DbQueryNode dbQuery = new DbQueryNode(
                "db_query",
                "SELECT * FROM SCHEDULE WHERE NAME = ?",
                List.of(K_NAME),
                K_QUERY_RESULT
        );

        // ---- Node 4) AI Final: queryResult + prompt 합쳐서 다시 AI 호출
        AiCallNode aiFinal = new AiCallNode(
                "ai_final",
                s -> {
                    String finalPrompt = "PROMPT: " + s.get(K_PROMPT) + "\n" +
                            "QUERY_RESULT: " + s.get(K_QUERY_RESULT);
                    s.put(K_FINAL_PROMPT, finalPrompt); // “합친 프롬프트”도 state에 보관
                    return finalPrompt;
                },
                K_FINAL_PROMPT, // prompt를 여기에도 저장(덮어쓰기)
                K_FINAL_AI       // 최종 AI 응답
        );

        // ---- Graph 구성
        LangGraph graph = new LangGraph()
                .addNode(aiExtract)
                .addNode(condLen)
                .addNode(dbQuery)
                .addNode(aiFinal)
                .setEntry("ai_extract")
                // ai_extract -> cond_len
                .addEdge(new Edge("ai_extract", "cond_len", s -> true))
                // cond_len -> db_query (Y)
                .addEdge(new Edge("cond_len", "db_query", s -> "Y".equals(s.get(K_ROUTE_VALID))))
                // cond_len -> ai_extract (N이면 1단계 재실행)
                .addEdge(new Edge("cond_len", "ai_extract", s -> "N".equals(s.get(K_ROUTE_VALID))))
                // db_query -> ai_final
                .addEdge(new Edge("db_query", "ai_final", s -> true))
                // ai_final -> END
                .addEdge(new Edge("ai_final", LangGraph.END, s -> true));

        // (옵션) 다이어그램 출력
        System.out.println(new ConsoleDiagramPrinter().print(graph));

        // 실행
        GraphEngine engine = new GraphEngine(50);
        ExecutionReport report = engine.run(graph, state, ctx);

        System.out.println("Final State: " + report.finalState());
        System.out.println("Steps: " + report.steps().size());
    }
}