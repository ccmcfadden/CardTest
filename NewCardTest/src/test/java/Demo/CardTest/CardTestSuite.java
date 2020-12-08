package Demo.CardTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import junit.framework.TestSuite;
import junitparams.JUnitParamsRunner;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Unit test for simple App.
 */
public class CardTestSuite extends TestCase {
	
	private static ChromeDriver driver;
	
	@Before
	public void setUp() {
	}
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CardTestSuite( String testName )
    {
        super( testName );
    }
    
    public static ChromeDriver getDriver() throws Exception{
    	return driver;
    }
    

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
		//driver = new ChromeDriver();
        return new TestSuite( NewDeck.class, DrawCards.class );
    }//end suite method

    
}//end class
