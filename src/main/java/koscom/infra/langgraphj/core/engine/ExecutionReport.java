package koscom.infra.langgraphj.core.engine;

import koscom.infra.langgraphj.core.node.NodeResult;
import koscom.infra.langgraphj.core.state.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ExecutionReport {

    public static final class Step {
        public final String nodeId;
        public final NodeResult result;
        public final Map<String, String> stateSnapshot;

        Step(String nodeId, NodeResult result, Map<String, String> stateSnapshot) {
            this.nodeId = nodeId;
            this.result = result;
            this.stateSnapshot = stateSnapshot;
        }
    }

    private final List<Step> steps = new ArrayList<>();
    private State finalState;

    public void addStep(String nodeId, NodeResult result, State state) {
        steps.add(new Step(nodeId, result, state.snapshot()));
    }

    public List<Step> steps() { return steps; }
    public State finalState() { return finalState; }
    public void setFinalState(State s) { this.finalState = s; }
}
