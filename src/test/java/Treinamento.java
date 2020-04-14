import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class Treinamento {
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String token = "BQBbUgukBZ4i47HxJLky6ifyf1cSqsOUhjm4XYCqmZrXagxuzgfu4BGXxhlL899WBBLA6atDiHusgO0m111XqG3QV6FoWOT5e4fYRGEuTlt82gqslybdbD9LpQz6Lr435ztDtsfmGrwKi1QDsrHX3YOYvKTjqQFM4nOEz3LPSQANecX1V4uhvA34N0uJhmciPvuocsrIe1VHdhK1K54vWYImZJ1s2lOMmQpyvruwUd2cvcbvjuG6pkXE1_GRVkkgRHneB5Ia0TDX_nhJzppDG0Qf8brSTitGTRWrcw";
    public static String idUsuario = "31y365smpy5c5zy52kgzv7jrjk44";
    public static String URL = "https://api.spotify.com/v1";
    public static  String nomeMuscia = "Please Mr. Postman";
    public static  String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static  String nomePlayList = "Teste Postman";
    public static  String nomeNovaPlayList = "Nova playList";
    public static String nameArtist = "The Marvelettes";
    public static  String nomePlayListAlteracao = "Teste Postman Novo";
    public static Response response;
    public static String idPlayList;
    public static String idNomeMusica;
    public static ArrayList  list;
    public static int index;

    @Test
    public void buscaPlayList()
    {
        buscaPlayListRequest();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            assertTrue(verificaPorName(nomePlayList));
        }
        else
        {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void buscaNomeMusica()
    {
        retornaIdPlayList(nomePlayList);
        buscaNomeMusicaRequest();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            list = response.path("items.track.name");
           assertTrue(verificaPorName(nomeMuscia));
        }
        else
        {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void buscaArtista()
    {
        retornaIdMusica(nomePlayList, nomeMuscia);
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
        retornaIdMusica(nomePlayList, nomeMuscia);
        if(!verificaPorName(nomeMusciaProcura)){
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

//    @Test
//    public void adicionaMusicaArquivoJson() {
//        File json = new File("src/arquivosJson/adicionarMusica.json");
//        retornaIdMusica(nomePlayList, nomeMuscia);
//        if(!verificaPorName(nomeMusciaProcura)) {
//            response = given()
//                    .accept("application/json")
//                    .contentType("application/json")
//                    .header("Authorization", "Bearer " + token)
//                    .body(json)
//                    .when()
//                    .post(URL + "/playlists/" + idPlayList + "/tracks")
//                    .then()
//                    .extract()
//                    .response();
//            if (response.statusCode() != 401) {
//                assertEquals(response.statusCode(), 201);
//                buscaNomeMusicaRequest();
//                list = response.path("items.track.name");
//                assertTrue(verificaPorName(nomeMusciaProcura));
//            } else {
//                System.out.println("Usuário não está logado!");
//            }
//        }else {
//                System.out.println("Música já cadastrada!");
//            }
//    }

    @Test
    public void deletaMusica()
    {
            retornaIdMusica(nomePlayList, nomeMusciaProcura);
            response = given()
                    .accept("application/json")
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .body("{\"tracks\":[{\"uri\":\"spotify:track:6wfK1R6FoLpmUA9lk5ll4T\"}]}")
                    .when()
                    .delete(URL + "/playlists/" + idPlayList + "/tracks")
                    .then()
                    .extract()
                    .response();
            if (response.statusCode() != 401) {
                assertEquals(response.statusCode(), 200);
                buscaNomeMusicaRequest();
//                list = response.path("items.track.name");
                assertFalse(verificaPorName(nomeMusciaProcura));
            } else {
                System.out.println("Usuário não está logado!");
            }
    }

    @Test
    public void zalterarNomePlatList()
    {
            retornaIdPlayList(nomePlayList);
            response = given()
                    .accept("application/json")
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .body("{\"name\":\"" + nomePlayListAlteracao + "\",\"description\":\"" + nomePlayListAlteracao + "\",\"public\":false}")
                    .when()
                    .put(URL + "/playlists/" + idPlayList)
                    .then()
                    .extract()
                    .response();
            if (response.statusCode() != 401) {
                assertEquals(response.statusCode(), 200);
                buscaPlayListRequest();
                list = response.path("items.name");
                assertTrue(verificaPorName(nomePlayListAlteracao));
                assertFalse(verificaPorName(nomePlayList));
            } else {
                System.out.println("Usuário não está logado!");
            }
    }

    @Test
    public void buscaPlayListUsuario()
    {
        buscaPlayListUsuarioRequest();
        if (response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            assertTrue(verificaPorName(nomePlayList));
        } else {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void criarPlayListUsuario()
    {
        retornaIdPlayList(nomePlayList);
        response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{\"name\":\"" + nomeNovaPlayList + "\",\"description\":\"" + nomeNovaPlayList + "\",\"public\":false}")
                .when()
                .post(URL + "/users/" + idUsuario + "/playlists")
                .then()
                .extract()
                .response();
        if (response.statusCode() != 401) {
            assertEquals(response.statusCode(), 201);
            buscaPlayListUsuarioRequest();
            assertTrue(verificaPorName(nomeNovaPlayList));
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
        if (response.statusCode() != 401) {
            list = response.path("items.track.name");
        } else {
            System.out.println("Usuário não está logado!");
        }
    }

    public void buscaPlayListRequest()
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
        if (response.statusCode() != 401) {
             list = response.path("items.name");
        } else {
            System.out.println("Usuário não está logado!");
        }
    }

    public void buscaPlayListUsuarioRequest()
    {
        response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL + "/users/" + idUsuario + "/playlists")
                .then()
                .extract()
                .response();
        if (response.statusCode() != 401) {;
            list = response.path("items.name");
        } else {
            System.out.println("Usuário não está logado!");
        }
    }

    public Boolean verificaPorName(String nome) {
        index = -1;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(nome))
            {
                index = i;
                return  true;
            }
        }
        return false;
    }

    public void retornaIdPlayList(String nomePlayList)
    {
        buscaPlayListRequest();
        if(verificaPorName(nomePlayList)){
            idPlayList = response.path("items[" + index + "].id");
        } else {
            System.out.println("PlayList não encontrada!");
        }
    }

    public void retornaIdMusica(String nomePlaList, String nomeMusica)
    {
        retornaIdPlayList(nomePlaList);
        buscaNomeMusicaRequest();
        if(verificaPorName(nomeMusica)) {
            idNomeMusica = response.path("items[" + index + "].track.id");
        }else {
            System.out.println("Música não encontrada!");
        }
    }
}

