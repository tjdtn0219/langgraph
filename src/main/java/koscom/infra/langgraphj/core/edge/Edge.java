package koscom.infra.langgraphj.core.edge;

import koscom.infra.langgraphj.core.state.State;

import java.util.function.Predicate;

public final class Edge {
    private final String from;
    private final String to;
    private final Predicate<State> condition;

    public Edge(String from, String to, Predicate<State> condition) {
        this.from = from;
        this.to = to;
        this.condition = condition != null ? condition : (s -> true); // default: always true
    }

    public String from() { return from; }
    public String to() { return to; }
    public boolean matches(State state) { return condition.test(state); }
}
