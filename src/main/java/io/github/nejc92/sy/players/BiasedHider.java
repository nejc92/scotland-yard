package io.github.nejc92.sy.players;

import io.github.nejc92.sy.game.Action;
import io.github.nejc92.sy.game.State;
import io.github.nejc92.sy.playouts.BiasedPlayout;

public abstract class BiasedHider extends Hider {

    public BiasedHider(Operator operator) {
        super(operator);
    }

    @Override
    protected Action getActionForHiderFromStatesAvailableActionsForSimulation(State state) {
        return BiasedPlayout.getGreedyBiasedActionForHider(state);
    }

    @Override
    protected Action getActionForSeekerFromStatesAvailableActionsForSimulation(State state) {
        return BiasedPlayout.getGreedyBiasedActionForSeeker(state);
    }
}