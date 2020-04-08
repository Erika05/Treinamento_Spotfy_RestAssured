import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Treinamento {
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String token = "BQDmUQUJnlTLDdIaYK-vUOJUMQ9vGiIPSiagtaA-O8o8urlk3o7Fq52JGgFbsxxRrrMrm5MRH_IPwgXLD5xl41VeB-boXMwj1587JcxgT5N6O4cxQvRZ2FuiFJO1_qCAKHaeZtJ0Y5MWpnjf_FJzoCS_UU1BSU9NzzvehWljtGIPmsLIbDha5RDNIJIlB_4C4GVKCOB9yYkQgKNrOIJpqIx5HJpMRys1P2YvcGDEpPV_flYFFEVLHAj4WmXbwyFd1M6HAKjKODt-kTw-NjYn7ExvQ6lFvxCWUZaIYA";
    public static String URL = "https://api.spotify.com/v1";
    public static  String nomeMuscia = "Please Mr. Postman";
    public static  String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static  String nomePlayList = "Teste Postman";
    public static String nameArtist = "The Marvelettes";
    public static Response response;
    public static String idPlayList;
    public static String idNomeMusica;
    public static ArrayList  list;
    public static int index = -1;

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
        if(index > 0) {
            response = given()
                    .accept("application/json")
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .body("{\"uris\":[\"spotify:track:6wfK1R6FoLpmUA9lk5ll4T\"]}")
                    .when()
                    .post(URL + "/playlists/" + idPlayList + "/tracks")
                    .then()
                    .extract()
                    .response();
            if (response.statusCode() != 401) {
                assertEquals(response.statusCode(), 201);
                buscaNomeMusicaRequest();
                list = response.path("items.track.name");
                assertTrue(verificaPorName(nomeMusciaProcura));
            } else {
                System.out.println("Usuário não está logado!");
            }
        }
        else {
            System.out.println("Música já cadastrada!");
        }
    }

    @Test
    public void adicionaMusicaArquivoJson() {
        File json = new File("src/arquivosJson/adicionarMusica.json");
        buscaPlayList();
        response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(json)
                .when()
                .post(URL + "/playlists/" + idPlayList + "/tracks")
                .then()
                .extract()
                .response();
        if (response.statusCode() != 401) {
            assertEquals(response.statusCode(), 201);
            buscaNomeMusicaRequest();
            list = response.path("items.track.name");
            assertTrue(verificaPorName(nomeMusciaProcura));
        } else {
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
}

