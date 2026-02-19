package koscom.infra.langgraphj.dsl;

import koscom.infra.langgraphj.core.edge.Edge;
import koscom.infra.langgraphj.core.graph.LangGraph;
import koscom.infra.langgraphj.core.node.Node;
import koscom.infra.langgraphj.core.state.State;

import java.util.function.Predicate;

public final class GraphBuilder {
    private final LangGraph graph = new LangGraph();
    private final State initialState = new State();

    public static GraphBuilder create() {
        return new GraphBuilder();
    }

    public GraphBuilder entry(String nodeId) {
        graph.setEntry(nodeId);
        return this;
    }

    public GraphBuilder node(Node node) {
        graph.addNode(node);
        return this;
    }

    public GraphBuilder edge(String from, String to, Predicate<State> cond) {
        graph.addEdge(new Edge(from, to, cond));
        return this;
    }

    public GraphBuilder edge(String from, String to) {
        return edge(from, to, s -> true);
    }

    public GraphBuilder putState(String key, String value) {
        initialState.put(key, value);
        return this;
    }

    public GraphBuildResult build() {
        return new GraphBuildResult(graph, initialState);
    }

    public static final class GraphBuildResult {
        public final LangGraph graph;
        public final State initialState;

        GraphBuildResult(LangGraph graph, State initialState) {
            this.graph = graph;
            this.initialState = initialState;
        }
    }
}
