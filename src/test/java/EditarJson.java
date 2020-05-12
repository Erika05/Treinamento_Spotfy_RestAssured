import io.restassured.response.Response;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
@Ignore
public class EditarJson {
    public static String token = "BQCWM8Cgr-wEuE3ib6VoWX3FqAlA3pywFbvn-wt7IVWoDyeJDWfuJ0sQMsIxmFkjxcv2mEM-cGO-e_7QsGIBDHxAv3p5-P0J1dVq9u8mHnQ0WGVvyJgItbqSxEE6aEd_UaXZRyeUfHIll1YRgUtjBhAI5HNNOB_j_-mvol1HW9tfqlepKxSv-KlXUdNkednD37za3WjQRfGOQCeOTMOdkROZ41h5Uv_jovgV1hMYsp2bvg0u7XZzHQZe-SvN7IWYry59EafPhb3SJ8aTAmoiFE4xClXV4j7YBKPhTA";
    public static String URL = "https://api.spotify.com/v1";
    public static String nomeMuscia = "Please Mr. Postman";
    public static String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static String nomePlayList = "Teste Postman";
    public static String nameArtist = "The Marvelettes";
    public static Response response;
    public static String idPlayList;
    public static String idNomeMusica;
    public static ArrayList list;
    public static int index;

//    @Test
//    public void adicionaMusicaAlteradoArquivoJson() {
//        JSONObject json = lerArquivoJson.LerJson("src/arquivosJson/adicionarMusica.json");
//        json.put("x", "s");
//        String x = json.toString();
//        buscaPlayList();
//          verificaPorName(nomeMusciaProcura);
//        response = given()
//                .accept("application/json")
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + token)
//                .body(json)
//                .when()
//                .post(URL + "/playlists/" + idPlayList + "/tracks")
//                .then()
//                .extract()
//                .response();
//        if (response.statusCode() != 401) {
//            assertEquals(response.statusCode(), 201);
//            buscaNomeMusicaRequest();
//            list = response.path("items.track.name");
//            assertTrue(verificaPorName(nomeMusciaProcura));
//        } else {
//            System.out.println("Usuário não está logado!");
//        }
//    }
}