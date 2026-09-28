package ws.aperture.chess.model;

import ws.aperture.chess.view.tui.TUI;

/** Entry point: starts the terminal interface. */
public class App {
    public static void main(String[] args) {
        TUI tui = new TUI();
        tui.start();
    }
}
