package ws.aperture.chess.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import ws.aperture.chess.model.pieces.Pawn;
import ws.aperture.chess.model.pieces.Piece;



public class Move {

    final private Square sourceSquare, destSquare;
    private LocalDateTime timeStamp; 
    private boolean isAttack;
    private boolean isCheck, isCheckMate;
    private String algebraic ,promotionChoice, enPassantCode;
   
    public Move(Square sourceSquare, Square destSquare, boolean isAttack) {
        this.sourceSquare = sourceSquare;
        this.destSquare   = destSquare;
        timeStamp         = LocalDateTime.now();
        this.isAttack     = isAttack;
        isCheck           = false;
        isCheckMate       = false;
        algebraic         = "";
        promotionChoice   = "";
        enPassantCode     = "";
        setMoveComponent();
    }

    public void setPromotionChoice(char c) {
        promotionChoice += c;
    }

    public void setCheck() {
        isCheck = true;
    }

    public void setCheckMate() {
        isCheckMate = true;
    }

    public String getAlgebraicNotation() {
        return algebraic;
    }

    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM );


    private String resolveAmbiguity(Square sourceSquare, Square destSquare) {
       
        Piece piece = sourceSquare.getPiece();

        List<Piece> potentialMovers = destSquare.getConflicts(piece);

        if ( potentialMovers.isEmpty()) {
            return "";
        }
        
        boolean ranksDiffer = false;

        
        for ( Piece p : potentialMovers ) {

            if ( p.getCol() == piece.getCol() ) {
                ranksDiffer = true;
                break;
            }
        }

        boolean filesDiffer = false;

        for ( Piece p : potentialMovers ) {
            if ( p.getRow() == piece.getRow() ) {
                filesDiffer = true;
            }
        }

        String conflictComponent = "";

        if ( filesDiffer ) {
            conflictComponent += piece.getColChar();
        } 
        
        if ( ranksDiffer && !(piece instanceof Pawn) ) {
            conflictComponent += piece.getRowChar();
        }

        if ( !filesDiffer && !ranksDiffer ) {
            conflictComponent += piece.getColChar() + piece.getRowChar();
        }


        return conflictComponent.toLowerCase();
    }


     /** 
     * Only to be called before move or attack is made
     * 
      */

    private void setMoveComponent() {

        

        String captureCode = isAttack ? "x" : "";
        String sourceSquareCode = sourceSquare.toString();
        String destSquareCode = destSquare.toString();
        Piece piece = sourceSquare.getPiece();
        String pieceCode = (piece instanceof Pawn) ? "" : piece.toString();
        // char lowerCaseColChar = Character.toLowerCase(piece.getSquare().getColChar());
        

        enPassantCode = (Square.areDiagonal(sourceSquare, destSquare)
                                && !destSquare.isOccupied()
                                && piece instanceof Pawn) ? " e.p" :"";

        String kingSideCastle =(sourceSquareCode.charAt(0) == 'E' 
                                && destSquareCode.charAt(0) == 'G'
                                && pieceCode.charAt(0) == 'K') ? "O-O" : "";

        String queenSideCastle =(sourceSquareCode.charAt(0) == 'E' 
                                && destSquareCode.charAt(0) == 'B'
                                && pieceCode.charAt(0) == 'K') ? "O-O-O" : "";

        String ambiguityResolution = resolveAmbiguity(sourceSquare, destSquare);
        
        String mainComponent =  (kingSideCastle.isEmpty()) ? 
                                    ((queenSideCastle.isEmpty()) ?
                                        ( pieceCode  + ambiguityResolution + captureCode + destSquareCode.toLowerCase())
                                    : queenSideCastle ) 
                                : kingSideCastle;



        

        algebraic += mainComponent;
    }




    /** 
     * Only to be called once check and checkmate have been established
     * 
      */
    public void setSuffix(){

        String checkCode = (isCheck) ? "+" : "";
        String checkMateCode = (isCheckMate) ? "++" : "";
        String checkMateSuffix = ((isCheckMate) ? checkMateCode : checkCode);

        algebraic +=  promotionChoice + checkMateSuffix + enPassantCode;
    }

    static String getTimestamp() {
        return LocalDateTime.now().format( FORMATTER );
    }

    
}