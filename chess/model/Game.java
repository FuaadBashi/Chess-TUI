package ws.aperture.chess.model;

import ws.aperture.chess.model.Logs.LogServer;
import ws.aperture.chess.model.pieces.*;
import ws.aperture.chess.utilities.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;



public class Game {

    private Map<Side, Player> players;
    private Player winner, checkPlayer, checkMatePlayer;

    private Side turn;
    private Pawn promotionPawn;

    private Board board;

    public int counter; 

    private LogServer logServer;

    //private ArrayList<Move> moveLogBad; // how is a queen side castle stored in a move log?
    private ArrayList<String> moveLog;
    private LocalDateTime beginDateTime; // Need to add White and Black timers, add times to move logs 
    private Move currentMove;

    public Game(Player p1, Player p2) {
        players = new HashMap<Side, Player>();
        players.put(Side.WHITE, p1);
        players.put(Side.BLACK, p2);
        turn = Side.WHITE;
        checkPlayer = null;
        promotionPawn = null;
        currentMove = null;

        counter = 0;

        board = new Board();
        moveLog = new ArrayList<String>();
        beginDateTime = LocalDateTime.now();
        checkMatePlayer = null;
        winner = null;
        checkPlayer = null;

        logServer = new LogServer();
        logServer.start();

        calcMovesAttacks();
    }

    public boolean isDone() {
        return checkMatePlayer != null;
    }

    private void setCheckPlayer(Side side) {
        checkPlayer = players.get(side);
    }

    private void checkEscaped() {
        checkPlayer = null;
    }

    public Player getCheckPlayer() {
        return checkPlayer;
    }

    public boolean isCheck() {
        return checkPlayer != null;
    }

    public Player getCheckMatePlayer() {
        return checkMatePlayer;
    }

    public Pawn getPromotionPawn() {
        return promotionPawn;
    }

    public void clearPromotionPawn() {
        promotionPawn = null;
    }

    public void changePlayerTurn() {
        turn = Side.otherSide(turn);
    }

    public Side getPlayerTurn() {
        return turn;
    }

    public String getPlayerName(Side side) {
        return players.get(side).getName();
    }

    public String getPlayerTurnName() {
        return players.get(turn).getName();
    }
    public void setWinner(Side side){
        winner = players.get(side);
    }

    public Player getWinner() {
        setCheckPlayer(getPlayerTurn());
        setWinner(Side.otherSide(getPlayerTurn()));
        return winner;
    }


    public boolean move(Piece piece, Square destSquare) {
        // TODO create new move object and log move
        Square sourceSquare = piece.getSquare();

        currentMove = new Move(sourceSquare, destSquare, false);


        boolean castling = false;
        boolean promotion = false;

        if (piece instanceof King) {

            char row_idx = (turn == Side.WHITE) ? '1' : '8';

            
            Square kingsRookDestSquare = getSquare("F" + row_idx);
            if ( destSquare == getSquare("G" + row_idx) && piece.getSquare() == getSquare("E" + row_idx)) {
                castling = true;
                board.move(board.getKing(turn), destSquare );
                board.move(board.getPiece("H" + row_idx), kingsRookDestSquare );

                // destSquare.clearProspectiveMovers();
                // kingsRookDestSquare.clearProspectiveMovers();

            }
            

            Square queensRookDestSquare = getSquare("D" + row_idx);
            if ( destSquare == getSquare("C" + row_idx) && piece.getSquare() == getSquare("E" + row_idx)) {
                castling = true;
                board.move(board.getKing(turn), destSquare);
                board.move(board.getPiece("A" + row_idx), queensRookDestSquare);

                // destSquare.clearProspectiveMovers();
                // queensRookDestSquare.clearProspectiveMovers();
            }
            

        } else if (piece instanceof Pawn)  {
            
            final int promotionRow = (turn == Side.WHITE) ? 7 : 0;
            final int pieceRow     = (turn == Side.WHITE) ? 1 : 6;
            final int destRow      = (turn == Side.WHITE) ? 3 : 4;
            final int leftDelta    = (turn == Side.WHITE) ? -1 : 1;
            final int rightDelta   = (turn == Side.WHITE) ? 1 : -1;

            if (destSquare.getRow() == promotionRow) {
                promotion = true;
                promotionPawn = (Pawn) piece;
            }

            if ( destSquare.getRow() == destRow && piece.getRow() == pieceRow ) {
                Square leftNeighbourSquare  = getSquare( destSquare.getRow(), destSquare.getCol() + leftDelta );
                Square rightNeighbourSquare = getSquare( destSquare.getRow(), destSquare.getCol() + rightDelta );

                
                if (leftNeighbourSquare != null) {
                        
                    if ( leftNeighbourSquare.isOccupied() ) {
                        Piece leftNeighbour = leftNeighbourSquare.getPiece();
                        if ( leftNeighbour.getSide() == Side.otherSide(turn) && leftNeighbour instanceof Pawn ) {
                           Pawn attacker = (Pawn) leftNeighbour;
                           attacker.setEnPassantAttackee( piece );
    
                        } 
                    }
                }  
                
                if (rightNeighbourSquare != null) {
                    
                    if ( rightNeighbourSquare.isOccupied() ) {
                        Piece rightNeighbour = rightNeighbourSquare.getPiece();
                        if ( rightNeighbour.getSide() == Side.otherSide(turn) && rightNeighbour instanceof Pawn ) {
                            Pawn attacker = (Pawn) rightNeighbour;
                            attacker.setEnPassantAttackee( piece );    
                        }
                    }
                }
            }
        }
        
        if (!castling) {
            board.move(piece, destSquare);
        }

     
    
        return promotion;

    }

