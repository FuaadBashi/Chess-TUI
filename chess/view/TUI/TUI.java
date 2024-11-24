package ws.aperture.chess.view.TUI;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import ws.aperture.chess.model.Game;
import ws.aperture.chess.model.Player;
import ws.aperture.chess.model.Side;
import ws.aperture.chess.model.Square;
import ws.aperture.chess.model.pieces.Piece;
import ws.aperture.chess.utilities.ANSIColour;

public class TUI {
    private static Game game;
    private Scanner scanner;
    private String  message;
    private String  sourceLoc, destLoc;

    private char    mode;
    private int     promptIndex;

    private final static ArrayList<String> promptMessages;

    static {
        promptMessages = new ArrayList<String>();
        promptMessages.add("        (V)iew moves and attacks    (M)ove piece       "); // 0
        promptMessages.add("        Enter location of piece:    ");
        promptMessages.add("        Enter destination:    ");
        promptMessages.add("        Invalid Location.     "); // 3
        promptMessages.add("        Moves are highlighted "
                + ANSIColour.ANSI_GREEN_BACKGROUND + "green" + ANSIColour.ANSI_RESET 
                + ", attacks are highlighted "
                + ANSIColour.ANSI_RED_BACKGROUND + "red" + ANSIColour.ANSI_RESET
                + ".    ");


        promptMessages.add("        No moves or attacks available"); 
        promptMessages.add("        Invalid destination."); // 6
        promptMessages.add("        Friendly fire will not be tolerated!");
        promptMessages.add("        Not your turn mate.");
        promptMessages.add("        Promote pawn to Q R B N:     "); //9
        promptMessages.add("        Invalid piece code"); //10
        promptMessages.add("          Congrats the Winner is: "); //11
    }


    public TUI() {
        scanner = new Scanner(System.in);
        promptIndex = 0;
        mode = 's'; /* s : start, v : view, m : move (source stage), d : move (destination stage) */
    }

    private void printErrorScreen(int errIndex) {
        promptIndex = errIndex;
        drawBoard();
        try {
            TimeUnit.SECONDS.sleep( 2 );
        } catch ( InterruptedException ie ) {
            System.out.println( "Clock interrupted" );
        }
        promptIndex = 0;
        mode = 's';
    }

