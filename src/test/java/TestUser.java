// biblioteca
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;

import org.apache.http.entity.mime.content.StringBody;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

// classe
public class TestUser {
    static String ct = "application/json"; //content type
    static String uriUser = "https://petstore.swagger.io/v2/user";

    
    @Test
    public void testLogin(){
        // configura
        String username = "Pedrinho";
        String password = "pedroca";

        String resultadoEsperado = "logged in user session:";

        Response resposta = (Response) given()
            .contentType(ct)
            .log().all()
        // executa
        .when()
            .get(uriUser + "/login?username=" + username + "&password=" + password) 
        // valida 
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", containsString(resultadoEsperado)) // contem
            .body("message", hasLength(36))  // contar numero de caracteres
        .extract();
        ;

        // extração
        String token = resposta.jsonPath().getString("message").substring(23);
        System.out.println("Conteudo do Token: " + token);
}

}
