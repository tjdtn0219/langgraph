package koscom.infra.langgraphj.io.ai;

public final class AiRequest {
    private final String prompt;

    public AiRequest(String prompt) { this.prompt = prompt; }
    public String prompt() { return prompt; }
}
