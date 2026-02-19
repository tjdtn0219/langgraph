package koscom.infra.langgraphj.core.node;

import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.runtime.ExecutionContext;

public interface Node {
    String id();
    NodeResult execute(State state, ExecutionContext ctx) throws Exception;
}

