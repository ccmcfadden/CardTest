package Demo.CardTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CardTestSuite extends TestCase {
	
	//global variables
	public static final int OK_StatusCode = 200;
	public static final String OK_StatusMessage = "HTTP/1.1 200 OK";
	public static final String BASE_URI = "https://deckofcardsapi.com/api/deck";
	
    public CardTestSuite( String testName ){
        super( testName );
    }
    
 
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( NewDeck.class, DrawCards.class );
    }//end suite method

    
}//end class
