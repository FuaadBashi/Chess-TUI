package ws.aperture.chess.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  The Monitor class could follow the singleton pattern: https://www.baeldung.com/java-singleton.
 *  It will store global data structures such as the leaderboard
 *  
 */
public final class Monitor {

    private HashMap<Player, ArrayList<Game>> gameHistoryMap;
    private HashMap<Player, ArrayList<Game>> ongoingGamesMap;
    private ArrayList<Player> leaderboard;       /* Lock and update when a game is played, then reorder */
    private ArrayList<Player> dumbLeaderboard;

}
