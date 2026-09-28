package ws.aperture.chess.model.logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/** Echoes anything the connected viewer sends back to the game's console. */
public class LogClientThread implements Runnable {

    private final Socket connection;

    public LogClientThread(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            // readLine returns null when the viewer disconnects; looping on it printed "null"
            // forever.
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // Connection closed at game end.
        }
    }

    public void closeAll() throws IOException {
        connection.close();
    }
}
