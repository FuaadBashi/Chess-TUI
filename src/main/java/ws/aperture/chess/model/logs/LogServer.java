package ws.aperture.chess.model.logs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Streams the move log to an optional viewer connected on {@link #PORTNUMBER} (for example {@code
 * nc localhost 6666} in a second terminal).
 *
 * <p>Connections are accepted on a background thread. Accepting on the game thread blocked the game
 * at startup until a viewer connected, so a player without one never saw the board.
 */
public class LogServer {

    static final int PORTNUMBER = 6666;
    private static final int MOVE_COLUMN_WIDTH = 8;

    private ServerSocket serverSocket;
    private LogClientThread client;
    private PrintWriter printWriter;
    private String outputLine = "";
    private int moveCount;

    public void start() {
        try {
            serverSocket = new ServerSocket(PORTNUMBER);
        } catch (IOException e) {
            // The viewer is optional: a busy port must not stop the game from starting.
            System.err.println("Move-log viewer disabled: port " + PORTNUMBER + " unavailable.");
            return;
        }

        Thread acceptor = new Thread(this::acceptViewer, "log-server");
        acceptor.setDaemon(true);
        acceptor.start();
    }

    private void acceptViewer() {
        try {
            Socket connection = serverSocket.accept();
            synchronized (this) {
                printWriter = new PrintWriter(connection.getOutputStream(), true);
                client = new LogClientThread(connection);
            }
            Thread reader = new Thread(client, "log-client");
            reader.setDaemon(true);
            reader.start();
        } catch (IOException e) {
            // Socket closed at game end before any viewer connected.
        }
    }

    public synchronized void processMove(String moveLog) {
        moveCount++;

        String padded = String.format("%-" + MOVE_COLUMN_WIDTH + "s", moveLog);

        if (moveCount % 2 == 1) {
            outputLine = padded + "    ";
        } else {
            outputLine += padded;
            if (printWriter != null) {
                printWriter.println(outputLine);
            }
        }
    }

    public synchronized void closeAll() throws IOException {
        if (printWriter != null) {
            printWriter.close();
        }
        if (client != null) {
            client.closeAll();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
