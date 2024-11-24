package ws.aperture.chess.examples;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.TimeUnit;    // for sleeping

public class DateTimeExample {
    public static void runExample() {

        /* for(int i = 0; i < 8; i++) {
            System.out.println( (char) (i + 'A'));
            
        }
        return; */

        LocalDateTime before = LocalDateTime.now();

        try {
        TimeUnit.SECONDS.sleep( 3 );
        } catch ( InterruptedException ie ) {
            System.out.println( "Clock interrupted" );
        }

        LocalDateTime after = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM );

        System.out.println( before.format( formatter ) );
        System.out.println( after.format( formatter ) ); 

        


    }
}