package debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


//code from https://chess.stackexchange.com/questions/34687/unable-to-communicate-with-uci-through-java-program
public class Stockfish {
    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;
    private static final String Path = "fish/stockfish.exe";

    public boolean startEngine() {
        try {
            engineProcess = Runtime.getRuntime().exec(Path);
            processReader = new BufferedReader(new InputStreamReader(
                    engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(
                    engineProcess.getOutputStream());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutput() throws IOException, InterruptedException {

        return getOutput(1000);
    }

    public String getOutput(int waitTime) throws IOException, InterruptedException {
        StringBuffer buffer = new StringBuffer();

        Thread.sleep(waitTime);
        sendCommand("isready");
        while (true) {
            String text = processReader.readLine();
            if (text.equals("readyok")) {
                break;
            }
            else if(!text.isEmpty()){
                buffer.append(text + "\n");
            }
        }
        return buffer.toString().trim();
    }



        public void stopEngine() {
            try {
                sendCommand("quit");
                processReader.close();
                processWriter.close();
            } catch (IOException e) {
            }
        }

    }
