package koscom.infra.langgraphj.core.node;

import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.io.ai.AiRequest;
import koscom.infra.langgraphj.io.ai.AiResponse;
import koscom.infra.langgraphj.runtime.ExecutionContext;

import java.util.Objects;
import java.util.function.Function;

public final class AiCallNode implements Node {
    private final String id;
    private final Function<State, String> promptBuilder; // State 기반으로 prompt 생성
    private final String promptStateKey;                 // 생성된 prompt 저장 키
    private final String outputStateKey;                 // AI 응답 저장 키

    public AiCallNode(String id,
                      Function<State, String> promptBuilder,
                      String promptStateKey,
                      String outputStateKey) {
        this.id = Objects.requireNonNull(id);
        this.promptBuilder = Objects.requireNonNull(promptBuilder);
        this.promptStateKey = Objects.requireNonNull(promptStateKey);
        this.outputStateKey = Objects.requireNonNull(outputStateKey);
    }

    @Override public String id() { return id; }

    @Override
    public NodeResult execute(State state, ExecutionContext ctx) throws Exception {
        String prompt = promptBuilder.apply(state);
        state.put(promptStateKey, prompt);

        // ✅ 실제 OpenAI 호출 구현은 AiClient 내부에서 추후 구현 예정
        //    (여기서는 ctx.ai().request(...)만 호출)
        AiResponse res = ctx.ai().request(new AiRequest(prompt));

        state.put(outputStateKey, res.text());
        return NodeResult.ok("ai-saved=" + outputStateKey);
    }
}