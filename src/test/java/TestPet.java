// 0 - nome do pacote

// 1 blibliotecas

import io.restassured.response.Response; // classe resposta REST-assured

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;     // funçao given
import static org.hamcrest.Matchers.*;              // classe de verificadores do hamcrest

// 2 classe
public class TestPet {
    // 2.1 atributos, variaveis
    static String ct = "application/json"; //content type
    static String uriPet = "https://petstore.swagger.io/v2/pet";

    // 2.2 funçoes e metodos...
    // 2.2.1 ...comuns / uteis

    // funçao de leitura de json
    public static String lerArquivoJson(String arquivoJson) throws IOException{
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));        
    }

    // 2.2.2 metodos de teste
    @Test
    public void testPostPet() throws IOException{
        //configura 1

        // carregar dados do arquivo json do pet
        String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");
        
        int petId = 373981801; // codigo esperado do pet

        // começa o teste via REST-assured

        given()                         // dado que
            .contentType(ct)            // o tipo do conteudo é
            .log().all()                // mostre tudo na ida
            .body(jsonBody)             // envie o corpo da requisição
        
        // executa 2
        .when()                         // quando
            .post(uriPet)               // chamamos o endpoint fazendo post
        
        // valida 3
        .then()                                 // então
            .log().all()                                   // mostre td na volta
            .statusCode(200)            // verifique o status code é 200 (foi e volto com sucesso)
            .body("name", is("Filomena"))       // verifica se o nome é ----
            .body("id", is(petId))                               // verifica o codigo do pet
            .body("category.name", is("cachorro"))         // verifica se é cachorro
            .body("tags[0].name", is("vacinado"))          // verificar  se esta vacinado
        ;    
    
    }

}