    /**
     *     Called from App.java to begin the TUI process
     */
    public void start() {

        startScreen();        
        do {
            drawBoard();
            message = scanner.next();
            if ( mode == 's' ) {
                promptIndex = 0;
                
                if ( message.toLowerCase().equals("exit") ) {
                    game.logGame();
                    break;
                }

                if (   message.toLowerCase().equals("v")
                    || message.toLowerCase().equals("view")) {
                    promptIndex = 1;
                    mode        = 'v';
                    continue;
                } 
                
                if (   message.toLowerCase().equals("m")
                    || message.toLowerCase().equals("move")) {
                    promptIndex = 1;
                    mode        = 'm';
                    continue;
                }

                /* short move code */
                if ( message.length() == 4 ) {

                    sourceLoc = message.substring(0, 2).toLowerCase();
                    destLoc   = message.substring(2, 4).toLowerCase();

                    /* Source Square OK */
                    if ( game.getSquare(sourceLoc) == null ) {
                        printErrorScreen(3);
                        continue;
                    }

                    /* Dest Square OK */
                    if ( game.getSquare(destLoc) == null ) {
                        printErrorScreen(6);
                        continue;
                    }

                    Square sourceSquare = game.getSquare(sourceLoc);
                    Square destSquare   = game.getSquare(destLoc);

                    /* No Piece on Source Square */
                    if ( !sourceSquare.isOccupied() ) {
                        printErrorScreen(3);
                        continue;
                    }

                    /* Not your Piece on Source Square */
                    Piece subjectPiece = sourceSquare.getPiece();
                    if ( game.getSide(subjectPiece) != game.getPlayerTurn() ) {
                        printErrorScreen(8);
                        continue;
                    }

                    
                    
                    List<Square> moves   = game.getLegalMoves(subjectPiece);
                    List<Square> attacks = game.getLegalAttacks(subjectPiece);
                    
                    if (moves.contains(destSquare)) {
                        boolean isPromotion =  game.move(subjectPiece, destSquare);
                        if (isPromotion) {
                            promptIndex = 9;
                            mode = 'p';
                            continue;
                        } else {
                            game.endTurn();
                            promptIndex = 0;
                            mode = 's';
                            continue;
                        }
                       
                    }

                    if (attacks.contains(destSquare)) {
                        boolean isPromotion = game.attack(subjectPiece, destSquare);
                        if (isPromotion) {
                            promptIndex = 9;
                            mode = 'p';
                            continue;
                        } else {
                            game.endTurn();
                            promptIndex = 0;
                            mode = 's';
                            continue;
                        }
                        
                    }

                    printErrorScreen(6);
                    continue;
                }
            }            

            if ( mode == 'v' ) {
                String squareCode = message.toUpperCase();
                if (game.getSquare(squareCode) != null) {
                    Square square = game.getSquare(squareCode);
                    if (square.isOccupied()) {
                        Piece occupier = square.getPiece();
                        if (game.getSide(occupier) == game.getPlayerTurn()) {
                            drawBoard(occupier);
                            try {
                                TimeUnit.SECONDS.sleep( 3 );
                            } catch ( InterruptedException ie ) {
                                System.out.println( "Clock interrupted" );
                            }
                            promptIndex = 0;
                            mode = 's';
                            continue;
                        } else {
                            promptIndex = 8;
                            drawBoard();
                            try {
                                TimeUnit.SECONDS.sleep( 2 );
                            } catch ( InterruptedException ie ) {
                                System.out.println( "Clock interrupted" );
                            }
                            promptIndex = 0;
                            mode = 's';
                            continue;
                        }
                    }
                }
                promptIndex = 3;
                drawBoard();
                try {
                    TimeUnit.SECONDS.sleep( 2 );
                } catch ( InterruptedException ie ) {
                    System.out.println( "Clock interrupted" );
                }
                promptIndex = 0;
                mode = 's';
                continue;
            }
            if (mode == 'p') {
                message = message.toUpperCase();
                if (message.length() == 1) {
                    if ("QRBN".contains(message)) {
                        game.processPromotionChoice(message.charAt(0));
                        game.clearPromotionPawn();
                        game.endTurn();
                        
                        promptIndex = 0;
                        mode = 's';
                        continue;
                    }        
                }

                printErrorScreen(10);
                promptIndex = 9;
                continue;
            }

        } while (true);
    }

    /**
     *  Gets user input for names, creates Players and the Game.
     */
    private void startScreen() {
        clearScreen();
        String p1Name, p2Name;

        System.out.println("Enter white player name: ");
        p1Name = scanner.nextLine();

        System.out.println("Enter black player name: ");
        p2Name = scanner.nextLine();
        

        Player p1 = new Player(p1Name);
        Player p2 = new Player(p2Name);
        game = new Game(p1, p2);
    }

