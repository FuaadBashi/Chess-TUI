package ws.aperture.chess.model.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LogClientThread implements Runnable {

    private LogServer logServer;
    private Socket connection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private PrintWriter writer;


    public LogClientThread(LogServer logServer, Socket connection) {
        this.logServer = logServer;
        this.connection = connection;

     
                
    }


@Override
    public void run(){
        try {
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(System.out);
            for(;;){

                String moveLog = bufferedReader.readLine();
                writer.println(moveLog);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }   
    
    public void closeAll() throws IOException {
        inputStream.close();
        bufferedReader.close();
        writer.close();
        connection.close();

    }
    

    

}
