package io.github.nejc92.sy;

import io.github.nejc92.mcts.Mcts;
import io.github.nejc92.sy.game.Action;
import io.github.nejc92.sy.game.board.Board;
import io.github.nejc92.sy.game.State;
import io.github.nejc92.sy.players.*;

import java.util.Scanner;

public class ScotlandYard {

    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        Mcts<State, Action, Player> mcts = Mcts.initializeIterations(150);
        mcts.dontClone(Board.class);
        Player[] players = initializePlayers();
        State state = State.initialize(players);
        while (!state.isTerminal()) {
            state.printNewRound();
            System.out.println("Current player: " + state.getCurrentAgent());
            System.out.print("Positions: ");
            state.printPositions();
            if (state.getAvailableActionsForCurrentAgent().size() > 0) {
                Action mostPromisingAction;
                if (state.currentPlayerIsHuman()) {
                    System.out.println("Available actions: " + state.getAvailableActionsForCurrentAgent());
                    System.out.print("Enter action: ");
                    int action = Integer.parseInt(scanner.nextLine());
                    mostPromisingAction = state.getAvailableActionsForCurrentAgent().get(action);
                }
                else {
                    state.setSearchModeOn();
                    mostPromisingAction = mcts.uctSearchWithExploration(state, 2);
                    System.out.println(mostPromisingAction);
                    state.setSearchModeOff();
                }
                state.performActionForCurrentAgent(mostPromisingAction);
            }
            else
                state.skipCurrentAgent();
            System.out.print("New positions: ");
            state.printPositions();
            System.out.println();
            if (state.previousPlayerIsHider() && state.previousPlayerIsHuman()) {
                Hider hider = (Hider)state.getPreviousAgent();
                if (hider.hasDoubleMoveCard()) {
                    System.out.println("Use double move? y/n");
                    String doubleMove = scanner.nextLine();
                    if (doubleMove.equals("y")) {
                        state.skipAllSeekers();
                        hider.removeDoubleMoveCard();
                    }
                }
            }
        }
        if (state.seekersWon())
            System.out.println("Seekers won!");
        else
            System.out.println("Hider won!");
    }

    private static Player[] initializePlayers() {
        Player[] players = new Player[6];
        players[0] = new RandomHider(Player.Operator.HUMAN);
        players[1] = new BiasedSeeker(Player.Operator.COMPUTER, Seeker.Color.BLACK);
        players[2] = new BiasedSeeker(Player.Operator.COMPUTER, Seeker.Color.BLUE);
        players[3] = new BiasedSeeker(Player.Operator.COMPUTER, Seeker.Color.YELLOW);
        players[4] = new BiasedSeeker(Player.Operator.COMPUTER, Seeker.Color.RED);
        players[5] = new BiasedSeeker(Player.Operator.COMPUTER, Seeker.Color.GREEN);
        return players;
    }
}