import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import java.io.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class Musica extends  Helper{
    public LerArquivoJson lerArquivoJson = new LerArquivoJson();
    public static String idUsuario = "31y365smpy5c5zy52kgzv7jrjk44";
    public static  String nomeMuscia = "Please Mr. Postman";
    public static  String nomeMusciaProcura = "Please Mister Postman - Remastered 2009";
    public static  String nomePlayList = "Teste Postman";
    public static  String nomeNovaPlayList = "Nova playList";
    public static String nameArtist = "The Marvelettes";
    public static  String descricaoPlayListAlteracao = "Teste Postman Novo";
    public  static  String idMusicaPosicao;
    public static String nomeMusicPaosicao;
    public  static  String idMusicaPosicaoSegunda;
    public static String nomeMusicPaosicaoSegunda;
    public static String idNomeMusica;
    public static PlayList buscaPlayList = new PlayList();

    @BeforeClass
    public void Login()
    {
        gerarToken();
        buscaPlayList.retornaIdPlayList(nomePlayList);
    }

    @Test
    public void buscaNomeMusica()
    {
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
        retornaIdMusica(nomeMuscia);
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
        retornaIdMusica(nomeMusciaProcura);
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

    public void retornaIdMusica(String nomeMusica)
    {
        buscaPlayList.retornaIdPlayList(nomePlayList);
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
        retornaIdMusica(nomeMuscia);
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