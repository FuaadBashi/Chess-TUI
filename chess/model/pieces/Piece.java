package ws.aperture.chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import ws.aperture.chess.model.Board;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Square;
import ws.aperture.chess.utilities.Pair;

public abstract class Piece {
    protected final Side        side;
    protected Square            square;
    protected final Board       board;
    private List<Square>        legalMoves, legalAttacks;
    

    protected boolean           hasMoved;

    protected Piece(Side side, Board board, Square square) {
        this.side           = side;
        this.board          = board;
        this.square         = square;
        this.legalMoves     = new ArrayList<Square>();
        this.legalAttacks   = new ArrayList<Square>();
        hasMoved            = false;
    }
     
    public static Piece promote(Pawn p, char choice){
        Side pawnSide = p.getSide();
        Board sameBoard = p.getBoard();
        switch (choice) {
            case 'R':
                return new Rook(pawnSide, sameBoard, null);
            case 'B' :
                return new Bishop(pawnSide, sameBoard, null);
            case 'N' :
                return new Knight(pawnSide, sameBoard, null);
            case 'Q' :
                return new Queen(pawnSide, sameBoard, null);
            default :       
                return new Pawn(pawnSide, sameBoard, null);   

        }
    }

    /**
     *  Factory method for generating clone pieces for a new theory board.
     * @param piece     The Piece we are cloning.
     * @param board     The new Board on which the new Piece will reside.
     * @param square    The Square on the new Board on which the new Piece will reside.
     * @return          The newly created Piece object, of correct subclass.
     */

    public static Piece createTheoryPiece(Piece piece, Board board, Square square) {
        if (piece instanceof King) {
            return new King(piece.getSide(), board, square);
        } else if (piece instanceof Bishop) {
            return new Bishop(piece.getSide(), board, square);
        } else if (piece instanceof Queen) {
            return new Queen(piece.getSide(), board, square);
        } else if (piece instanceof Pawn) {
            return new Pawn(piece.getSide(), board, square);
        } else if (piece instanceof Rook) {
            return new Rook(piece.getSide(), board, square);
        } else {
            return new Knight(piece.getSide(), board, square);
        }
    }

    public abstract Pair< List<Square> , List<Square> > getUncheckedMovesAttacks();
    public abstract List<Square> getUncheckedAttacks();
 
    public Board getBoard() {
        return board;
    }

    public int getRow() {
        return square.getRow();
    }

    public int getCol() {
        return square.getCol();
    }

    public char getColChar() {
        return square.getColChar();
    }

    public char getRowChar() {
        return square.getRowChar();
    }

    public Square getSquare() {
        return square;
    }


    public void setSquare(Square square) {
        this.square = square;
    }

    public Side getSide() {
        return side;
    }

    public boolean sameSide(Piece otherPiece) {
        return side == otherPiece.getSide();
    }

    public List<Square> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves( List<Square> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public List<Square> getLegalAttacks() {
        return legalAttacks;
    }

    public void setLegalAttacks(List<Square> legalAttacks) {
        this.legalAttacks = legalAttacks;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved() {
        if (!hasMoved()) {
            hasMoved = true;
        }
    }
    public boolean hasLegalMovesAndAttacks (){
        return !(legalAttacks.size() == 0 && legalMoves.size() == 0);
    }

    @Override
    public  String toString(){
        return "";
    }
    


    

}