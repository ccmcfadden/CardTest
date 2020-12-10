package Demo.CardTest;

import org.junit.Test;
import junit.framework.TestCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.path.json.JsonPath;

/*
 * Of note, this Class is a bona fide test class in and of itself, but its methods are 
 * used by the DrawCards class when testing as well.  This is run initially by itself
 * to verify that it is working properly before the DrawCards tests are run,
 * as the DrawCards tests depend upon this working properly.  
 */
public class NewDeck extends TestCase{

	public static final String PATH = "/new/";

	
    public NewDeck( String testName )
    {
        super( testName );
    }
    
	@Test
	public void test() throws Exception {
		boolean jokers_enabled[] = {false, true};
		for (int i=0; i<jokers_enabled.length; i++) {
			JsonPath jsonPathEvaluator = getNewDeck(jokers_enabled[i]);
			getDeckID(jsonPathEvaluator);
			evaluateRemainingCards(jsonPathEvaluator, jokers_enabled[i]);
		}//end for loop
	}//end test method
	
	
	public static JsonPath getNewDeck(boolean jokers_enabled) throws Exception {
		if (jokers_enabled)
			System.out.println("***** GET Request is being evaluated with Jokers enabled ******");
		else
			System.out.println("***** GET Request is being evaluated with Jokers *not* enabled ******");

		RestAssured.baseURI = CardTestSuite.BASE_URI;//"https://deckofcardsapi.com/api/deck";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("jokers_enabled", jokers_enabled).get(PATH);
		
		assertEquals("Incorrect status code received: "+response.getStatusCode(), CardTestSuite.OK_StatusCode, response.getStatusCode());
		assertEquals("Incorrect status line received: "+response.getStatusLine(), CardTestSuite.OK_StatusMessage, response.getStatusLine());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		return jsonPathEvaluator;
	}//end method getNewDeck
	
	public static String getDeckID(JsonPath jsonPathEvaluator) throws Exception{
		String deck_id = jsonPathEvaluator.get("deck_id");
		System.out.println("Deck ID: "+deck_id);
		return deck_id;
	}//end getDeckID method
	
	public void evaluateRemainingCards(JsonPath jsonPathEvaluator, boolean jokers_enabled) throws Exception{
		int expectedRemainingCards = 52;
		if (jokers_enabled)
			expectedRemainingCards = 54;
		int actualRemainingCards = ((Integer)jsonPathEvaluator.get("remaining")).intValue();
		assertEquals("Incorrect number of cards remaining: "+actualRemainingCards, expectedRemainingCards, actualRemainingCards);
		//if it doesn't fail before this point, it passes - just output the number of remaining cards
		System.out.println("Number of Cards Remaining in the deck is "+actualRemainingCards+", as expected");
		
	}//end evaluateRemainingCards method

}//end test class
