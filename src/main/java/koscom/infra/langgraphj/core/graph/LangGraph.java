package koscom.infra.langgraphj.core.graph;

import koscom.infra.langgraphj.core.edge.Edge;
import koscom.infra.langgraphj.core.node.Node;

import java.util.*;

public final class LangGraph {
    public static final String END = "__END__";

    private final Map<String, Node> nodes = new LinkedHashMap<>();
    private final Map<String, List<Edge>> outgoing = new HashMap<>();
    private String entryNodeId;

    public LangGraph addNode(Node node) {
        Objects.requireNonNull(node, "node");
        nodes.put(node.id(), node);
        outgoing.putIfAbsent(node.id(), new ArrayList<>());
        return this;
    }

    public LangGraph addEdge(Edge edge) {
        Objects.requireNonNull(edge, "edge");
        outgoing.putIfAbsent(edge.from(), new ArrayList<>());
        outgoing.get(edge.from()).add(edge);
        return this;
    }

    public LangGraph setEntry(String nodeId) {
        this.entryNodeId = nodeId;
        return this;
    }

    public Optional<Node> findNode(String nodeId) {
        return Optional.ofNullable(nodes.get(nodeId));
    }

    public List<Edge> outgoingEdges(String fromNodeId) {
        return outgoing.getOrDefault(fromNodeId, Collections.emptyList());
    }

    public String entry() {
        return entryNodeId;
    }

    public Set<String> nodeIds() {
        return nodes.keySet();
    }

    public void validate() {
        if (entryNodeId == null || !nodes.containsKey(entryNodeId)) {
            throw new IllegalStateException("Entry node is not set or missing: " + entryNodeId);
        }
        // edge target 존재 체크(END는 허용)
        for (Map.Entry<String, List<Edge>> e : outgoing.entrySet()) {
            for (Edge edge : e.getValue()) {
                if (!nodes.containsKey(edge.from())) {
                    throw new IllegalStateException("Edge from node missing: " + edge.from());
                }
                if (!END.equals(edge.to()) && !nodes.containsKey(edge.to())) {
                    throw new IllegalStateException("Edge to node missing: " + edge.to());
                }
            }
        }
    }
}
