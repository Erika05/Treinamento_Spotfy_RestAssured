import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import java.io.*;
import java.util.*;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

import io.restassured.http.ContentType;

public class Treinamento {
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String token = "BQC9uZbtzeVoof8Wy53cughlJz9O5M6XFv_Zy0uOyEgalwgrMpgGv_09l7k9sYCbJwBobmzLd05SNvciUaty0J125LuPDYbxWpcatnCKH5gL2543g0Rq4XSNtJy1N4OuQaSKhKawBXd6oF83e9cjehCb2d73lIZQgHMXHdWZsDwHeamlJx0y9HJaFEfCKVU66_7njjqNCluv6hjzJhS_FF9EIYz10hWL2W9m9M55B1BqiOWRZzYIf-2DzsKdrR3WghmHFMLR54LGHSteqHIWlcN4hO8ae5U9XjHZVw";
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
    public static String client_id = "507cc87c01f24b7ba331d3d616404eba";
    public static String client_secret = "131c0d2189424c37ae0d39fdc55069ec";

    @BeforeClass
    public void gerarToken()
    {
        response = given()
        .formParam("grant_type", "refresh_token")
        .formParam("refresh_token", "AQC86RkjqfXwUCHcwF2hVWrzankEXqm7FGc6NdOdFBRgf3zhg49ZVwJDEcHXHL83tK1eUyVNWhbPKUBmGfwQ0PqmSqP2I77x3gNaeFxTu9dzHtk_W9DgPvfu7M4lZsqgM4Y")
        .header("Authorization", "Basic NTA3Y2M4N2MwMWYyNGI3YmEzMzFkM2Q2MTY0MDRlYmE6MTMxYzBkMjE4OTQyNGMzN2FlMGQzOWZkYzU1MDY5ZWM=")
        .post("https://accounts.spotify.com/api/token")
        .then()
        .extract()
        .response() ;
        assertEquals(response.statusCode(), 200);
        token = response.path("access_token");
    }

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
            int quantidadeMusica = list.size();
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
               assertEquals(list.size(), quantidadeMusica -1);
            } else {
                System.out.println("Usuário não está logado!");
            }
    }

    @Test
    public void alterarDescricaoPlaytList()
    {
            retornaIdPlayList(nomePlayList);
            if(!buscaDadosPlayList().equals(descricaoPlayListAlteracao)){
                response = given()
                        .accept("application/json")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .body("{\"description\":\"" + descricaoPlayListAlteracao + "\"}")
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
            else {
                System.out.println("Alteração já realizada!");
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
