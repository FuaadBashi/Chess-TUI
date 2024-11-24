package ws.aperture.chess.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ws.aperture.chess.model.pieces.Piece;

public final class Square {
    private final  int row;  // nums(rank)
    private final  int col; // chars(file)
    private Piece piece;

    private List<Piece>      prospectiveMovers;

    public List<Piece> getProspectiveMovers() {
        return prospectiveMovers;
    }

    public void addProspectiveMover( Piece piece ) {
        prospectiveMovers.add( piece );
    }

    public void clearProspectiveMovers() {
        prospectiveMovers.clear();
    }

    public List<Piece> getConflicts(Piece mover) {
        return prospectiveMovers.stream()
                                .filter( p -> mover.getClass().equals( p.getClass() ) && mover != p && mover.sameSide(p) )                         
                                .collect(Collectors.toList());
    }


    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.prospectiveMovers = new ArrayList<>();
    }

    private Side getColour() {
        return (row % 2 == 0) == (col % 2 == 0) ? Side.BLACK : Side.WHITE;
    }

    private Side getColourOld() {
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                return Side.BLACK;
            } else {
                return Side.WHITE;
            }
        } else {
            if (col % 2 == 0) {
                return Side.WHITE;
            } else {    
                return Side.BLACK;
            }
        }
    }

    /** @return     Chess style code for this square
     *              e.g. 3,5 -> F4
     */
    @Override
    public String toString() {
        return "" + getColChar() + getRowChar();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    /*
     *  1->A, 2->B, ... , 8->H
     */
    public char getColChar() {
        return (char) (col + 'A');
    }

    public char getRowChar() {
        return (char) (row + '0' + 1);
    }

    public Piece getPiece() {
        return piece;
    }   

    public boolean isOccupied() {
        return piece != null;
    }

    /**
     * @param placedPiece       Piece to be placed on this Square.
     * @return                  True if Piece was placed, False otherwise.
     */
    public boolean placePiece(Piece placedPiece) {
        if (this.piece == null) {
            this.piece = placedPiece;
            placedPiece.setSquare(this);
            return true;
        } else {
            return false;
        }
    }

    public void removePiece() {
        if (isOccupied()) {
            piece.setSquare(null);
            piece = null;
        }
    }


    public static boolean areDiagonal(Square sq1, Square sq2) {
        return (Math.abs(sq1.getRow() - sq2.getRow()) == 1) 
            && (Math.abs(sq1.getCol() - sq2.getCol()) == 1);

    }
    

    public static boolean areAdjacent(Square sq1, Square sq2) {
        return 1 == sq1.getRow() - sq2.getRow() + sq1.getCol() - sq2.getCol();
    }


    

    // public static boolean onSameColumn(Piece piece) {
    //     int numberOfSamePieces = 0;
    //     Side side = piece.getSide();
    //     int col = piece.getCol();

    //     if (piece instanceof Knight || piece instanceof Bishop || piece instanceof Pawn) {
    //         return false;
    //     } else {  
    //         for (int i = 0 ; i < 7 ; i++){
    //             Square next = piece.checkSquare(i, col);
    //             if (next.getPiece() == piece && next.getPiece().getSide() == side ) {
    //                 numberOfSamePieces ++;
    //             }
    //         }
    //         return numberOfSamePieces > 1;
    //     }
    // }

    // public static boolean onSameRow(Piece piece) {
    //     int numberOfSamePieces = 0;
    //     Side side = piece.getSide();
    //     int row = piece.getRow();

    //     if (piece instanceof Knight || piece instanceof Bishop) {
    //         return false;
    //     } else {
    //         for (int i = 0 ; i < 7 ; i++){
    //             Square next = piece.checkSquare(row, i);
    //             if (next.getPiece() == piece && next.getPiece().getSide() == side ) {
    //                 numberOfSamePieces ++;
    //             }
    //         }
    //     return numberOfSamePieces > 1;
    //     }
    // }

}