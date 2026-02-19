package koscom.infra.langgraphj.core.node;

import koscom.infra.langgraphj.core.state.State;
import koscom.infra.langgraphj.runtime.ExecutionContext;

import java.util.Objects;
import java.util.function.Predicate;


public final class ConditionRoutingNode implements Node {

    @FunctionalInterface
    public interface StateMutation {
        void apply(State state);
    }

    private final String id;
    private final Predicate<State> condition;

    // 라우팅 플래그를 state에 기록하기 위한 값
    private final String routeKey;
    private final String trueRouteValue;
    private final String falseRouteValue;

    // 조건 true/false일 때 추가로 state 업데이트(선택)
    private final StateMutation onTrue;
    private final StateMutation onFalse;

    public ConditionRoutingNode(String id,
                                Predicate<State> condition,
                                String routeKey,
                                String trueRouteValue,
                                String falseRouteValue,
                                StateMutation onTrue,
                                StateMutation onFalse) {
        this.id = Objects.requireNonNull(id);
        this.condition = Objects.requireNonNull(condition);
        this.routeKey = Objects.requireNonNull(routeKey);
        this.trueRouteValue = Objects.requireNonNull(trueRouteValue);
        this.falseRouteValue = Objects.requireNonNull(falseRouteValue);
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    @Override public String id() { return id; }

    @Override
    public NodeResult execute(State state, ExecutionContext ctx) {
        boolean ok = condition.test(state);

        if (ok) {
            state.put(routeKey, trueRouteValue);
            if (onTrue != null) onTrue.apply(state);
            return NodeResult.ok("route=" + trueRouteValue);
        } else {
            state.put(routeKey, falseRouteValue);
            if (onFalse != null) onFalse.apply(state);
            return NodeResult.ok("route=" + falseRouteValue);
        }
    }
}