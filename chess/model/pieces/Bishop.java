package ws.aperture.chess.model.pieces;

import java.util.List;

import ws.aperture.chess.utilities.Pair;
import ws.aperture.chess.model.Square;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Board;




import java.util.ArrayList;

public final class Bishop extends Piece {

    public Bishop(Side side, Board board, Square square){
        super(side, board, square);
    }

    public Pair<List<Square>, List<Square>> getUncheckedMovesAttacks() {
        List<Square> uncheckedMoves     = new ArrayList<Square>();
        List<Square> uncheckedAttacks   = new ArrayList<Square>();

        final int[][] deltas = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};; // NE, SE, SW, NW
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

    public List<Square> getUncheckedAttacks() {
         List<Square> attacks   = new ArrayList<Square>();

        final int[][] deltas = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};; // NE, SE, SW, NW
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
                        attacks.add(prospective);
                    }
                    break;
                } else {
                    steps++;
                }
			}
        }

        return attacks;
    }

    @Override
    public String toString() {
        return super.toString() + "B";
    }







}
