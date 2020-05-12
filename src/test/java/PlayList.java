import org.testng.SkipException;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PlayList extends Helper {

    public static String nomePlayList = "Teste Postman";

    @Test
    public void buscaPlayList() {
        buscaPlayListRequest();
        if (response.statusCode() != 401) {
            assertEquals(response.statusCode(), 200, "PlayList não encontrada!");
            assertTrue(verificaPorName(nomePlayList));
        } else {
            System.out.println("Usuário não está logado!");
            throw new SkipException("Usuário não está logado!");
        }
    }

    public void buscaPlayListRequest() {
        gerarToken();
        response = given()
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

    public void retornaIdPlayList(String playList) {
        buscaPlayListRequest();
        if (verificaPorName(playList)) {
            idPlayList = response.path("items[" + index + "].id");
        } else {
            System.out.println("PlayList não encontrada!");
        }
    }
}

