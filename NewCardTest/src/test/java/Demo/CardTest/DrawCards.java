package Demo.CardTest;


import org.junit.Test;
import junit.framework.TestCase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.HashMap;


public class DrawCards extends TestCase{

    public DrawCards( String testName )
    {
        super( testName );
    }


	@Test
	public void test() throws Exception {
		boolean[] jokers_enabled = {true, false, false, true};
		for (int i=0; i<jokers_enabled.length; i++) {
			JsonPath jsonPathEvaluator = NewDeck.getNewDeck(jokers_enabled[i]);
			String deck_id = NewDeck.getDeckID(jsonPathEvaluator);
			boolean isShuffled = false;
			if (i%2 == 1)
				isShuffled = shuffleCards(deck_id);
			else 
				isShuffled = false;
			int expectedRemainingCards = 52;
			int expectedValue = 0;
			if (jokers_enabled[i])
				expectedRemainingCards = 54;
			for (int j=1; j<5; j++) {
				expectedRemainingCards-=j;
				jsonPathEvaluator = drawCards(deck_id, j);
				verifyCardsRemaining(expectedRemainingCards, jsonPathEvaluator);
				ArrayList<HashMap<String, String>> cards 
					= (ArrayList<HashMap<String, String>>)jsonPathEvaluator.get("cards");
				verifyCardsDrawn(j, cards);
				if (! isShuffled) {
					verifySuitUniformity(cards);
					expectedValue = verifySuitOrder(cards, expectedValue);
				}//end if - the deck is not shuffled
			}//end inner for loop
		}//end outer for loop
	}//end test method
	
	private void verifySuitUniformity(ArrayList<HashMap<String, String>> cards) throws Exception{
		String expectedSuit = cards.get(0).get("suit");
		System.out.println("The deck is not shuffled - only suit "+expectedSuit+" should be drawn");
		for (int i=0; i<cards.size(); i++) {
			HashMap<String, String> card = cards.get(i);
			String actualSuit = card.get("suit");
			String outputCardString = "Card at index "+i+": is "+card.get("value")+" of "+actualSuit+" with code "+card.get("code");
			assertEquals("Wrong suit on the card is drawn for a deck that is not shuffled: "+outputCardString, expectedSuit, actualSuit);
		}//end for loop
		System.out.println("Only suit "+expectedSuit+" is drawn, as expected");
	}//end verifySuitUniformity method
	
	//this method receives the ArrayList of card Objects and the last index of the 
	private int verifySuitOrder (ArrayList<HashMap<String, String>> cards, int beginIndex) throws Exception{
		System.out.println("The deck is not shuffled - all cards should be in numerical order");
		int expectedValue = beginIndex;
		String cardValueOutputString = new String();
		for (int i=0; i<cards.size(); i++) {
			expectedValue++;
			String actualValue = cards.get(i).get("value");
			if (expectedValue == 1) {
				assertEquals("Wrong value on the card at index "+i, "ACE", actualValue);
				cardValueOutputString = "ACE";
			}
			else {
				cardValueOutputString = cardValueOutputString.concat(actualValue);
				assertEquals("Wrong value on the card at index "+i, expectedValue, new Integer(actualValue).intValue());
			}
			if (i<cards.size()-1) 
				cardValueOutputString = cardValueOutputString.concat(", ");
		}//end for loop
		System.out.println("... and all cards are in order, as expected - order is "+cardValueOutputString);
		return expectedValue;
	}//end verifySuitOrder method
	
	private boolean shuffleCards(String deck_id) throws Exception{
		System.out.println();
		System.out.println("*** Shuffling cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = CardTestSuite.BASE_URI+"/"+deck_id;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/shuffle/");
		
		//make sure that the transaction was successful
		assertEquals("Incorrect status code received: "+response.getStatusCode(), CardTestSuite.OK_StatusCode, response.getStatusCode());
		assertEquals("Incorrect status line received: "+response.getStatusLine(), CardTestSuite.OK_StatusMessage, response.getStatusLine());

		JsonPath jsonPathEvaluator = response.jsonPath();

		//make sure that the deck is actually shuffled
		boolean isActuallyShuffled =  ((Boolean)jsonPathEvaluator.get("shuffled")).booleanValue();
		assertEquals("Deck was not shuffled as expected", true, isActuallyShuffled);
		//if it made it to this point without failing, it passed - just output the status message
		System.out.println("Deck is actually shuffled, as expected");
		
		return isActuallyShuffled;
	}//end shuffleCards method

	//re-write this so that one only gets the cards - not count the cards or verify the suit. 
	//break the counting and suit verification into separate methods using the "cards" ArrayList<HashMap<String, String>>
	private JsonPath drawCards(String deck_id, int numberOfCards) throws Exception{
		System.out.println();
		System.out.println("*** Drawing "+numberOfCards+" cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = CardTestSuite.BASE_URI+"/"+deck_id;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("count", numberOfCards).get("/draw/");
		
		//make sure that the transaction was successful
		assertEquals("Incorrect status code received: "+response.getStatusCode(), CardTestSuite.OK_StatusCode, response.getStatusCode());
		assertEquals("Incorrect status line received: "+response.getStatusLine(), CardTestSuite.OK_StatusMessage, response.getStatusLine());

		//.get("cards")
		JsonPath jsonPathEvaluator = response.jsonPath();		
		return jsonPathEvaluator;
	}//end drawCards method
	
	private void verifyCardsDrawn(int expected, ArrayList<HashMap<String, String>> cards) throws Exception{
		System.out.print("Verifying that "+expected+" cards were drawn");
		int actual = cards.size();
		assertEquals("Incorrect number of cards actually drawn: "+actual, expected, actual);
		System.out.println(", and that was the number drawn");
	}//end verifyCardsDrawn method
	
	private void verifyCardsRemaining(int expected, JsonPath jsonPathEvaluator) throws Exception{
		System.out.print("Verifying that "+expected+" cards actually remain in the deck");
		int actual = ((Integer)jsonPathEvaluator.get("remaining")).intValue();
		assertEquals("Incorrect number of cards actually remaining: "+actual, expected, actual);
		System.out.println(", and that is the number remaining");
	}//end verifyCardsRemaining method
	
}//end DrawCards class
