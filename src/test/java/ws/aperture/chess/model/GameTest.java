package ws.aperture.chess.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ws.aperture.chess.model.pieces.Piece;

class GameTest {

    @TempDir Path logDir;

    private Game game;

    @BeforeEach
    void newGame() {
        game = new Game(new Player("Alice"), new Player("Bob"));
        game.setLogDirectory(logDir);
    }

    private Piece pieceOn(String square) {
        return game.getSquare(square).getPiece();
    }

    private void play(String from, String to) {
        Piece piece = pieceOn(from);
        Square dest = game.getSquare(to);
        if (game.getLegalAttacks(piece).contains(dest)) {
            game.attack(piece, dest);
        } else {
            assertTrue(game.getLegalMoves(piece).contains(dest), from + to + " should be legal");
            game.move(piece, dest);
        }
        game.endTurn();
    }

    @Test
    void whiteMovesFirst() {
        assertEquals(Side.WHITE, game.getPlayerTurn());
        assertEquals("Alice", game.getPlayerTurnName());
    }

    @Test
    void aPawnOnItsStartingSquareCanAdvanceOneOrTwoSquares() {
        List<Square> moves = game.getLegalMoves(pieceOn("E2"));

        assertEquals(2, moves.size());
        assertTrue(moves.contains(game.getSquare("E3")));
        assertTrue(moves.contains(game.getSquare("E4")));
    }

    @Test
    void aKnightCanJumpOverPiecesOnTheOpeningMove() {
        List<Square> moves = game.getLegalMoves(pieceOn("B1"));

        assertEquals(2, moves.size());
        assertTrue(moves.contains(game.getSquare("A3")));
        assertTrue(moves.contains(game.getSquare("C3")));
    }

    @Test
    void aBlockedRookHasNoMovesAtTheStart() {
        assertTrue(game.getLegalMoves(pieceOn("A1")).isEmpty());
    }

    @Test
    void theTurnPassesToBlackAfterWhiteMoves() {
        play("E2", "E4");

        assertEquals(Side.BLACK, game.getPlayerTurn());
        assertFalse(game.getSquare("E2").isOccupied());
        assertTrue(game.getSquare("E4").isOccupied());
    }

    @Test
    void foolsMateEndsTheGameWithWhiteCheckmatedAndWritesTheMoveLog() throws IOException {
        play("F2", "F3");
        play("E7", "E5");
        play("G2", "G4");
        play("D8", "H4");

        assertTrue(game.isDone());
        assertEquals("Alice", game.getCheckMatePlayer().getName());

        try (var logs = Files.list(logDir)) {
            Path log = logs.findFirst().orElseThrow();
            assertEquals(List.of("f3    e5", "g4    Qh4++"), Files.readAllLines(log));
        }
    }
}
