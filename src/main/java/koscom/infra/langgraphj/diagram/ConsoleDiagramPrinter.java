package koscom.infra.langgraphj.diagram;

import koscom.infra.langgraphj.core.edge.Edge;
import koscom.infra.langgraphj.core.graph.LangGraph;

import java.util.List;

public final class ConsoleDiagramPrinter {

    public String print(LangGraph graph) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LangGraphJ Diagram ===\n");
        sb.append("Entry: ").append(graph.entry()).append("\n\n");

        for (String nodeId : graph.nodeIds()) {
            sb.append("[").append(nodeId).append("]\n");
            List<Edge> edges = graph.outgoingEdges(nodeId);
            for (Edge e : edges) {
                sb.append("  -> ").append(e.to()).append(" (cond)\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
