package ws.aperture.chess.model.pieces;

import java.util.List;

import ws.aperture.chess.utilities.Pair;
import ws.aperture.chess.model.Square;
import ws.aperture.chess.model.Game;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Board;




import java.util.ArrayList;

public final class Knight extends Piece {

    public Knight(Side side, Board board, Square square) {
        super(side, board, square);
    }

    
    public Pair<List<Square> , List<Square>> getUncheckedMovesAttacks() {

        List<Square> uncheckedMoves     = new ArrayList<Square>();
        List<Square> uncheckedAttacks   = new ArrayList<Square>();
        
        final int[][] deltas = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}}; 
		final int currentRow = square.getRow();
		final int currentCol = square.getCol();
        int newRow, newCol;
        Square prospective;

        for (int[] d : deltas) {
            newRow = currentRow + d[0];
            newCol = currentCol + d[1];

            if (!Board.onBoard(newRow, newCol)) {
                continue;
            }

            prospective = board.getSquare(newRow, newCol);

            if (prospective.isOccupied()) {
                Piece occupier = prospective.getPiece();
                if (!sameSide(occupier)) {
                    uncheckedAttacks.add(prospective);
                }
            } else {
                uncheckedMoves.add(prospective);
            }
        }

        return new Pair<List<Square>,List<Square>>(uncheckedMoves, uncheckedAttacks);
    }

    public List<Square> getUncheckedAttacks() {

        List<Square> uncheckedAttacks   = new ArrayList<Square>();
        
        final int[][] deltas = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}}; 
		final int currentRow = getRow();
		final int currentCol = getCol();
        int newRow, newCol;
        Square prospective;

        for (int[] d : deltas) {
            newRow = currentRow + d[0];
            newCol = currentCol + d[1];

            if (!Board.onBoard(newRow, newCol)) {
                continue;
            }

            prospective = board.getSquare(newRow, newCol);

            if (prospective.isOccupied()) {
                Piece occupier = prospective.getPiece();
                if (!sameSide(occupier)) {
                    uncheckedAttacks.add(prospective);
                }
            }
        }

        return uncheckedAttacks;
    }

  
    @Override
    public String toString() {
        return super.toString() + "N";
    }

}