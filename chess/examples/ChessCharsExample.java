package ws.aperture.chess.examples;

/*

    "\u2654 " + // white king
    "\u2655 " + // white queen
    "\u2656 " + // white rook
    "\u2657 " + // white bishop
    "\u2658 " + // white knight
    "\u2659 " + // white pawn
    "\n" +
    "\u265A " + // black king
    "\u265B " + // black queen
    "\u265C " + // black rook
    "\u265D " + // black bishop
    "\u265E " + // black knight
    "\u265F " + // black pawn

*/ 
public class ChessCharsExample {
    public static void runExample() {
        final String BLACK_KING = "\u2654";
        final String WHITE_KING = "\u265A";

        final String BLACK_ROOK = "\u2656";
        final String WHITE_ROOK = "\u265C";
        

        System.out.println(WHITE_KING);
        System.out.println(BLACK_KING);
        
        System.out.println(WHITE_ROOK);
        System.out.println(BLACK_ROOK);
    }
}