/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.ariesplayer;

import java.util.List;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;


public class AriesPlayer extends Player {

    private Color myColor;
    private Color opColor;
    private int bestMove;

    private int evaluate(Board b){
        int eval = 0;
        int size = b.getSize();
        for (int i=0; i<size; ++i){
            for (int j=0; j<size; ++j){
                if (b.getState(i, j) == myColor){
                    ++eval;
                }
                else if (b.getState(i, j) != Color.EMPTY){
                    --eval;
                }
            }
        }
        return eval;
    }

    private int alphaBeta(Board board, boolean maxi, int depth, int alpha, int beta, boolean begin){
        if (depth == 0){
            return evaluate(board);
        }
        List<Move> moves;
        int eval;
        int bestScore;
        int size = board.getSize();
        if (maxi) {
            bestScore = -1000;
            moves = board.getMovesFor(myColor);
            for (Move m : moves){
                board.doMove(m);
                if (myColor == Color.PLAYER1 && board.getState(size-1, size-1) == myColor){
                    if (begin) {
                        bestMove = moves.indexOf(m);
                    }
                    return 9000;
                }
                else if (myColor == Color.PLAYER2 && board.getState(0, 0) == myColor){
                    if (begin) {
                        bestMove = moves.indexOf(m);
                    }
                    return 9000;
                }

                eval = alphaBeta(board.clone(), false, depth-1, alpha, beta, false);
                if (eval > bestScore){
                    bestScore = eval;
                    if (begin) {
                        bestMove = moves.indexOf(m);
                    }
                    if (alpha < bestScore){
                        alpha = bestScore;
                    }
                }
                if (beta <= alpha){
                    break;
                }
                board.undoMove(m);
            }
        }
        else{
            bestScore = 1000;
            moves = board.getMovesFor(opColor);
            for (Move m : moves){
                board.doMove(m);
                if (myColor == Color.PLAYER1 && board.getState(0, 0) == opColor){
                    return -9000;
                }
                else if (myColor == Color.PLAYER2 && board.getState(size-1, size-1) == opColor){
                    return -9000;
                }
                eval = alphaBeta(board.clone(), true, depth-1, alpha, beta, false);
                if (eval < bestScore){
                    bestScore = eval;
                    if (beta > bestScore){
                        beta = bestScore;
                    }
                }
                if (beta <= alpha){
                    break;
                }
                board.undoMove(m);
            }
        }
        return bestScore;
    }

    @Override
    public String getName() {
        return "Dominik Pawłowski 145289";
    }


    @Override
    public Move nextMove(Board b) {
        myColor = getColor();
        if (myColor == Color.PLAYER1){
            opColor = Color.PLAYER2;
        }
        else{
            opColor = Color.PLAYER1;
        }
        List<Move> moves = b.getMovesFor(myColor);
        if (getTime() < 1000 || b.getSize() > 10){
            alphaBeta(b.clone(), true, 2, -1000, 1000, true);
        }
        else if (getTime() < 10000 || b.getSize() > 12) {
            alphaBeta(b.clone(), true, 3, -1000, 1000, true);
        }
        else {
            alphaBeta(b.clone(), true, 4, -1000, 1000, true);
        }
        return moves.get(bestMove);
    }

    public static void main(String[] args){}
}
