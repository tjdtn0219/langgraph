package koscom.infra.langgraphj.runtime;

import koscom.infra.langgraphj.core.node.NodeResult;
import koscom.infra.langgraphj.core.state.State;

public interface RuntimeHook {
    void beforeNode(String nodeId, State state);
    void afterNode(String nodeId, State state, NodeResult result);
    void onError(String nodeId, State state, Exception ex);
}
