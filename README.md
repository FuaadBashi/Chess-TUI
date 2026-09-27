# Chess — Java Terminal Interface

A Java chess project organized around a board model, piece classes, and a terminal interface. The repository also includes logging components and small utility examples.

## Run locally

Use JDK 17 or later. From a POSIX shell:

```bash
git clone https://github.com/FuaadBashi/Chess-TUI.git
cd Chess-TUI
mkdir -p out
find chess -name '*.java' > sources.txt
javac -d out @sources.txt
java -cp out ws.aperture.chess.model.App
```

## Code to explore

| Area | Entry point |
| --- | --- |
| Application | [App.java](chess/model/App.java) |
| Game state | [Game.java](chess/model/Game.java), [Board.java](chess/model/Board.java) |
| Piece behavior | [pieces](chess/model/pieces) |
| Terminal interface | [TUI.java](chess/view/TUI/TUI.java) |
| Logging | [Logs](chess/model/Logs) |

Use the model and piece implementations to inspect individual rule handling. This overview does not establish complete chess-rule coverage or automated validation of special moves.
