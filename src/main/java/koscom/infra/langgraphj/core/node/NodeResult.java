package koscom.infra.langgraphj.core.node;

public final class NodeResult {
    private final Status status;
    private final String message;

    public enum Status { OK, ERROR }

    private NodeResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static NodeResult ok() { return new NodeResult(Status.OK, null); }
    public static NodeResult ok(String message) { return new NodeResult(Status.OK, message); }
    public static NodeResult error(String message) { return new NodeResult(Status.ERROR, message); }

    public Status status() { return status; }
    public String message() { return message; }
}
