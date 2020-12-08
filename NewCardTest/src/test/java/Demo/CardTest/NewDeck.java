package Demo.CardTest;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import io.restassured.http.Headers;
import io.restassured.http.Header;
import io.restassured.response.ResponseBody;
import java.io.IOException;

import io.restassured.path.json.JsonPath;


public class NewDeck extends TestCase{

	private static ChromeDriver driver;

	
    public NewDeck( String testName )
    {
        super( testName );
    }
    

	@Test
	public void test() throws Exception {
		//the boolean parameter passed is jokers_enabled
		String deck_id = getNewDeck(false);
		deck_id = getNewDeck(true);
		//this test has been failing - access is forbidden (Error code 403)
	}//end test method
	
	
	public static String getNewDeck(boolean jokers_enabled) throws Exception {
		System.out.println("***** GET Request is being evaluated with Jokers enabled : "+jokers_enabled+" ******");
		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("jokers_enabled", jokers_enabled).get("/new/");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		
		int statusCode = response.getStatusCode();
		assertEquals("Incorrect status code received: "+statusCode, 200, statusCode);
		String statusLine = response.getStatusLine();
		assertEquals("Incorrect status line received: "+statusLine, "HTTP/1.1 200 OK", statusLine);
		
		System.out.println();
		System.out.println();
		System.out.println("*** EVALUATING HEADER ***");


		String contentType = response.header("Content-Type");
		System.out.println("Response Header Content Type is "+contentType);
		
		System.out.println();
		System.out.println();
		System.out.println("*** EVALUATING BODY ***");
		
		ResponseBody body = response.getBody();
		System.out.println("Response Body is: " + body.asString());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean success = jsonPathEvaluator.get("success");
		System.out.println("Success: "+success.toString());
		assertEquals("Incorrect success status received: "+success.toString(), "true", success.toString());
		
		String deck_id = jsonPathEvaluator.get("deck_id");
		System.out.println("Deck ID: "+deck_id);
		
		int expectedRemainingCards = 52;
		if (jokers_enabled)
			expectedRemainingCards = 54;
		Integer actualRemainingCards = jsonPathEvaluator.get("remaining");
		System.out.println("Actual Remaining Cards: "+actualRemainingCards.intValue());
		assertEquals("Incorrect number of cards remaining: "+actualRemainingCards.intValue(), expectedRemainingCards, actualRemainingCards.intValue());
		
		return deck_id;
	}

}//end test class
