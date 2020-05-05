import io.restassured.response.Response;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import java.util.ArrayList;
import java.util.Base64;
import static  org.junit.Assert.assertThat;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class Helper {
    public static  String URL = "https://api.spotify.com/v1";
    public static  String token;
    public  static  int index;
    public static  Response response;
    public static ArrayList list;
    public static  String idPlayList;
    public static String client_id = "507cc87c01f24b7ba331d3d616404eba";
    public static String client_secret = "131c0d2189424c37ae0d39fdc55069ec";


    public void gerarToken()
    {
        String client_credentials = Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes());

        response = given()
                .formParam("grant_type", "refresh_token")
                .formParam("refresh_token", "AQC86RkjqfXwUCHcwF2hVWrzankEXqm7FGc6NdOdFBRgf3zhg49ZVwJDEcHXHL83tK1eUyVNWhbPKUBmGfwQ0PqmSqP2I77x3gNaeFxTu9dzHtk_W9DgPvfu7M4lZsqgM4Y")
                .header("Authorization", "Basic " + client_credentials)
                .post("https://accounts.spotify.com/api/token")
                .then()
                .extract()
                .response() ;
        assertEquals(response.statusCode(), 200);
        assertThat(response.statusCode(), anyOf(is(200), is(204)));
        token = response.path("access_token");
    }

    public static Boolean verificaPorName(String nome) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(nome))
            {
                index = i;
                return  true;
            }
        }
        return false;
    }

    public static void x() {
        index = -1;
        for (Object s : list) {
           index++;
        }
    }
}
