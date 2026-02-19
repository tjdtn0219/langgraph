package koscom.infra.langgraphj.runtime;

import koscom.infra.langgraphj.core.node.NodeResult;
import koscom.infra.langgraphj.core.state.State;

public final class DebugPrintHook implements RuntimeHook {

    @Override
    public void beforeNode(String nodeId, State state) {
        System.out.println("\n==============================");
        System.out.println("[BEFORE NODE] " + nodeId);
        System.out.println("STATE: " + state.snapshot());
        System.out.println("==============================");
    }

    @Override
    public void afterNode(String nodeId, State state, NodeResult result) {
        System.out.println("[AFTER NODE] " + nodeId);
        System.out.println("RESULT: " + result.status() + " / " + result.message());
        System.out.println("STATE: " + state.snapshot());
        System.out.println("==============================");
    }

    @Override
    public void onError(String nodeId, State state, Exception ex) {
        System.out.println("[ERROR NODE] " + nodeId);
        System.out.println("STATE: " + state.snapshot());
        ex.printStackTrace();
    }
}
