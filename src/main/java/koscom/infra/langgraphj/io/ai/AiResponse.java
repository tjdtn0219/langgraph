package koscom.infra.langgraphj.io.ai;

public final class AiResponse {
    private final String text;

    public AiResponse(String text) { this.text = text; }
    public String text() { return text; }
}
