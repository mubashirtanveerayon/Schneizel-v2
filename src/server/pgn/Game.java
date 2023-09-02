package server.pgn;

import java.util.ArrayList;

public class Game {

    public String opening,result,sanMoveText;
    public Game(String gameText){
        String[] parts = gameText.split("~");
        opening = parts[0];
        result = parts[1];
        sanMoveText = parts[2];
    }

}
