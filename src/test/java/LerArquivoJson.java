import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LerArquivoJson {

 public JSONObject json = null;

    public JSONObject LerJson(String caminhoArquivo ){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(caminhoArquivo));
            json = (JSONObject) obj;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


}
