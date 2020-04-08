import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class teste {
    public static String token = "BQCWM8Cgr-wEuE3ib6VoWX3FqAlA3pywFbvn-wt7IVWoDyeJDWfuJ0sQMsIxmFkjxcv2mEM-cGO-e_7QsGIBDHxAv3p5-P0J1dVq9u8mHnQ0WGVvyJgItbqSxEE6aEd_UaXZRyeUfHIll1YRgUtjBhAI5HNNOB_j_-mvol1HW9tfqlepKxSv-KlXUdNkednD37za3WjQRfGOQCeOTMOdkROZ41h5Uv_jovgV1hMYsp2bvg0u7XZzHQZe-SvN7IWYry59EafPhb3SJ8aTAmoiFE4xClXV4j7YBKPhTA";
    public static String URL = "https://api.spotify.com/v1";
    public static  String nomeMuscia = "Please Mr. Postman";
    public static  String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static  String nomePlayList = "Teste Postman";
    public static String nameArtist = "The Marvelettes";
    public static Response response;
    public static String idPlayList;
    public static String idNomeMusica;
    public static ArrayList  list;
    public static int index;

    @Test
    public void buscaPlayList()
    {
         response =  given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL + "/me/playlists")
                .then()
                .extract()
                .response();
         if(response.statusCode() != 401) {
             assertEquals(response.statusCode(), 200);
             list = response.path("items.name");
             assertTrue(verificaPorName(nomePlayList));
             idPlayList = response.path("items[" + index + "].id");
         }
         else
         {
             System.out.println("Usuário não está logado!");
         }
    }

    @Test
    public void buscaNomeMusica()
    {
        buscaPlayList();
//        response =  given()
//                .accept("application/json")
//                .contentType("application/json")
//                .header("Authorization", "Bearer " + token)
//                .when()
//                .get(URL + "/playlists/" + idPlayList + "/tracks")
//                .then()
//                .extract()
//                .response();
        buscaNomeMusicaRequest();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            list = response.path("items.track.name");
            assertTrue(verificaPorName(nomeMuscia));
            idNomeMusica = response.path("items[" + index + "].track.id");
        }
        else
        {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void buscaArtista()
    {
        buscaNomeMusica();
        response =  given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL + "/tracks/" + idNomeMusica)
                .then()
                .extract()
                .response();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            list = response.path("artists.name");
            assertTrue(verificaPorName(nameArtist));
        }
        else
        {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void adicionaMusica()
    {
        buscaPlayList();
        response =  given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{\"uris\":[\"spotify:track:6wfK1R6FoLpmUA9lk5ll4T\"]}")
                .when()
                .post(URL + "/playlists/" + idPlayList +"/tracks")
                .then()
                .extract()
                .response();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 201);
            buscaNomeMusicaRequest();
            list = response.path("items.track.name");
            assertTrue(verificaPorName(nomeMusciaProcura));
        }
        else
        {
            System.out.println("Usuário não está logado!");
        }
    }

    public void buscaNomeMusicaRequest()
    {
         response =  given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL + "/playlists/" + idPlayList + "/tracks")
                .then()
                .extract()
                .response();
         list = response.path("items.track.name");
    }

    public Boolean verificaPorName(String nome) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(nome))
            {
                index = i;
                return  true;
            }
        }
        return false;
    }

//    @Test
//    public void adicionaMusicaAlteradoArquivoJson() {
//        JSONObject json = lerArquivoJson.LerJson("src/arquivosJson/adicionarMusica.json");
//        json.put("x", "s");
//        String x = json.toString();
//        buscaPlayList();
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

//    @Test
//    public void BuscaPlayList()
//    {
//                given()
//                        .accept("application/json")
//                        .contentType("application/json")
//                        .header("Authorization", "Bearer " + token)
//                        .when()
//                        .get(URL + "/me/playlists")
//                        .then()
//                        .statusCode(200);
//    }

//    public Boolean verificaPlayList(String playList) {
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).equals(playList))
//          {
//              idPlayList = response.path("items["+i+"].id");
//              return  true;
//          }
//        }
//        return false;
//        }

//    public Boolean verificaMusica(String nomeMuscia) {
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).equals(nomeMuscia))
//            {
//                idNomeMusica = response.path("items["+i+"].track.id");
//                return  true;
//            }
//        }
//        return false;
//    }
}
