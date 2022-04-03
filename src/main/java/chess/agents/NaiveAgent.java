package chess.agents;

import chess.Board;
import chess.services.BoardMovement;
import chess.services.GlobalContext;
import chess.util.Move;

import java.util.List;
import java.util.Random;

//Plays a random move
public class NaiveAgent {

    public void play() {
        Board board = GlobalContext.getBoard();
        BoardMovement boardMovement = GlobalContext.getBoardMovement();

        List<Move> moves = board.getAvailableMovesForPlayer();
        Random rand = new Random();
        Move move = moves.get(rand.nextInt(moves.size()));

        int[] from = move.getCurrentPosition();
        int[] to = move.getTargetPosition();
        boardMovement.moveFigure(from[0], from[1], to[0], to[1]);
    }

}
