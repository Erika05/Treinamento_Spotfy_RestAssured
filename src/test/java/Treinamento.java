import io.restassured.response.Response;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import java.io.File;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class Treinamento {
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String token = "BQAoemv6becodF5KWpdtJRpRVE-ECgUQHfc0E8EpKnjyXzmcJNvRwynAmp6MqH9tKAOTJ9dZ-8T9xh-3tLLJIkXVJ_BgOQi1emK7hyvQ8t4AtqxmfAmwjNHksEmdP3NurYZAywPDGzB1cXli_c-rcX1Yis31Tt3ZY-Xh3RaUtZFYuQ5Cgqv6ZEzh8xqT2bwvdQ4RVHly9bZOlzFJySxMUu66J1Igu_4-B2YzNJxVlRkHEQ2plzUa6uY4JCuY2qw1czQ8nypdmugjuodMgcCwSjQEazfwt37O3y-o5g";
    public static String idUsuario = "31y365smpy5c5zy52kgzv7jrjk44";
    public static String URL = "https://api.spotify.com/v1";
    public static  String nomeMuscia = "Please Mr. Postman";
    public static  String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static  String nomePlayList = "Teste Postman";
    public static  String nomeNovaPlayList = "Nova playList";
    public static String nameArtist = "The Marvelettes";
    public static  String descricaoPlayListAlteracao = "Teste Postman Novo";
    public static Response response;
    public static String idPlayList;
    public  static  String idMusicaPosicao;
    public static String nomeMusicPaosicao;
    public  static  String idMusicaPosicaoSegunda;
    public static String nomeMusicPaosicaoSegunda;
    public static String idNomeMusica;
    public static ArrayList  list;
    public static int index;

    @Test
    public void buscaPlayList()
    {
        buscaPlayListRequest();
        if(response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200, "PlayList não encontrada!");
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
                assertFalse(verificaPorName(nomeMusciaProcura));
            } else {
                System.out.println("Usuário não está logado!");
            }
    }

    @Test
    public void alterarDescricaoPlaytList()
    {
            retornaIdPlayList(nomePlayList);
            response = given()
                    .accept("application/json")
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .body("{\"description\":\"" + descricaoPlayListAlteracao  + "\"}")
                    .when()
                    .put(URL + "/playlists/" + idPlayList)
                    .then()
                    .extract()
                    .response();
            if (response.statusCode() != 401) {
                assertEquals(response.statusCode(), 200);
                assertEquals(buscaDadosPlayList(), descricaoPlayListAlteracao);
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
            idPlayList = response.path("id");
            assertEquals(buscaDadosPlayList(), nomeNovaPlayList);
        } else {
            System.out.println("Usuário não está logado!");
        }
    }

    @Test
    public void reordenarPlayList()
    {
        retornaIdPlayList(nomePlayList);
        buscaNomeMusicaRequest();
        idMusicaPosicao = response.path("items[0].track.id");
        nomeMusicPaosicao = response.path("items[0].track.name");
        idMusicaPosicaoSegunda = response.path("items[1].track.id");
        nomeMusicPaosicaoSegunda = response.path("items[1].track.name");
        response = given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{\"range_start\":0,\"range_length\":1,\"insert_before\":2}")
                .when()
                .put(URL + "/playlists/" + idPlayList + "/tracks")
                .then()
                .extract()
                .response();
        if (response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200);
            buscaNomeMusicaRequest();
            assertEquals(idMusicaPosicao, response.path("items[1].track.id"));
            assertEquals(nomeMusicPaosicao, response.path("items[1].track.name"));
            assertEquals(idMusicaPosicaoSegunda, response.path("items[0].track.id"));
            assertEquals(nomeMusicPaosicaoSegunda, response.path("items[0].track.name"));
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

    public String buscaDadosPlayList()
    {
        response =  given()
                .accept("application/json")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(URL + "/playlists/" + idPlayList)
                .then()
                .extract()
                .response();
        if (response.statusCode() != 401) {
           return response.path("description");
        } else {
            System.out.println("Usuário não está logado!");
            return null;
        }
    }

   @Test @Ignore("Usando arquivo JSON")
    public void adicionaMusicaArquivoJson() {
        File json = new File("src/arquivosJson/adicionarMusica.json");
        retornaIdMusica(nomePlayList, nomeMuscia);
        if(!verificaPorName(nomeMusciaProcura)) {
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
                System.out.println("Música já cadastrada!");
            }
    }
}
