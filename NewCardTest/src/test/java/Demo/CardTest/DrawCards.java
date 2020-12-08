package Demo.CardTest;

//import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import io.restassured.http.Headers;
import io.restassured.http.Header;
import io.restassured.response.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DrawCards extends TestCase{

    public DrawCards( String testName )
    {
        super( testName );
    }
    

	@Test
	public void test() throws Exception{
		String deck_id = NewDeck.getNewDeck(false);
		int expectedRemainingCards = 52;		
		for (int i=1; i<5; i++) {
			expectedRemainingCards-=i;
			drawCards(deck_id, i, expectedRemainingCards);
		}//end for loop

		deck_id = NewDeck.getNewDeck(true);
		expectedRemainingCards = 54;
		shuffleCards(deck_id);
		for (int i=1; i<5; i++) {
			expectedRemainingCards-=i;
			drawCards(deck_id, i, expectedRemainingCards);
		}//end for loop

	}//end test method
	
	private void shuffleCards(String deck_id) throws Exception{
		System.out.println();
		System.out.println("*** Shuffling cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck/"+deck_id;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/shuffle/");
	}//end shuffleCards method

	private void drawCards(String deck_id, int numberOfCards, int expectedRemainingCards) throws Exception{
		System.out.println();
		System.out.println("*** Drawing "+numberOfCards+" cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck/"+deck_id;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("count", numberOfCards).get("/draw/");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		
		int statusCode = response.getStatusCode();
		assertEquals("Incorrect status code received: "+statusCode, 200, statusCode);
		String statusLine = response.getStatusLine();
		assertEquals("Incorrect status line received: "+statusLine, "HTTP/1.1 200 OK", statusLine);

		//make sure that the transaction was successful
		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean success = jsonPathEvaluator.get("success");
		System.out.println("Success: "+success.toString());
		assertEquals("Incorrect success status received: "+success.toString(), "true", success.toString());

		ArrayList<HashMap> cards = jsonPathEvaluator.get("cards");
		for (int i=0; i<cards.size(); i++) {
			System.out.println("Card at index "+i+": ");
			Iterator iterator = cards.get(i).entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry)iterator.next();
				System.out.println("     "+mapElement.getKey()+": "+mapElement.getValue());
			}//end while
		}//end for loop

		Integer remainingCards = jsonPathEvaluator.get("remaining");
		System.out.println("Remaining Cards: "+remainingCards.intValue());
		assertEquals("Incorrect number of remaining cards: "+remainingCards.intValue(), expectedRemainingCards, remainingCards.intValue());
	}//end drawCards method

}
