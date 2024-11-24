package ws.aperture.chess.model.pieces;

import java.util.List;
import java.util.ArrayList;


import ws.aperture.chess.model.Square;
import ws.aperture.chess.model.Game;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Board;

import ws.aperture.chess.utilities.Pair;

public final class Queen extends Piece {

    public Queen(Side side, Board board, Square square){
        super(side, board, square);
    }

    
    public Pair<List<Square> , List<Square>> getUncheckedMovesAttacks() {
        List<Square> uncheckedMoves     = new ArrayList<Square>();
        List<Square> uncheckedAttacks   = new ArrayList<Square>();

        final int[][] deltas = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // NE, SE, SW, NW, N, E, S, W
		final int currentRow = square.getRow();
		final int currentCol = square.getCol();
        int newRow, newCol;
        Square prospective;

        for (int[] d : deltas) {
			int steps = 1;

			for (;;) {
				newRow = currentRow + (steps * d[0]);
				newCol = currentCol + (steps * d[1]);

                if (!Board.onBoard(newRow, newCol)) {
                    break;
                }

                prospective = board.getSquare(newRow, newCol);
                if (prospective.isOccupied()) {
                    Piece occupier = prospective.getPiece();
                    if (!sameSide(occupier)) {
                        uncheckedAttacks.add(prospective);
                    }
                    break;
                } else {
                    uncheckedMoves.add(prospective);
                    steps++;
                }
			}
        }

        return new Pair<List<Square>,List<Square>>(uncheckedMoves, uncheckedAttacks);
    }

    /**
     *  Returns the next step attacks, no checking
     */
    public List<Square> getUncheckedAttacks() {
        List<Square> uncheckedAttacks   = new ArrayList<Square>();

        final int[][] deltas = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // NE, SE, SW, NW, N, E, S, W
		final int currentRow = getRow();
		final int currentCol = getCol();
        int newRow, newCol;
        Square prospective;

        for (int[] d : deltas) {
			int steps = 1;

			for (;;) {
				newRow = currentRow + (steps * d[0]);
				newCol = currentCol + (steps * d[1]);

                if (!Board.onBoard(newRow, newCol)) {
                    break;
                }

                prospective = board.getSquare(newRow, newCol);
                if (prospective.isOccupied()) {
                    Piece occupier = prospective.getPiece();
                    if (!sameSide(occupier)) {
                        uncheckedAttacks.add(prospective);
                    }
                    break;
                } else {
                    steps++;
                }
			}
        }

        return uncheckedAttacks;
    }

   
    @Override
    public String toString() {
        return super.toString() + "Q";
    }


}
