package ws.aperture.chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import ws.aperture.chess.utilities.Pair;
import ws.aperture.chess.model.Square;
import ws.aperture.chess.model.Game;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Board;


public final class Pawn extends Piece {

    private Piece enPassantAttackee;


    public Pawn(Side side, Board board, Square square){
        super(side, board, square);
        enPassantAttackee = null;
    }

    public Pair<List<Square>, List<Square>> getUncheckedMovesAttacks() {

        List<Square> uncheckedMoves     = new ArrayList<Square>();
        List<Square> uncheckedAttacks   = new ArrayList<Square>();

        int row = square.getRow();
        int col = square.getCol();

        if (side == Side.WHITE) {

           

            Square destSquare;

            if ( enPassantAttackee != null ) {
                Square attackeeSquare = enPassantAttackee.getSquare();
                destSquare = board.getSquare( attackeeSquare.getRow() + 1, attackeeSquare.getCol());
                uncheckedAttacks.add( destSquare );
                enPassantAttackee = null;
             }
            
            if (Board.onBoard(row + 1, col)) {
                destSquare = board.getSquare(row + 1, col);
                
                if (!destSquare.isOccupied()) {
                    uncheckedMoves.add(destSquare);
                }
            } 
            if (Board.onBoard(row + 1, col + 1)){
                destSquare = board.getSquare(row + 1, col + 1);
                
                if (destSquare.isOccupied()) {
                    Piece occupier = destSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(destSquare);
                    }
                }
                
            }
            if (Board.onBoard(row + 1, col -  1)){
                destSquare = board.getSquare(row + 1, col - 1);
                
                if (destSquare.isOccupied()) {
                    Piece occupier = destSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(destSquare);
                    }
                }
                
            }
           

            if (row == 1) {
                Square oneStep, twoStep;
                
                if (Board.onBoard(row + 2, col) && Board.onBoard(row + 1, col)) {
                    oneStep = board.getSquare(row + 1, col);
                    twoStep = board.getSquare(row + 2, col);

                    if (!twoStep.isOccupied() && !oneStep.isOccupied()) {
                        uncheckedMoves.add(twoStep);
                    }
                }
            }
        } else {
          

            Square destSquare;

            if ( enPassantAttackee != null ) {
                Square attackeeSquare = enPassantAttackee.getSquare();
                destSquare = board.getSquare( attackeeSquare.getRow() - 1, attackeeSquare.getCol());
                uncheckedAttacks.add( destSquare );
                enPassantAttackee = null;
             }

            if (Board.onBoard(row - 1, col)) {
                destSquare = board.getSquare(row - 1, col);
                
                if (!destSquare.isOccupied()) {
                    uncheckedMoves.add(destSquare);
                }

            } 
            if (Board.onBoard(row - 1 , col + 1)){
                destSquare = board.getSquare(row - 1, col + 1);
                
                if (destSquare.isOccupied()) {
                    Piece occupier = destSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(destSquare);
                    }
                }
                
            }
            if (Board.onBoard(row - 1, col - 1)){
                destSquare = board.getSquare(row - 1, col - 1);
                
                if (destSquare.isOccupied()) {
                    Piece occupier = destSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(destSquare);
                    }
                }
                
            }
            if (row == 6) {
                Square oneStep, twoStep;
                
                if (Board.onBoard(row - 2, col) && Board.onBoard(row - 1, col)) {
                    oneStep = board.getSquare(row - 1, col);
                    twoStep = board.getSquare(row - 2, col);

                    if (!twoStep.isOccupied() && !oneStep.isOccupied()) {
                        uncheckedMoves.add(twoStep);
                    }
                }
            }
        }

        return new Pair<List<Square>,List<Square>>(uncheckedMoves, uncheckedAttacks);

    }


    public List<Square> getUncheckedAttacks() {
        List<Square> uncheckedAttacks = new ArrayList<Square>();
        int row = square.getRow();
        int col = square.getCol();

        Square attackeeSquare;
        if (side == Side.WHITE) {
            
            
            if (Board.onBoard(row + 1, col + 1)){
                attackeeSquare = board.getSquare(row + 1, col + 1);
                
                if (attackeeSquare.isOccupied()) {
                    Piece occupier = attackeeSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(attackeeSquare);
                    }
                }
                
            }
            if (Board.onBoard(row + 1, col -  1)){
                attackeeSquare = board.getSquare(row + 1, col - 1);
                
                if (attackeeSquare.isOccupied()) {
                    Piece occupier = attackeeSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(attackeeSquare);
                    }
                }
                
            }
           
        } else {
            
            if (Board.onBoard(row - 1 , col + 1)) {
                attackeeSquare = board.getSquare(row - 1, col + 1);
                
                if (attackeeSquare.isOccupied()) {
                    Piece occupier = attackeeSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(attackeeSquare);
                    }
                }
                
            }
            if (Board.onBoard(row - 1, col - 1)) {
                attackeeSquare = board.getSquare(row - 1, col - 1);
                
                if (attackeeSquare.isOccupied()) {
                    Piece occupier = attackeeSquare.getPiece();
                    
                    if (!sameSide(occupier)){
                        uncheckedAttacks.add(attackeeSquare);
                    }
                } 
            }
        }

        return uncheckedAttacks;
    }
    
    
    @Override
    public String toString() {
        return super.toString() + "P";
    }


    public Piece getEnPassantAttackee() {
        return enPassantAttackee;
    }

    public void setEnPassantAttackee(Piece enPassantAttackee) {
        this.enPassantAttackee = enPassantAttackee;
    }
}
