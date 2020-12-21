package runAndData;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.List;

public class FileCarer {

    static public void readFile(String fileName){
        JSONParser parser = new JSONParser();
        try (Reader reader = new java.io.FileReader(fileName)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            GlobalVariables.height = ((Long)jsonObject.get("height")).intValue();
            GlobalVariables.width = ((Long)jsonObject.get("width")).intValue();
            GlobalVariables.startEnergy = ((Long)jsonObject.get("startEnergy")).intValue();
            GlobalVariables.moveEnergy = ((Long)jsonObject.get("moveEnergy")).intValue();
            GlobalVariables.plantEnergy = ((Long)jsonObject.get("plantEnergy")).intValue();
            GlobalVariables.jungleRatio = (Double)jsonObject.get("jungleRatio");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    static public void writeData(String fileName, List<String> lines) throws IOException {
        Files.write(Paths.get(fileName),lines,StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
