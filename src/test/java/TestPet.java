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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;


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
            .header("", "api_key: " + TestUser.testLogin())
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

    // massa de teste DDT
    //teste com Json parametrizado

    @ParameterizedTest @Order (5)
    @CsvFileSource(resources = "/csv/petMassa.csv", numLinesToSkip = 1, delimiter = ',')
    public void testPostPetDDT(
        int petId,
        String petName,
        int catId,
        String catName,
        String status1,
        String status2
    )       // fim dos parametros
    {       // incio do codigo do metodo testPostPetDDT
            // criar a classe pet para receber os dados do CSV
        Pet pet = new Pet(); // instancia a classe User
        Pet.Category category = pet.new Category();  // instanciar subclasse
        Pet.Tag[] tags = new Pet.Tag[2]; 
        tags[0] = pet.new Tag();
        tags[1] = pet.new Tag();

        pet.id = petId; 
        pet.category = category; // associar pet.categopry com a subclasse
        pet.category.id = catId;
        pet.category.name = catName;
        pet.name = petName;
        // pet.photoUrls = nao precisa ser incluido pq ta vazio
        pet.tags = tags; 
        pet.tags[0].id = 9;
        pet.tags[0].name = "vacinado";
        pet.tags[1].id = 8;
        pet.tags[1].name = "vermifugado";
        pet.status = status1; // status inicial

        // criar um json para o body ser enviado a partir da classe Pet e do CSV
        Gson gson = new Gson(); // instancia a classe Gson com o objeto gson
        String jsonBody = gson.toJson(pet);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriPet)
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(petId))
            .body("name", is(petName))
            .body("category.id", is(catId))
            .body("category.name", is(catName))
            .body("status", is(status1))    // inicial do post
            ;
    }

}