    private static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
    }

    /**
     *      Prints the board to the TUI display
     * @param piece     Focus piece for which we are drawing moves and attacks
     */
    public void drawBoard(Piece piece) {
        clearScreen();
        
        List<Square> moves = game.getLegalMoves(piece);
        List<Square> attacks = game.getLegalAttacks(piece);

        Square currSquare;
        Piece currPiece;

        String squareColour;
        String pieceColour;

        promptIndex = (moves.size() + attacks.size() == 0) ? 5 : 4;
        
        
        System.out.println("\n");
        for (int i = Game.getNumRowsCols() - 1; i >= 0; --i) {
            System.out.println(ANSIColour.ANSI_CYAN);
            System.out.print( "     " + String.valueOf(i + 1)  + "   ");    // Print row indices

            
            for (int j = 0; j < Game.getNumRowsCols(); j++) {
                currSquare = game.getSquare(i, j);

                /* Set Square Background Colour */
                if (moves.contains(currSquare)) {
                    squareColour = ANSIColour.ANSI_GREEN_BACKGROUND;
                } else if (attacks.contains(currSquare)) {
                    squareColour = ANSIColour.ANSI_RED_BACKGROUND;
                } else {
                    squareColour = "";
                }

                /* Set Piece Colour, if there is a Piece on this Square */
                currPiece = currSquare.getPiece();                
                if (currPiece != null) {
                    if (game.getSide(currPiece) == Side.WHITE) {
                        pieceColour = ANSIColour.ANSI_WHITE;
                    } else {
                        pieceColour = ANSIColour.ANSI_YELLOW;
                    }
                } else {
                    pieceColour = "";
                }
                
                /* Print each Square with formatted colours */ 
                if ( currPiece != null ) {
                    System.out.print( ANSIColour.ANSI_CYAN + "[" + squareColour + " " + pieceColour + currPiece + " " + ANSIColour.ANSI_CYAN + ANSIColour.ANSI_RESET + ANSIColour.ANSI_CYAN + "]");               
                } else {
                    System.out.print( ANSIColour.ANSI_CYAN + "[" + squareColour + "   "  + ANSIColour.ANSI_RESET + ANSIColour.ANSI_CYAN + "]");
                }

            }

            System.out.println(infoMessage(i));
        }
        
        System.out.println("\n           A    B    C    D    E    F    G    H\n\n\n");
        System.out.print(infoMessage(-1));
    }


    /**
     *      Draws the board without any moves or attacks highlighted.
     */
    public void drawBoard() {

        clearScreen();

        Square currSquare;
        Piece currPiece;
        
        String pieceColour;

        System.out.println("\n");

        // 
        for (int i = Game.getNumRowsCols() - 1; i >= 0; --i) { // rows
            System.out.println(ANSIColour.ANSI_CYAN);
            System.out.print( "     " + String.valueOf(i + 1)  + "   ");    // Print row indices

            
            for (int j = 0; j < Game.getNumRowsCols(); j++) {
                currSquare = game.getSquare(i, j);

                /* Set Piece Colour, if there is a Piece on this Square */
                currPiece = currSquare.getPiece();                
                if (currPiece != null) {
                    pieceColour = (game.getSide(currPiece) == Side.WHITE) ?
                                ANSIColour.ANSI_WHITE : ANSIColour.ANSI_YELLOW;
                } else {
                    pieceColour = "";
                }
                
                /* Print each Square with formatted colours */ 
                if ( currPiece != null ) {
                    System.out.print( ANSIColour.ANSI_CYAN + " [" + pieceColour + currPiece + ANSIColour.ANSI_CYAN + ANSIColour.ANSI_RESET + ANSIColour.ANSI_CYAN + "] ");               
                } else {
                    System.out.print( ANSIColour.ANSI_CYAN + " [" +  " "  + ANSIColour.ANSI_RESET + ANSIColour.ANSI_CYAN + "] ");
                }

                

            }

            System.out.println(infoMessage(i));
        }
        
        System.out.println("\n           A    B    C    D    E    F    G    H\n\n\n");
        System.out.print(infoMessage(-1));
    }

    /**     Returns a formatted message to be displayed
     *  
     *      @param row      The row index we want the message for.
     * 
     *  7 -> white name
     *  6 -> black name
     *  5 -> turn name
     *  4 -> check info e.g. "white is in check"
     *  2 -> Directive message e.g. "Type a Square code"
     * 
     *  -1 -> Prompt options (static)
     *  
     */
    private String infoMessage(int row) {
        switch (row) {
            case 7 :
                return "    white:    " + game.getPlayerName(Side.WHITE);
            case 6 :
                return "    black:    " + game.getPlayerName(Side.BLACK);
            case 5 :
                return "    turn:     " + game.getPlayerTurnName();
            case 3:
                return "    count:    " + game.counter;
            case 2 :
                String s = "";
                if ( game.isDone() ) {
                    s = "    " +  game.getCheckMatePlayer().getName() + " is in Checkmate";
                } else if ( game.isCheck() ) {
                    s = "    " +  game.getCheckPlayer().getName() + " is in Check";
                }  
                return s;
            case 1 :
                return "   " + graveYard(game.getWhiteCaptured());
            case 0 :
                return "   " + graveYard(game.getBlackCaptured());
            case -1 :
                return promptMessages.get(promptIndex);
            // case -2 :
            //     return promptMessages.get(11) + game.getWinner().getName();
            default:
                return "";
        }
    }

    /**
     * @param pieces        List of pieces in the graveyard.
     * @return              String representation of these pieces.
     */
    private String graveYard(List<Piece> pieces) {
        if (pieces.isEmpty()) {
            return "";
        }

        String graveyard = (pieces.get(0).getSide() == Side.WHITE) ? ANSIColour.ANSI_WHITE : ANSIColour.ANSI_YELLOW;
        
        for (Piece p : pieces) {
            graveyard += " " + p;
        }

        return graveyard + ANSIColour.ANSI_CYAN;
    }


    
}
