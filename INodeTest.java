/**
 * 
 */

package bPlusTree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Rachi
 *
 */

class INodeTest 
{
	/**
	 * @throws java.lang.Exception
	 */
	
	INode<Double> testINode;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception 
	{
		System.out.println("Entering INode test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
		System.out.println("Exiting INode test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception 
	{
		testINode=new INode<Double>(4, null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterEach
	void tearDown() throws Exception 
	{
		testINode.clear();
		testINode=null;
	}

	@Test
	void capacityTest() 
	{
		testINode.setCapacity(100);
		assertTrue(testINode.getCapacity()==100);
	}
	
	@Test
	void keysTest()
	{
		ArrayList<Double> testKeys=new ArrayList<Double>();
		for(int keyCount=0; keyCount<900; ++keyCount)
		{
			testKeys.add((double) (keyCount+0.99));
		}
		testINode.setKeys(new ArrayList<Double>(testKeys));
		assertArrayEquals(testINode.getKeys().toArray(), testKeys.toArray());
		testKeys.clear();
		testKeys=null;
	}
	
	@Test
	void parentTest()
	{
		INode<Double> testParent=new INode<Double>(4, null);
		testINode.setParent(testParent);
		assertEquals(testINode.getParent(), testParent);
		testParent=null;
	}
	
	@Test
	void childrenTest()
	{
		ArrayList<Node<Double>> testChildren=new ArrayList<Node<Double>>();
		testChildren.add(new LNode<Double>(3, null));
		testChildren.add(new LNode<Double>(3, null));
		testINode.setChildren(testChildren);
		assertEquals(testINode.getChildren(), testChildren);
		assertThat(testINode.getChildren().size(), is(2));
		assertThat(testINode.getChildren().get(0), sameInstance(testINode.getChildren().get(0)));
		assertThat(testINode.getChildren().get(1), sameInstance(testINode.getChildren().get(1)));
		testChildren=null;
	}
	
	@Test
	void keyLocationTest() 
	{
		testINode.getChildren().add(new LNode<Double>(3, null));
		testINode.getChildren().add(new LNode<Double>(3, null));
		assertEquals(testINode.getKeyLocation(9.91), -1);
		testINode.getKeys().add(9.0);
		testINode.insertKey(8.7);
		testINode.insertKey(10.5);
		assertEquals(testINode.getKeyLocation(8.7), 0);
		assertEquals(testINode.getKeyLocation(10.5), 0);
	}
	
	@Test
	void insertKeyTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, null));
		testINode.getChildren().add(new LNode<Double>(3, null));
		testINode.getKeys().add(10.0);
		testINode.insertKey(2.0);
		testINode.insertKey(4.0);
		testINode.insertKey(11.9);
		testINode.insertKey(100.265);
		assertTrue(testINode.getChildren().get(0).getKeys().size()==2);
		assertTrue(testINode.getChildren().get(1).getKeys().size()==2);
		assertTrue(testINode.getChildren().get(0).getKeys().get(0)==2.0);
		assertTrue(testINode.getChildren().get(0).getKeys().get(1)==4.0);
		assertTrue(testINode.getChildren().get(1).getKeys().get(0)==11.9);
		assertTrue(testINode.getChildren().get(1).getKeys().get(1)==100.265);
	}
	
	@Test
	void splitTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.split();
		
		assertTrue(testINode.getKeys().size()==2);
		assertTrue(testINode.getChildren().size()==3);
		assertNotNull(testINode.getParent());
		assertTrue(testINode.getParent().getKeys().size()==1);
		assertTrue(((INode<Double>)testINode.getParent()).getChildren().size()==2);
		assertTrue(((INode<Double>)testINode.getParent()).getChildren().get(1).getKeys().size()==2);
		assertTrue(((INode<Double>)((INode<Double>)testINode.getParent()).getChildren().get(1)).getChildren().size()==3);
	}
	
	@Test
	void mergeTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.getKeys().add(5.0);
		testINode.split();
		
		testINode.getKeys().remove(testINode.getKeys().size()-1);
		testINode.getChildren().remove(testINode.getChildren().size()-1);
		testINode.merge();
		
		assertThat(testINode.getParent().getKeys().size(), is(0));
		assertThat(((INode<Double>)testINode.getParent()).getChildren().size(), is(1));
		assertTrue(testINode.getKeys().size()==4);
		assertTrue(testINode.getChildren().size()==5);
	}
	
	
	@Test
	void searchKeyTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		
		assertTrue(testINode.searchKey(4.50)==0);
		assertTrue(testINode.searchKey(6.78)==0);
		assertFalse(testINode.searchKey(45.0)==0);
	}
	
	@Test
	void removeKeyTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		
		assertThat(testINode.removeKey(2.50), is(0));
		assertThat(testINode.removeKey(-90.8), is(-1));
	}
	
	@Test
	void rangeTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		((LNode<Double>)testINode.getChildren().get(0)).setNext(((LNode<Double>)testINode.getChildren().get(1)));
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(0).getKeys().add(3.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		testINode.getChildren().get(1).getKeys().add(11.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		
		ArrayList<Double> testRange=testINode.getRange(0.00, 15.90);
		assertThat(testRange.size(), is(6));
		assertArrayEquals(testINode.getRange(0.00, 15.90).toArray(), testRange.toArray());
		
		testRange.clear();
		testRange=null;
	}
	
	@Test
	void lowerBoundTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		((LNode<Double>)testINode.getChildren().get(0)).setNext(((LNode<Double>)testINode.getChildren().get(1)));
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(0).getKeys().add(3.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		testINode.getChildren().get(1).getKeys().add(11.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		
		ArrayList<Double> testLowerBound=testINode.getLowerBound(4.50);
		assertThat(testLowerBound.size(), is(2));
		assertArrayEquals(testINode.getLowerBound(4.50).toArray(), testLowerBound.toArray());
		
		testLowerBound.clear();
		testLowerBound=null;
	}
	
	@Test
	void upperBoundTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		((LNode<Double>)testINode.getChildren().get(0)).setNext(((LNode<Double>)testINode.getChildren().get(1)));
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(0).getKeys().add(3.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		testINode.getChildren().get(1).getKeys().add(11.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		
		ArrayList<Double> testUpperBound=testINode.getUpperBound(12.0);
		assertThat(testUpperBound.size(), is(0));
		assertArrayEquals(testINode.getUpperBound(12.0).toArray(), testUpperBound.toArray());
		
		testUpperBound.clear();
		testUpperBound=null;
	}
	
	@Test
	void printAllKeysTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		((LNode<Double>)testINode.getChildren().get(0)).setNext(((LNode<Double>)testINode.getChildren().get(1)));
		
		for(int index=0; index<4000000; ++index)
		{
			testINode.insertKey(11.0);
		}
		
		assertThrows
		(
			StackOverflowError.class, ()-> 
			
			{
				testINode.printAllKeys();
			}
		);
	}
	
	@Test
	void clearTest()
	{
		testINode.clear();
		assertNull(testINode.getParent());
		assertThat(testINode.getCapacity(), is(0));
		assertNull(testINode.getKeys());
		assertNull(testINode.getChildren());
	}
	
	@Test
	void allKeysTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getKeys().add(5.0);
		((LNode<Double>)testINode.getChildren().get(0)).setNext(((LNode<Double>)testINode.getChildren().get(1)));
		testINode.getChildren().get(0).getKeys().add(4.50);
		testINode.getChildren().get(0).getKeys().add(3.50);
		testINode.getChildren().get(0).getKeys().add(2.50);
		testINode.getChildren().get(1).getKeys().add(6.78);
		testINode.getChildren().get(1).getKeys().add(11.78);
		testINode.getChildren().get(1).getKeys().add(10.78);
		
		for(int index=0; index<400; ++index)
		{
			testINode.insertKey(11.0);
		}
		
		ArrayList<Double> testAllKeys=testINode.getAllKeys();
		assertArrayEquals(testINode.getAllKeys().toArray(), testAllKeys.toArray());
		testAllKeys.clear();
		testAllKeys=null;
	}
	
	@Test
	void levelNodeKeysTest()
	{
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		testINode.getChildren().add(new LNode<Double>(3, testINode));
		
		int level=0;
		HashMap<Integer, ArrayList<ArrayList<Double>>> levelMap=new HashMap<Integer, ArrayList<ArrayList<Double>>>();
		testINode.getLevelNodeKeys(level, levelMap);
		
		assertThat(level, is(0));
		assertThat(levelMap.size(), is(2));
		assertThat(levelMap.get(0).size(), is(1));
		assertThat(levelMap.get(1).size(), is(2));
	}
}