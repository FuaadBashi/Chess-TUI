package ws.aperture.chess.model.Logs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LogServer {

    static final int PORTNUMBER = 6666;
    private ServerSocket serverSocket;
    private LogClientThread client;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private String outputLine;
    private int moveCount;


    public void start() {
        try {
            serverSocket = new ServerSocket(PORTNUMBER);
            Socket connection = serverSocket.accept();
            outputStream = connection.getOutputStream();
            printWriter = new PrintWriter(outputStream, true);
            client = new LogClientThread(this , connection);
            Thread thread = new Thread(client);
            moveCount = 0;
            thread.start();
      
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void processMove(String moveLog) {
        moveCount++;

        int padSpaces = 8 - moveLog.length();
        moveLog += " ".repeat(padSpaces);

        if (moveCount % 2 == 1) {
            outputLine = moveLog + "    ";

        } else {
            outputLine += moveLog;
            printWriter.println(outputLine);
        }
        

    }


    public void closeAll() throws IOException {
        outputStream.close();
        printWriter.close();
        client.closeAll();
        serverSocket.close();

    }
}

