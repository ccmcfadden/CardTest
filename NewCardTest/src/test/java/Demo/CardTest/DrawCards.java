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
		String deck_id = NewDeck.getNewDeck(false);
		int expectedRemainingCards = 52;
		boolean isShuffled = false;
		for (int i=1; i<5; i++) {
			expectedRemainingCards-=i;
			drawCards(deck_id, i, expectedRemainingCards, isShuffled);
		}//end for loop

		deck_id = NewDeck.getNewDeck(true);
		expectedRemainingCards = 54;
		isShuffled = shuffleCards(deck_id);
		for (int i=1; i<5; i++) {
			expectedRemainingCards-=i;
			drawCards(deck_id, i, expectedRemainingCards, isShuffled);
		}//end for loop

	}//end test method
	
	private boolean shuffleCards(String deck_id) throws Exception{
		System.out.println();
		System.out.println("*** Shuffling cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck/"+deck_id;
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

	private void drawCards(String deck_id, int numberOfCards, int expectedRemainingCards, boolean isShuffled) throws Exception{
		System.out.println();
		System.out.println("*** Drawing "+numberOfCards+" cards from deck_id "+deck_id+" ***");
		RestAssured.baseURI = "https://deckofcardsapi.com/api/deck/"+deck_id;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("count", numberOfCards).get("/draw/");
		
		//make sure that the transaction was successful
		assertEquals("Incorrect status code received: "+response.getStatusCode(), CardTestSuite.OK_StatusCode, response.getStatusCode());
		assertEquals("Incorrect status line received: "+response.getStatusLine(), CardTestSuite.OK_StatusMessage, response.getStatusLine());

		JsonPath jsonPathEvaluator = response.jsonPath();
		
		ArrayList<HashMap<String, String>> cards = jsonPathEvaluator.get("cards");
		//test to verify the number of cards drawn
		int numberOfCardsActuallyDrawn = cards.size();
		assertEquals("Incorrect number of cards actually drawn: "+numberOfCardsActuallyDrawn, numberOfCards, numberOfCardsActuallyDrawn);
		
		//test to verify the number of cards remaining
		int remainingCards = ((Integer)jsonPathEvaluator.get("remaining")).intValue();
		assertEquals("Incorrect number of remaining cards: "+remainingCards, expectedRemainingCards, remainingCards);
		//if it hasn't failed up to this point, the correct number of cards are drawn and the correct number remain
		System.out.println(numberOfCards+" cards are drawn, "+remainingCards+" are left, as expected");

		//if the deck is not shuffled, the suit should be the same for all cards
		String expectedSuit = cards.get(0).get("suit");
		if (isShuffled)
			System.out.println("The deck is shuffled - various suits will be drawn");
		else
			System.out.println("The deck is not shuffled - only suit "+expectedSuit+" should be drawn");

		for (int i=0; i<cards.size(); i++) {
			HashMap<String, String> card = cards.get(i);
			String actualSuit = card.get("suit");
			System.out.println("Card at index "+i+": is "+card.get("value")+" of "+actualSuit+" with code "+card.get("code"));
			if (! isShuffled)
				assertEquals("Wrong suit on the card is drawn for a deck that is not shuffled: "+actualSuit, expectedSuit, actualSuit);
		}//end for loop
		
	}//end drawCards method

}//end DrawCards class
