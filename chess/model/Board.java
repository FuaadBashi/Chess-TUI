package ws.aperture.chess.model;

import java.util.ArrayList;
import java.util.List;

import ws.aperture.chess.model.pieces.Bishop;
import ws.aperture.chess.model.pieces.King;
import ws.aperture.chess.model.pieces.Knight;
import ws.aperture.chess.model.pieces.Pawn;
import ws.aperture.chess.model.pieces.Piece;
import ws.aperture.chess.model.pieces.Queen;
import ws.aperture.chess.model.pieces.Rook;


public final class Board {
    static final int NUM_ROW_COL = 8;
    private Square[][] board;
    private final ArrayList<Piece> whitePieces, blackPieces;
    private final ArrayList<Piece> whiteCaptured, blackCaptured;
    private Square whiteKingSquare, blackKingSquare;
    
    Board() {
        
        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();
        whiteCaptured = new ArrayList<Piece>();
        blackCaptured = new ArrayList<Piece>();
        
        createNewEmptyBoard();
        populateBoard();
    }

    /** 
     *  For each piece in the game's Piece lists, we create a new Piece from the old.
     *  Then we place the new piece at the same location as the old piece.
     * 
     * @param fromBoard      The board from which to copy data from.
     */
    Board(Board fromBoard) {
        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();
        createNewEmptyBoard();

        for (Piece oldPiece : fromBoard.getAllPieces()) {
            
            int row = oldPiece.getRow();
            int col = oldPiece.getCol();

            Square newSquare = getSquare(row, col);

            Piece newPiece = Piece.createTheoryPiece(oldPiece, this, newSquare);
            placePiece(newPiece, row, col);

            if (newPiece.getSide() == Side.WHITE) {
                whitePieces.add(newPiece);
            } else {
                blackPieces.add(newPiece);
            }
        }

        whiteCaptured = new ArrayList<Piece>();
        blackCaptured = new ArrayList<Piece>();

        for (Piece whiteDead : fromBoard.getWhiteCaptured()) {
            Piece newDead = Piece.createTheoryPiece(whiteDead, fromBoard, null);
            whiteCaptured.add(newDead);
        }

        for (Piece blackDead : fromBoard.getWhiteCaptured()) {
            Piece newDead = Piece.createTheoryPiece(blackDead, fromBoard, null);
            whiteCaptured.add(newDead);
        } 
        
        int whiteKingRow    = fromBoard.getKingSquare(Side.WHITE).getRow();
        int whiteKingCol    = fromBoard.getKingSquare(Side.WHITE).getCol();
        whiteKingSquare     = getSquare(whiteKingRow, whiteKingCol);

        int blackKingRow    = fromBoard.getKingSquare(Side.BLACK).getRow();
        int blackKingCol    = fromBoard.getKingSquare(Side.BLACK).getCol();
        blackKingSquare     = getSquare(blackKingRow, blackKingCol);
    }

