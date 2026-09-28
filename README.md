# Chess TUI

[![CI](https://github.com/FuaadBashi/Chess-TUI/actions/workflows/ci.yml/badge.svg)](https://github.com/FuaadBashi/Chess-TUI/actions/workflows/ci.yml)

Two-player chess in the terminal, written in Java. The engine validates every move, including
castling, en passant, promotion, check and checkmate, and records the game in algebraic notation.
You can also stream the move log live to a second terminal over TCP.

## Highlights

- **Full rule set.** Each piece generates candidate moves, then the game discards any that would
  leave the mover's own king in check. Castling is refused out of, through or into check. En
  passant and promotion are supported.
- **Polymorphic piece model.** Every piece extends an abstract `Piece` and supplies its own
  movement and attack rules.
- **Move highlighting.** Ask to *view* a piece and the board shows its legal moves in green and its
  captures in red.
- **Algebraic notation.** Moves are logged as `e4`, `Nf3`, `Qh4++` and so on, with disambiguation
  when two pieces of the same type can reach the same square. The log is saved to `game-logs/`
  when the game ends.
- **Live move feed.** A small TCP server on port 6666 streams the move log to any viewer that
  connects. It accepts connections on a background thread, so the game never waits for a viewer.

## Getting started

Requires JDK 17+ and Maven.

```bash
git clone https://github.com/FuaadBashi/Chess-TUI.git
cd Chess-TUI
mvn package
java -jar target/chess-tui.jar
```

Optionally, watch the move log from a second terminal:

```bash
nc localhost 6666
```

## How to play

| Input | Action |
| --- | --- |
| `e2e4` | Move the piece on e2 to e4 (captures work the same way) |
| `v`, then a square | Show a piece's legal moves and captures |
| `m` | Enter a move in two steps: source square, then destination |
| `Q` `R` `B` `N` | Choose the new piece when a pawn promotes |
| `exit` | Save the move log and quit |

## Project structure

```
src/main/java/ws/aperture/chess/
├── model/
│   ├── App.java        entry point
│   ├── Game.java       turns, check and checkmate, castling, en passant, promotion
│   ├── Board.java      8×8 grid and self-check detection
│   ├── Move.java       algebraic-notation formatting
│   ├── pieces/         abstract Piece and one class per piece type
│   └── logs/           TCP move-log server
├── view/tui/TUI.java   rendering and input handling
└── utilities/          ANSI colours, Pair
```

## Tests

```bash
mvn verify
```

This runs the JUnit suite and checks formatting with google-java-format. The suite covers opening
moves, turn order, and a full fool's-mate game that ends in checkmate and writes the expected move
log. Run `mvn spotless:apply` to fix formatting.
