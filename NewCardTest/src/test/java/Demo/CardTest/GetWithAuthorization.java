package Demo.CardTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.TestCase;

public class GetWithAuthorization extends TestCase {

	   private String path;

	@Before
	protected void setUp() throws Exception {
		RestAssured.baseURI = "https://aswsuat.stanford.edu/fss/ijournal";
        path = "/status/";
    }

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() {
		Header authorizationHeader = new Header("Authorization", "Basic ZnNzLWlqb3VybmFsLXN1cHBvcnQ6ZXBxNkNkTUVKeVdyRFRXYQ==");
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header(authorizationHeader);
		/*
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName("fss-ijournal-support");
		authScheme.setPassword("epq6CdMEJyWrDTWa");
		RestAssured.authentication = authScheme;
		*/
		
		Response response = httpRequest.queryParam("P_JOURNAL_NUMBER", "1623250").get(path);

		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);

		/*
		 * given().auth()
  .basic("user1", "user1Pass")
  .when()
  .get("http://localhost:8080/spring-security-rest-basic-auth/api/foos/1")
        Response response = RestAssured.given()
        		.auth()
                .preemptive()
                .basic("required_username", "required_password")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(validRequest)
                .get(path)
                .then().extract().response();
        */
		int statusCode = response.getStatusCode();
		System.out.println("Status code received: "+statusCode);

	}

}