    void createNewEmptyBoard() {
        board  = new Square[NUM_ROW_COL][NUM_ROW_COL];
        for (int i = 0; i < NUM_ROW_COL; i++) {
            for (int j = 0; j < NUM_ROW_COL; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    private void populateBoard() {

        for (int j = 0; j < NUM_ROW_COL; ++j) {
            Piece whitePawn = new Pawn(Side.WHITE, this, getSquare(1, j));
            placePiece(whitePawn, 1, j);
        }

        for (int j = 0; j < NUM_ROW_COL; ++j) {
            Piece blackPawn = new Pawn(Side.BLACK, this, getSquare(6, j));
            placePiece(blackPawn, 6, j);
        }



        /* Whew! Smelly!
        *
        *     Currently, we called placePiece
        *
        *
        * */

        placePiece( new Rook( Side.WHITE, this,        getSquare(0, 0)), 0, 0);
        placePiece( new Knight( Side.WHITE, this,      getSquare(0, 1)), 0, 1);
        placePiece( new Bishop( Side.WHITE, this,      getSquare(0, 2)), 0, 2);
        placePiece( new Queen( Side.WHITE, this,       getSquare(0, 3)), 0, 3);
        placePiece( new King( Side.WHITE, this,        getSquare(0, 4)), 0, 4);
        placePiece( new Bishop( Side.WHITE, this,      getSquare(0, 5)), 0, 5);
        placePiece( new Knight( Side.WHITE, this,      getSquare(0, 6)), 0, 6);
        placePiece( new Rook( Side.WHITE, this,        getSquare(0, 7)), 0, 7);
        placePiece( new Rook( Side.BLACK, this,      getSquare(7, 0)), 7, 0);
        placePiece( new Knight( Side.BLACK, this,    getSquare(7, 1)), 7, 1);
        placePiece( new Bishop( Side.BLACK , this,   getSquare(7, 2)), 7, 2);
        placePiece( new Queen( Side.BLACK, this,     getSquare(7, 3)), 7, 3);
        placePiece( new King( Side.BLACK, this,      getSquare(7, 4)), 7, 4);
        placePiece( new Bishop( Side.BLACK, this,    getSquare(7, 5)), 7, 5);
        placePiece( new Knight( Side.BLACK, this,    getSquare(7, 6)), 7, 6);
        placePiece( new Rook( Side.BLACK, this,      getSquare(7, 7)), 7, 7);

        int[] whiteRows = {0, 1};
        int[] blackRows = {6, 7};

        Piece currPiece;
        for (int j = 0; j < NUM_ROW_COL; ++j) {
            for (int i : whiteRows) {
                currPiece = getSquare(i, j).getPiece();
                whitePieces.add(currPiece);
                
            }
            for (int i : blackRows) {
                currPiece = getSquare(i, j).getPiece();
                blackPieces.add(currPiece);
            }
        }
    }



    public static boolean onBoard(int row, int col) {
        return (row >= 0 && row < NUM_ROW_COL && col >= 0 && col < NUM_ROW_COL);
    }

    /**
     * @param piece     Piece to be placed on a Square.
     * @param row       The row index of the Square.
     * @param col       The column index of the Square.
     * @return          True if the piece was placed at the supplied indices, False otherwise.
     */



    /*
    *   SMELL: return value is not used, because placePiece is only called from safe contexts, does it ever return false?
    *
    *
    *
    * */
    private boolean placePiece(Piece piece, int row, int col) {
        if (onBoard(row, col)) {
            Square destSquare = getSquare(row, col);
            if (piece instanceof King) {
                if (piece.getSide() == Side.WHITE) {
                    whiteKingSquare = destSquare;
                } else {
                    blackKingSquare = destSquare;
                }
            }
            
            return destSquare.placePiece(piece);
        } else {
            return false;
        }
    }

    public Piece getPiece(int row, int col) {
        return onBoard(row, col) ? board[row][col].getPiece() : null;
    }

    public Piece getPiece(String squareCode) {
        return getSquare(squareCode) != null ? getSquare(squareCode).getPiece() : null;
    }

    public Square getSquare(int row, int col) {
        return onBoard(row, col) ? board[row][col] : null;
    }

    

    Square getSquare(String squareCode) {
        squareCode = squareCode.toUpperCase();
        if (squareCode.length() != 2) {
            return null;
        }

        int row = squareCode.charAt(1) - '0' - 1;
        int col = squareCode.charAt(0) - 'A';

       return getSquare(row, col);
    }

    /*
     * Swap two pieces on the board, used for castling.
     */
    private void swap(Piece p1, Piece p2) {
        final int p1Row = p1.getRow();
        final int p1Col = p1.getCol();
        final int p2Row = p2.getRow();
        final int p2Col = p2.getCol();

        p1.getSquare().removePiece();
        p2.getSquare().removePiece();

        p1.setHasMoved();
        p2.setHasMoved();

        placePiece(p1, p2Row, p2Col);
        placePiece(p2, p1Row, p1Col);
    }

    void move(Piece piece, Square destSquare) {
        piece.getSquare().removePiece();
        piece.setHasMoved();
        final int row = destSquare.getRow();
        final int col = destSquare.getCol();
        placePiece(piece, row, col); 
    }

    void attack(Piece piece, Square destSquare) {

        if ( piece instanceof Pawn 
        && !destSquare.isOccupied()) {
            Square sourceSquare = piece.getSquare();
            Square attackeeSquare = getSquare( piece.getSquare().getRow(), destSquare.getCol() );
            Piece attackeePiece   = attackeeSquare.getPiece();
            piece.setHasMoved();
            attackeeSquare.removePiece();
            sourceSquare.removePiece();
            destSquare.placePiece(piece);
            capture(attackeePiece);


        } else {
            Square sourceSquare = piece.getSquare();
            Piece attackee = destSquare.getPiece();
            piece.setHasMoved();
            destSquare.removePiece();
            sourceSquare.removePiece();
            final int row = destSquare.getRow();
            final int col = destSquare.getCol();
            placePiece(piece, row, col);
            capture(attackee);
        }

        
    }

    
    /**
     * 
     * @param captured
     */
    private void capture(Piece captured) {
        if (captured.getSide() == Side.WHITE) {
            whiteCaptured.add(captured);
            whitePieces.remove(captured);    
        } else {
            blackCaptured.add(captured);
            blackPieces.remove(captured);
        }
    }

    List<Piece> getWhiteCaptured() {
        return whiteCaptured;
    }

    List<Piece> getBlackCaptured() {
        return blackCaptured;
    }

    List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<Piece>();
        pieces.addAll(whitePieces);
        pieces.addAll(blackPieces);
        return pieces;
    }

    List<Piece> getPieces(Side side) {
        return (side == Side.WHITE) ? whitePieces : blackPieces;
    }

    Square getKingSquare(Side side) {
        return (side == Side.WHITE) ? whiteKingSquare : blackKingSquare;
    }

    Piece getKing(Side side) {
        return getKingSquare(side).getPiece();
    }

    static Board commitMove(Board orig, int sourceRow, int sourceCol, int destRow, int destCol, boolean isMove) {
        Board theoryBoard = new Board(orig);  // create new copy of this board

        Piece piece       = theoryBoard.getPiece(sourceRow, sourceCol);
        Square destSquare = theoryBoard.getSquare(destRow, destCol);
        // Commit the move or attack
        if (isMove) {
            theoryBoard.move(piece, destSquare);
        } else {
            theoryBoard.attack(piece, destSquare);
        }

        return theoryBoard;
    }

    public static boolean selfChecked(Board board, Side turn) {
        Side enemySide = Side.otherSide(turn);
        Square myKingSquare = board.getKingSquare(turn);
        
        for (Piece enemy : board.getPieces(enemySide)) {
            List<Square> uncheckedAttacks = enemy.getUncheckedAttacks();
            for (Square attackTarget : uncheckedAttacks) {
                if ( attackTarget == myKingSquare ) {
                    return true;
                }
            }
        }

        return false;
    }


    boolean canCastleKS(Side side) {

        Square kingSquare = (side == Side.WHITE) ? whiteKingSquare : blackKingSquare;
        char kingRow      = (side == Side.WHITE) ? '1' : '8';
        if ( kingSquare != getSquare("E" + kingRow) ) {
            return false;
        }

        Piece kingSideRook = getPiece("H" + kingRow);
        if ( kingSideRook == null || !(kingSideRook instanceof Rook) || kingSideRook.getSide() == Side.otherSide(side)) {
            return false;
        }

        boolean neitherMoved = !kingSquare.getPiece().hasMoved() && !kingSideRook.hasMoved();
        boolean step1Empty = !getSquare("F" + kingRow).isOccupied();
        boolean step2Empty = !getSquare("G" + kingRow).isOccupied();
        
        return step1Empty && step2Empty && neitherMoved;

    }

    boolean canCastleQS(Side side) {
        Square kingSquare = (side == Side.WHITE) ? whiteKingSquare : blackKingSquare;
        char kingRow      = (side == Side.WHITE) ? '1' : '8';

        if ( kingSquare != getSquare("E" + kingRow) ) {
            return false;
        }

        Piece queenSideRook = getPiece("A" + kingRow);

        if ( queenSideRook == null || !(queenSideRook instanceof Rook) || queenSideRook.getSide() == Side.otherSide(side)) {
            return false;
        }

        boolean neitherMoved = !kingSquare.getPiece().hasMoved() && !queenSideRook.hasMoved();
        boolean step1Empty     = !getSquare("D" + kingRow).isOccupied();
        boolean step2Empty     = !getSquare("C" + kingRow).isOccupied();
        boolean step3Empty     = !getSquare("B" + kingRow).isOccupied();
        

        return step1Empty && step2Empty && step3Empty && neitherMoved;
    }

    


}
