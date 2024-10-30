// 0 - nome do pacote

// 1 blibliotecas

import static io.restassured.RestAssured.given;     // funçao given
// classe de verificadores do hamcrest
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// 2 classe
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // definir a sequencia de ordem dos testes
public class TestPet {
    // 2.1 atributos, variaveis
    static String ct = "application/json"; //content type
    static String uriPet = "https://petstore.swagger.io/v2/pet";
    static int petId = 373981801;
    String petName = "Filomena";
    String categoryName = "cachorro";
    String tagsName = "vacinado";
    String[] status = {"available", "sold"};


    // 2.2 funçoes e metodos...
    // 2.2.1 ...comuns / uteis

    // funçao de leitura de json
    public static String lerArquivoJson(String arquivoJson) throws IOException{
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));        
    }

    // 2.2.2 metodos de teste
    @Test @Order(1)
    public void testPostPet() throws IOException{
        //configura 1

        // carregar dados do arquivo json do pet
        String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");
        
        
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
            .body("name", is(petName))       // verifica se o nome é ----
            .body("id", is(petId))                               // verifica o codigo do pet
            .body("category.name", is(categoryName))         // verifica se é cachorro
            .body("tags[0].name", is(tagsName))          // verificar  se esta vacinado
        ;    // fim do BDD
    
    }
    @Test @Order(2)
    public void  testGetPet(){
        // configura 1
        // entrada - petId static lna classe
        // saidas - resultado esperado na classe
        

        given()                 // dado
            .contentType(ct)            
            .log().all()
            // qnd é get e delete nao tem body 
        
        // executa 2
        .when()                 // quando
            .get(uriPet + "/" + petId) // montar o endpoint da URI, com o link + a barra + o petid
        
        // valida 3
        .then()                 // entao
            .log().all()                                  
            .statusCode(200) 
            .body("name", is(petName))       
            .body("id", is(petId))                               
            .body("category.name", is(categoryName))         
            .body("tags[0].name", is(tagsName))          
        ; // fim do BDD       
    }
    @Test @Order(3)
    public void testPutPet() throws IOException{
        // configura
        String jsonBody = lerArquivoJson("src/test/resources/json/pet2.json");

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)    
        // executa
        .when()
            .put(uriPet)
        // valida
        .then()
            .log().all()
            .statusCode(200);
            // copiar campos so swegger
    }

    @Test @Order(4)
    public void testDeletePet(){
        // configura -> entrada e saida na classe

        given()
            .contentType(ct)
            .log().all()
        // executa
        .when()
            .delete(uriPet + "/" + petId)
        // valida
        .then()
            .statusCode(200) // se comunicou e processou
            .body("code", is(200))   // confirmou que apagou
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(petId)))
        ;

    }
}