    public void processPromotionChoice(char c) {
        
        currentMove.setPromotionChoice(c);
        

        Piece newPiece = Piece.promote(promotionPawn, c);
        Square s = promotionPawn.getSquare();



        board.getPieces(turn).remove(promotionPawn);
        board.getPieces(turn).add(newPiece);
        s.removePiece();
        s.placePiece(newPiece);
    }

    public void endTurn() {

        checkCheck();
        changePlayerTurn();
        calcMovesAttacks();
        checkCheckMate();
        
        currentMove.setSuffix();
        
        String log = currentMove.getAlgebraicNotation();
        
        moveLog.add( log );

        logServer.processMove( log );
        

        counter++;
        if (isDone()){
            logGame();
        }
    }

    public void logGame() {
        // String path = "/Users/felix/Documents/aperture/chess-app/artefacts/gamelogs/first.log";
        String path = "/Users/fuaad/aperture/chess-app/artefacts/gamelogs/first.log";
        File file = new File( path );
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try ( FileWriter fw = new FileWriter(path) ) {



            for (int i = 0, j = 1; j < moveLog.size() ; i+=2, j+=2) {
                
                String whiteMove = moveLog.get(i);
                String blackMove = moveLog.get(j);

                fw.write(whiteMove + "    " + blackMove + '\n');


            }

            int size = moveLog.size();
            if (size % 2 == 1) {
                String whiteMove = moveLog.get( size - 1 );
                fw.write(whiteMove);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (counter % 2 == 1){
            logServer.processMove("");      
        }

        try {
            logServer.closeAll();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        

    }

    public boolean attack(Piece piece, Square destSquare) {
        // create new move object and log move
        boolean isPromotion = false;
        Square sourceSquare = piece.getSquare();

        currentMove = new Move(sourceSquare, destSquare, true);

        if (turn == Side.WHITE) {
            if (piece instanceof Pawn) {
                if (destSquare.getRow() == 7) {
                    isPromotion = true;
                    promotionPawn = (Pawn) piece;
                }
            }
        } else {
            if (piece instanceof Pawn) {
                if (destSquare.getRow() == 0) {
                    isPromotion = true;
                    promotionPawn = (Pawn) piece;
                }
            }
        }

        board.attack(piece, destSquare);

        return isPromotion;
        
    }

    /**
     *  Removes moves or attacks which put the player in check
     */
    private void calcMovesAttacks() {

        for (int i = 0; i < Board.NUM_ROW_COL; i++){
            for (int j = 0; j < Board.NUM_ROW_COL; j++){
                Square currentSquare = board.getSquare(i, j);
                currentSquare.clearProspectiveMovers();
            }
        }
        for (Piece piece : board.getPieces(turn)) {
            final Pair<List<Square>, List<Square>> uncheckedMovesAttacks = piece.getUncheckedMovesAttacks();
            final List<Square> moves    = uncheckedMovesAttacks.getFirst();
            final List<Square> attacks  = uncheckedMovesAttacks.getSecond();

            /* https://stackoverflow.com/a/60859469 */
            moves.removeIf(
                sq -> Board.selfChecked( Board.commitMove( board, piece.getRow(), piece.getCol(), sq.getRow(), sq.getCol(), true ), turn ) );

            attacks.removeIf(
                sq -> Board.selfChecked( Board.commitMove( board, piece.getRow(), piece.getCol(), sq.getRow(), sq.getCol(), false ), turn ) );

        

            if (piece instanceof King) {
                King king = (King) piece;
                Side kingSide = king.getSide();
                List<Square> castlingMoves = new ArrayList<>();
                if ( board.canCastleKS(kingSide) ) {
                    castlingMoves.addAll( getCastlingMoves( king, kingSide, true ) );
                }

                if ( board.canCastleQS(kingSide) ) {
                    castlingMoves.addAll( getCastlingMoves( king, kingSide, false ) );
                }
               
                moves.addAll(castlingMoves);
            }

            piece.setLegalMoves( moves );

            for ( Square destSquare : moves ) {
                destSquare.addProspectiveMover( piece );
            }
            

            piece.setLegalAttacks( attacks );

            for ( Square destSquare : attacks ) {
                destSquare.addProspectiveMover( piece );
            }
        }
    }

    List<Square> getCastlingMoves(King king, Side side, boolean isKingSideCastle) {

        List<Square> castleMoves = new ArrayList<>();
        char row           = (turn == Side.WHITE) ? '1' : '8';
        char firstStepCol  = (isKingSideCastle) ? 'F' : 'D';
        char secondStepCol = (isKingSideCastle) ? 'G' : 'C';
        char rookStepCol   = (isKingSideCastle) ? 'H' : 'A';


        
        Board halfWay = Board.commitMove(board, king.getRow(), king.getCol(),
                        getSquare( "" + firstStepCol + row ).getRow(),
                        getSquare("" + firstStepCol + row ).getCol(), true);

        if (!Board.selfChecked(halfWay, turn)) {
            Board fullway = Board.commitMove(halfWay,
                getSquare("" + firstStepCol + row ).getRow(),
                getSquare("" + firstStepCol + row ).getCol(),
                getSquare("" + secondStepCol + row).getRow(),
                getSquare("" + secondStepCol + row).getCol(), true);

            fullway = Board.commitMove(fullway,
                getSquare("" + rookStepCol + row).getRow(),
                getSquare("" + rookStepCol + row).getCol(),
                getSquare("" + firstStepCol + row ).getRow(),
                getSquare("" + firstStepCol + row ).getCol(), true);

            if (!Board.selfChecked(fullway, turn)) {
                castleMoves.add( getSquare("" + secondStepCol + row) );
            }
        }
        
        return castleMoves;
    }

    /** 
     * Called after a move or attack, but before the turn changes. 
     */
    private void checkCheck() {
        Side enemySide = Side.otherSide(turn);
        Square enemyKingSquare = board.getKingSquare(enemySide);
        for (Piece p : board.getPieces(turn)) {
            List<Square> attacks = p.getUncheckedAttacks();
            if (attacks.contains(enemyKingSquare)) {
                setCheckPlayer(enemySide);
                currentMove.setCheck();
                return;
            }
        }

        checkEscaped();
    }
    /** 
     * Called after the turn changes and calcMoves. 
     */

    public void checkCheckMate() {
        for (Piece p : board.getPieces(turn)) {
            if (p.hasLegalMovesAndAttacks()){
                checkMatePlayer = null;
                return;
            } else {
                checkMatePlayer = players.get(turn);
                
            }
        }
        currentMove.setCheckMate();
    }

   /**
    *   Creates a new Board object as a 'copy' of this Board.
    *  
    *
    * @param oldPiece          The Piece which will move or attack.
    * @param oldDestSquare     The destination Square of the move or attack.
    * @return                  True if this move puts the Player in check, False otherwise.
    */
    

    public Square getSquare(String squareCode) {
        return board.getSquare(squareCode);
    }
    

    public Board getBoard() {
        return board;
    }


    public LocalDateTime getBeginDateTime() {
        return beginDateTime;
    }


    public static int getNumRowsCols() {
        return Board.NUM_ROW_COL;
    }


    public Side getSide(Piece piece) {
        return piece.getSide();
    }

    /**   
     * 
     * @param square      The Squa
     * @return
     */
    public boolean isOccupied(Square square) {
        return square.isOccupied();
    }

    public List<Square> getLegalMoves(Piece piece) {
        return piece.getLegalMoves();
    }

    public List<Square> getLegalAttacks(Piece piece) {
        return piece.getLegalAttacks();
    }

    public Square getSquare(int row, int col) {
        return board.getSquare(row, col);
    }

    public List<Piece> getWhiteCaptured() {
        return board.getWhiteCaptured();
    }

     public List<Piece> getBlackCaptured() {
        return board.getBlackCaptured();
    }
}