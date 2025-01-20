package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8002/tasks-backend";
	}

	@Test
	public void shouldGetTasks() {
		RestAssured.given()
			.log().all()
		.when()
			.get("/todo")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void shouldCreateTask() {
		RestAssured.given()
			.body("{\"task\": \"Teste via Api\",\"dueDate\": \"2025-12-30\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void shouldNotAddInvalidTask() {
		RestAssured.given()
			.body("{\"task\": \"Teste via Api\",\"dueDate\": \"2010-12-30\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void shouldRemoveTaskWithSuccess() {
		Integer id = RestAssured.given()
			.body("{\"task\": \"Teste via Api\",\"dueDate\": \"2025-12-30\"}")
			.contentType(ContentType.JSON)
				.when()
					.post("/todo")
				.then()
			.statusCode(201)
			.extract().path("id");
		
		RestAssured.given()
			.when()
				.delete("/todo/" + id)
			.then()
				.statusCode(204);
	}
}


