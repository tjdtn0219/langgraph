package koscom.infra.langgraphj.runtime;

import koscom.infra.langgraphj.io.ai.AiClient;
import koscom.infra.langgraphj.io.db.DbClient;

public final class ExecutionContext {
    private final AiClient aiClient;
    private final DbClient dbClient;
    private final RuntimeHook hook;

    public ExecutionContext(AiClient aiClient, DbClient dbClient, RuntimeHook hook) {
        this.aiClient = aiClient;
        this.dbClient = dbClient;
        this.hook = hook;
    }

    public AiClient ai() { return aiClient; }
    public DbClient db() { return dbClient; }
    public RuntimeHook hook() { return hook; }
}
