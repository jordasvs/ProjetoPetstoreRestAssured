package PetstoreTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPRequests {
	
	//Cadastrar novo pedido de pet com sucesso
	@Test(priority=1)
	public void postPedido(){
		
		HashMap data=new HashMap();
		data.put("id", "1");
		data.put("petId", "1");
		data.put("quantity", "1");
		data.put("shipDate", "2024-03-26T13:08:08.361Z");
		data.put("status", "placed");
		data.put("complete", true);

		given()
			.contentType("application/json")
			.body(data)
		.when()
			.post("https://petstore.swagger.io/v2/store/order")
		.then()
			.statusCode(200)
			.log().all()
			.body("id", equalTo(1))
	        .body("petId", equalTo(1))
	        .body("quantity", equalTo(1))
	        .body("shipDate", equalTo("2024-03-26T13:08:08.361+0000"))
	        .body("status", equalTo("placed"))
	        .body("complete", equalTo(true));
	}
	
	//Pesquisar por um pet inexistente
	@Test(priority=2)
	public void getPetInexistente(){
		int petId = 999;

		given()
			.pathParam("petId", petId)
		.when()
			.get("https://petstore.swagger.io/v2/pet/{petId}")
		.then()
			.statusCode(404)
			.body("code", equalTo(1))
            .body("type", equalTo("error"))
            .body("message", equalTo("Pet not found"))
			.log().all();
	}
	
	// Atualizar dados de um pet existente 
	@Test(priority=3)
	public void putAtualizarDados() {
		int petId = 1;

		HashMap data = new HashMap();
		data.put("id", petId);
	   
		HashMap category = new HashMap();
		category.put("id", 1);
		category.put("name", "string");
		data.put("category", category);
		data.put("name", "Floquinho");
	   
		ArrayList photoUrls = new ArrayList();
		photoUrls.add("string");
		data.put("photoUrls", photoUrls);
	   
		ArrayList tags = new ArrayList();
		HashMap tag = new HashMap();
		tag.put("id", 0);
		tag.put("name", "string");
		tags.add(tag);
		data.put("tags", tags);
		data.put("status", "available");

		given()
	        .contentType("application/json")
	           .body(data)
	        .when()
	            .put("https://petstore.swagger.io/v2/pet")
	        .then()
	            .statusCode(200)
	            .log().all();
	   
		//Usando um GET com id usado, para validação das alterações realizadas.    
		HashMap response = given()
	        .pathParam("petId", petId)
	        .when()
	            .get("https://petstore.swagger.io/v2/pet/{petId}")
	        .then()
	            .statusCode(200)
	            .extract()
	            .as(HashMap.class);
		assert response.get("name").equals("Floquinho");
		assert response.get("status").equals("available");
	}
	
	//Pesquisar por pets com status “pending”
	@Test(priority=4)
	public void getPetStatusPending() {
	   given()
	       .queryParam("status", "pending") // Definindo o parâmetro "status" como "pending"
	   .when()
	       .get("https://petstore.swagger.io/v2/pet/findByStatus")
	   .then()
	       .statusCode(200)
	       .body("status", everyItem(equalTo("pending")))
	       .log().all();
	}		
}