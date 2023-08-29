package server.pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import server.move.MoveManager;
import server.util.Constants;
import server.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {

    public static String cvtMove(String move,MoveManager mm){
        if(move.contains(Constants.QUEEN_SIDE_CASTLING)){
            return Constants.QUEEN_SIDE_CASTLING;
        }else if(move.contains(Constants.KING_SIDE_CASTLING)){
            return Constants.KING_SIDE_CASTLING;
        }



        return null;
    }

    public static ArrayList<HashMap<String,String>> parseFile(String path){
        ArrayList<HashMap<String,String>> games = new ArrayList<>();
        boolean parsingGame = false;
        HashMap<String,String> gameInfo = new HashMap<>();
        Pattern coordPattern = Pattern.compile("[a-h][1-8]");
        String moveText = "";
        for(String line:getContent(path)){
            String[] parts = line.split(" ");
            if(line.startsWith("[Event ")){
                parsingGame = true;
                gameInfo=new HashMap<>();
                moveText = "";
            }else if(!line.contains("\"") && parts[parts.length-1].contains("-")){
                parsingGame = false;
                moveText += line;
                gameInfo.put("Moves",moveText.trim());
                games.add(gameInfo);
            }
            if (parsingGame){
                if(line.startsWith("[")){
                    String infoLine = line.replace("[", "").replace("]","");
                    String[] infoSegments = infoLine.split("\"");
                    String key = infoSegments[0].trim();
                    String value = infoSegments[1].trim();
                    //System.out.println(key+": "+value);
                    gameInfo.put(key,value);
                }else if(Character.isDigit(line.charAt(0)) || parts[0].contains("O-O") || coordPattern.matcher(parts[0]).find()){
                    moveText += line+" ";
                }
            }
        }
        return games;
    }


    public static ArrayList<String> getContent(String filePath){
        ArrayList<String> content = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            for(String line;(line = reader.readLine()) != null;){
                if(!line.isEmpty()){
                    content.add(line.trim());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(content);
        return content;
    }
    public static String parseSAN(String san, MoveManager mm){
        ArrayList<String> allMoves = mm.getAllMoves();
        if (san.toUpperCase().contains(Constants.QUEEN_SIDE_CASTLING)){
            return Util.constructCastlingMove(Constants.QUEEN_SIDE_CASTLING,mm.cb.fenParts);
        }else if (san.toUpperCase().contains(Constants.KING_SIDE_CASTLING)){
            return Util.constructCastlingMove(Constants.KING_SIDE_CASTLING,mm.cb.fenParts);
        }else{
            Pattern coordPattern = Pattern.compile("[a-h][1-8]");

            Matcher matcher = coordPattern.matcher(san);
            String from = "",to="";
            while(matcher.find()){
                if(to.isEmpty()){
                    to = matcher.group();
                }else{
                    from = to;
                    to = matcher.group();
                }
            }
            for(String move:allMoves){
                if(move.contains(Constants.KING_SIDE_CASTLING)){
                    continue;
                }
                int index = Integer.parseInt(Character.toString(move.charAt(2))) + Integer.parseInt(Character.toString(move.charAt(3))) * 8;
                String dstCoord = Util.cvtCoord(index);
                if(dstCoord.equals(to)){

                    index = Integer.parseInt(Character.toString(move.charAt(0))) + Integer.parseInt(Character.toString(move.charAt(1))) * 8;
                    char piece = mm.cb.board[index/8][index%8];
                    if(Character.isUpperCase(san.charAt(0))){
                        if(Character.toUpperCase(piece) == san.charAt(0)) {
                            if (from.isEmpty()) {
                                String firstPart = san.split(to)[0];
                                if (firstPart.length()>1 && firstPart.charAt(1) != 'x'){
                                    if(Util.cvtCoord(index).contains(Character.toString(firstPart.charAt(1)))){
                                        return move;
                                    }
                                }else{
                                    return move;
                                }
                            } else {
                                if (index == Constants.FILES.indexOf(from.charAt(0)) + Constants.RANKS.indexOf(from.charAt(1)) * 8) {
                                    return move;
                                }
                            }
                        }
                    }else{
                        if(Character.toUpperCase(piece) == Constants.WHITE_PAWN){
                            if(san.contains("=")){
                                if(!move.contains(Constants.EN_PASSANT_NOTATION)&&!Character.isDigit(move.charAt(move.length()-1))){
                                    String[] moveParts = move.split(Constants.MOVE_SEPARATOR);
                                    if(Character.toUpperCase(moveParts[moveParts.length-1].charAt(0)) == san.charAt(san.indexOf("=")+1)){
                                        return move;
                                    }
                                }
                            }else if(index % 8 == Constants.FILES.indexOf(san.charAt(0))){
                                return move;
                            }
                        }
                    }

                }

            }
        }
        return null;
    }

    
    public static ArrayList<String> getMoveText(String filePath){
        ArrayList<String> moves = new ArrayList<>();
        Pattern coordPattern = Pattern.compile("[a-h][1-8]");
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = br.readLine())!=null){
                if(!line.isEmpty() && !line.startsWith("[") ){
                    for(String seg:line.split(" ")){
                        Matcher match = coordPattern.matcher(seg);
                        if(match.find() || seg.toUpperCase().contains(Constants.KING_SIDE_CASTLING)){
                            moves.add(seg);
                        }
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return moves;
    }


}
