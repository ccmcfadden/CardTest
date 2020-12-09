package Demo.CardTest;

import org.junit.Test;
import junit.framework.TestCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.path.json.JsonPath;


public class NewDeck extends TestCase{

	public static final String PATH = "/new/";

	
    public NewDeck( String testName )
    {
        super( testName );
    }
    
	@Test
	public void test() throws Exception {
		//the boolean parameter passed is jokers_enabled
		getNewDeck(false);
		getNewDeck(true);
	}//end test method
	
	
	public static String getNewDeck(boolean jokers_enabled) throws Exception {
		if (jokers_enabled)
			System.out.println("***** GET Request is being evaluated with Jokers enabled ******");
		else
			System.out.println("***** GET Request is being evaluated with Jokers *not* enabled ******");

		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("jokers_enabled", jokers_enabled).get(PATH);
		
		assertEquals("Incorrect status code received: "+response.getStatusCode(), CardTestSuite.OK_StatusCode, response.getStatusCode());
		assertEquals("Incorrect status line received: "+response.getStatusLine(), CardTestSuite.OK_StatusMessage, response.getStatusLine());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean success = jsonPathEvaluator.get("success");
		System.out.println("Success: "+success.toString());
		assertEquals("Incorrect success status received: "+success.toString(), "true", success.toString());
		
		String deck_id = jsonPathEvaluator.get("deck_id");
		System.out.println("Deck ID: "+deck_id);
		
		int expectedRemainingCards = 52;
		if (jokers_enabled)
			expectedRemainingCards = 54;
		int actualRemainingCards = ((Integer)jsonPathEvaluator.get("remaining")).intValue();
		assertEquals("Incorrect number of cards remaining: "+actualRemainingCards, expectedRemainingCards, actualRemainingCards);
		//if it doesn't fail before this point, it passes - just output the number of remaining cards
		System.out.println("Number of Cards Remaining in the deck is "+actualRemainingCards+", as expected");
		
		return deck_id;
	}//end method getNewDeck

}//end test class
