package koscom.infra.langgraphj.io.ai;

public final class GptClient implements AiClient {

    @Override
    public AiResponse request(AiRequest req) {
        String p = req.prompt();

        // 실제 구현

        // (예시) 1단계 프롬프트면 "장성수" 반환
        if (p.contains("가장 최근의 일정을 뽑아줘") && p.contains("이름으로 해당하는 것을 추출")) {
            return new AiResponse("장성수");
        }

        // (예시) 마지막 단계 프롬프트면 요약 문장 반환
        if (p.startsWith("PROMPT:") && p.contains("QUERY_RESULT:")) {
            return new AiResponse("최종 응답(더미): " + p);
        }

        return new AiResponse("UNKNOWN");
    }
}
