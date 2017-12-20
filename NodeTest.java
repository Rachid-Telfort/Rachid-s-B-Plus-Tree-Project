/**
 * 
 */

package bPlusTree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Rachi
 *
 */

class NodeTest 
{
	/**
	 * @throws java.lang.Exception
	 */
	
	Node<String> testNode;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception 
	{
		System.out.println("Entering Node test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
		System.out.println("Exiting Node test case...");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception 
	{
		testNode=null;
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterEach
	void tearDown() throws Exception 
	{
		testNode=null;
	}

	@Test
	void nullTest() 
	{
		assertNull(testNode);
	}
}