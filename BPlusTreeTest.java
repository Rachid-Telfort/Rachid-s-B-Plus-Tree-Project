/**
 * 
 */

package bPlusTree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Rachi
 *
 */

class BPlusTreeTest 
{
	/**
	 * @throws java.lang.Exception
	 */
	
	BPlusTree<Integer> testBPlusTree;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception 
	{
		System.out.println("Entering BPlusTree test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
		System.out.println("Exiting BPlusTree test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception 
	{
		testBPlusTree=new BPlusTree<Integer>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterEach
	void tearDown() throws Exception 
	{
		testBPlusTree.clear();
		testBPlusTree=null;
	}

	@Test
	void getKeyLocationTest() 
	{
		for(int index=0; index<400; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertThat(testBPlusTree.getKeyLocation(0), is(0));
		assertThat(testBPlusTree.getKeyLocation(500), is(-1));
	}
	
	@Test
	void insertKeyTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertFalse(testBPlusTree.isEmpty());
	}
	
	@Test
	void searchKeyTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertThat(testBPlusTree.searchKey(250), not(-1));
		assertThat(testBPlusTree.searchKey(-900), not(0));
	}
	
	@Test
	void removeKeyTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
		}
		
		assertThat(testBPlusTree.removeKey(250), not(-1));
		assertThat(testBPlusTree.removeKey(250), not(-1));
	}
	
	@Test
	void rangeTest()
	{
		for(int index=0; index<1000; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		ArrayList<Integer> testRange=testBPlusTree.getRange(250, 500);
		assertThat(testRange.size(), is(251));
		assertArrayEquals(testBPlusTree.getRange(250, 500).toArray(), testRange.toArray());
		
		testRange.clear();
		testRange=null;
	}
	
	@Test
	void lowerBoundTest()
	{
		for(int index=0; index<1000; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		ArrayList<Integer> testLowerBound=testBPlusTree.getLowerBound(500);
		assertThat(testLowerBound.size(), is(500));
		assertArrayEquals(testBPlusTree.getLowerBound(500).toArray(), testLowerBound.toArray());
		
		testLowerBound.clear();
		testLowerBound=null;
	}
	
	@Test
	void upperBoundTest()
	{
		for(int index=0; index<1000; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		ArrayList<Integer> testUpperBound=testBPlusTree.getUpperBound(0);
		assertThat(testUpperBound.size(), is(999));
		assertArrayEquals(testBPlusTree.getUpperBound(0).toArray(), testUpperBound.toArray());
		
		testUpperBound.clear();
		testUpperBound=null;
	}
	
	@Test
	void printAllKeysTest()
	{
		for(int index=0; index<1000000; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertThrows
		(
			StackOverflowError.class, ()-> 
			
			{
				testBPlusTree.printAllKeys();
			}
		);
		
		for(int index=0; index<1000000; ++index)
		{
			testBPlusTree.removeKey(index);
		}
	}
	
	@Test
	void clearTest()
	{
		testBPlusTree.clear();
		assertTrue(testBPlusTree.isEmpty());
	}
	
	@Test
	void emptyTest()
	{
		for(int index=0; index<1000; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertFalse(testBPlusTree.isEmpty());
		
		testBPlusTree.clear();
		
		assertTrue(testBPlusTree.isEmpty());
	}
	
	@Test
	void allKeysTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		ArrayList<Integer> testAllKeys=testBPlusTree.getAllKeys();
		assertArrayEquals(testBPlusTree.getAllKeys().toArray(), testAllKeys.toArray());
		testAllKeys.clear();
		testAllKeys=null;
	}
	
	@Test 
	void uniquateTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
		}
		
		BPlusTree<Integer> uniqueBPlusTree=testBPlusTree.uniquate();
		assertThat(uniqueBPlusTree.getAllKeys().size(), is(500));
		assertFalse(uniqueBPlusTree.isEmpty());
	}
	
	@Test
	void toStringTest()
	{
		for(int index=0; index<500; ++index)
		{
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
			testBPlusTree.insertKey(index);
		}
		
		String BPlusTreeString=testBPlusTree.toString();
		assertThat(BPlusTreeString.charAt(0), is('['));
		assertThat(BPlusTreeString.charAt(BPlusTreeString.length()-1), is(']'));
		BPlusTreeString=null;
	}
	
	@Test
	void heightTest()
	{
		for(int index=0; index<4; ++index)
		{
			testBPlusTree.insertKey(index);
		}
		
		assertThat(testBPlusTree.getHeight(), is(1));
	}
}