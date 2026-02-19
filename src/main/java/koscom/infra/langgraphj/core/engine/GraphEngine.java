package koscom.infra.langgraphj.core.engine;

import koscom.infra.langgraphj.core.edge.Edge;
import koscom.infra.langgraphj.core.graph.LangGraph;
import koscom.infra.langgraphj.core.node.Node;
import koscom.infra.langgraphj.core.node.NodeResult;
import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.runtime.ExecutionContext;
import koscom.infra.langgraphj.runtime.RuntimeHook;

import java.util.List;

public final class GraphEngine {

    private final int maxSteps;

    public GraphEngine(int maxSteps) {
        this.maxSteps = maxSteps <= 0 ? 1000 : maxSteps;
    }

    public ExecutionReport run(LangGraph graph, State state, ExecutionContext ctx) throws Exception {
        graph.validate();

        String current = graph.entry();
        int step = 0;
        ExecutionReport report = new ExecutionReport();

        while (current != null && !LangGraph.END.equals(current)) {
            if (step++ > maxSteps) {
                throw new IllegalStateException("Max steps exceeded. Possible infinite loop.");
            }

            String finalCurrent = current;
            Node node = graph.findNode(current)
                    .orElseThrow(() -> new IllegalStateException("Node missing: " + finalCurrent));

            RuntimeHook hook = ctx.hook();
            if (hook != null) hook.beforeNode(node.id(), state);

            NodeResult result;
            try {
                result = node.execute(state, ctx);
            } catch (Exception ex) {
                if (hook != null) hook.onError(node.id(), state, ex);
                throw ex;
            }

            report.addStep(node.id(), result, state);

            if (hook != null) hook.afterNode(node.id(), state, result);

            // routing: first matching edge
            List<Edge> edges = graph.outgoingEdges(node.id());
            String next = null;
            for (Edge edge : edges) {
                if (edge.matches(state)) {
                    next = edge.to();
                    break;
                }
            }
            current = next; // next could be null => stop
        }

        report.setFinalState(state);
        return report;
    }
}
