import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Treinamento {
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String token = "BQAQUCKaAuZ_j3HUHSe1-bFL3e1c_Jra-QluHaoBM-8lq4t5M4oemfJmaY1jjAIp6iV1K8ikMMJdLb61j2Rta4Vk1UAesRldhD1Bf50d5Q33vQ86VRafHfXuRSitolm9HinYExcaJKLhDp71uf7vDZYVLpRd6blaHOXSjuuo2sHIRt43zRKVE0d6vfsSAzEyDyk_8jajAPsBy2cm99sTB-aJE8n7N9Tg8Ig1YAjmv6u3O7yf6xN-0Yj23Yib-OG0JfcG7i_dRZ69Q5_vUNBGXf3N3HZgrLbwrJ3f0g";
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
        if(verificaPorName(nomeMusciaProcura)){
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
        if(verificaPorName(nomeMusciaProcura)) {
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
        }else {
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
        index = 0;
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

