/**
 * 
 */

package bPlusTree;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNull;
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

class LNodeTest 
{
	/**
	 * @throws java.lang.Exception
	 */
	
	LNode<String> testLNode;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception 
	{
		System.out.println("Entering LNode test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
		System.out.println("Exiting LNode test case...\n");
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception 
	{
		testLNode=new LNode<String>(3, null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterEach
	void tearDown() throws Exception 
	{
		testLNode.clear();
		testLNode=null;
	}

	@Test
	void capacityTest() 
	{
		testLNode.setCapacity(5);
		assertEquals(testLNode.getCapacity(), 5);
	}
	
	@Test
	void keysTest()
	{
		ArrayList<String> testKeys=new ArrayList<String>();
		for(int keyCount=0; keyCount<10; ++keyCount)
		{
			testKeys.add("LNodeTest!");
		}
		testLNode.setKeys(new ArrayList<String>(testKeys));
		assertArrayEquals(testLNode.getKeys().toArray(), testKeys.toArray());
		testKeys.clear();
		testKeys=null;
	}
	
	@Test
	void parentTest()
	{
		INode<String> testParent=new INode<String>(3, null);
		testLNode.setParent(testParent);
		assertEquals(testLNode.getParent(), testParent);
		testParent=null;
	}
	
	@Test
	void nextTest()
	{
		Node<String> testNext=new LNode<String>(3, null);
		testLNode.setNext((LNode<String>) testNext);
		assertEquals(testLNode.getNext(), testNext);
		testNext.clear();
		testNext=null;
	}
	
	@Test
	void keyLocationTest()
	{
		testLNode.getKeys().add("Isaac");
		testLNode.getKeys().add("Randy");
		testLNode.getKeys().add("Justin");
		testLNode.getKeys().add("Syed");
		testLNode.getKeys().add("Chuyun");
		testLNode.getKeys().add("Derek");
		
		assertEquals(testLNode.getKeyLocation("Chuyun"), 4);
		assertEquals(testLNode.getKeyLocation("Jammil"), -1);
	}
	
	@Test
	void insertKeyTest()
	{
		testLNode.insertKey("Yvenie");
		testLNode.insertKey("Yveniy");
		testLNode.insertKey("Jammil");
		
		assertTrue(testLNode.getKeys().get(0).equals("Jammil"));
		assertFalse(testLNode.getKeys().get(1).equals("Yveniy"));
	}
	
	@Test
	void splitTest()
	{
		testLNode.getKeys().add("a");
		testLNode.getKeys().add("b");
		testLNode.getKeys().add("c");
		testLNode.getKeys().add("d");
		testLNode.split();
		
		assertTrue(testLNode.getKeys().size()==2);
		assertTrue(testLNode.getNext()!=null);
		assertTrue(testLNode.getParent()!=null);
		assertTrue(testLNode.getNext().getKeys().size()==2);
		assertTrue(testLNode.getNext().getNext()==null);
		assertTrue(testLNode.getNext().getParent()!=null);
		assertEquals(testLNode.getParent(), testLNode.getNext().getParent());
		
		testLNode.getParent().clear();
		testLNode.getNext().clear();
	}
	
	@Test
	void mergeTest()
	{
		testLNode.getKeys().add("A");
		testLNode.getKeys().add("B");
		testLNode.getKeys().add("C");
		testLNode.getKeys().add("D");
		testLNode.split();
		
		testLNode.getKeys().remove(0);
		testLNode.merge();
		
		assertTrue(testLNode.getKeys().size()==3);
		assertTrue(testLNode.getNext()==null);
		assertTrue(testLNode.getParent().getKeys().size()==0);
		assertTrue(((INode<String>)testLNode.getParent()).getChildren().size()==1);
		
		testLNode.getParent().clear();
	}
	
	@Test
	void searchKeyTest()
	{
		testLNode.getKeys().add("C++");
		testLNode.getKeys().add("Java");
		testLNode.getKeys().add("Swift");
		testLNode.getKeys().add("Python");
		testLNode.getKeys().add("Go");
		testLNode.getKeys().add("C");
		testLNode.getKeys().add("Dart");
		
		assertThat(testLNode.searchKey("Python"), is(0));
		assertThat(testLNode.searchKey("JavaScript"), is(-1));
	}
	
	@Test
	void removeKeyTest()
	{
		testLNode.insertKey("Samus");
		testLNode.insertKey("Johnny");
		
		testLNode.removeKey("James");
		assertFalse(testLNode.getKeys().size()==1);
		
		testLNode.removeKey("Samus");
		assertFalse(testLNode.getKeys().size()==2);
	}
	
	@Test
	void rangeTest()
	{
		testLNode.getKeys().add("a");
		testLNode.getKeys().add("b");
		testLNode.getKeys().add("c");
		testLNode.getKeys().add("d");
		testLNode.getKeys().add("e");
		testLNode.getKeys().add("f");
		testLNode.getKeys().add("g");
		testLNode.getKeys().add("h");
		testLNode.getKeys().add("i");
		testLNode.getKeys().add("j");
		
		ArrayList<String> testRange=testLNode.getRange("c", "i");
		assertThat(testRange.size(), is(7));
		assertArrayEquals(testLNode.getRange("c", "i").toArray(), testRange.toArray());
		assertTrue(testRange.get(0)=="c");
		assertTrue(testRange.get(1)=="d");
		assertTrue(testRange.get(2)=="e");
		assertTrue(testRange.get(3)=="f");
		assertTrue(testRange.get(4)=="g");
		assertTrue(testRange.get(5)=="h");
		assertTrue(testRange.get(6)=="i");
		
		testRange.clear();
		testRange=null;
	}
	
	@Test
	void lowerBoundTest()
	{
		testLNode.getKeys().add("a");
		testLNode.getKeys().add("b");
		testLNode.getKeys().add("c");
		testLNode.getKeys().add("d");
		testLNode.getKeys().add("e");
		testLNode.getKeys().add("f");
		testLNode.getKeys().add("g");
		testLNode.getKeys().add("h");
		testLNode.getKeys().add("i");
		testLNode.getKeys().add("j");
		
		ArrayList<String> testLowerBound=testLNode.getLowerBound("f");
		assertThat(testLowerBound.size(), is(5));
		assertArrayEquals(testLNode.getLowerBound("f").toArray(), testLowerBound.toArray());
		assertTrue(testLowerBound.get(0)=="a");
		assertTrue(testLowerBound.get(1)=="b");
		assertTrue(testLowerBound.get(2)=="c");
		assertTrue(testLowerBound.get(3)=="d");
		assertTrue(testLowerBound.get(4)=="e");
		
		testLowerBound.clear();
		testLowerBound=null;
	}
	
	@Test
	void upperBoundTest()
	{
		testLNode.getKeys().add("a");
		testLNode.getKeys().add("b");
		testLNode.getKeys().add("c");
		testLNode.getKeys().add("d");
		testLNode.getKeys().add("e");
		testLNode.getKeys().add("f");
		testLNode.getKeys().add("g");
		testLNode.getKeys().add("h");
		testLNode.getKeys().add("i");
		testLNode.getKeys().add("j");
		
		ArrayList<String> testUpperBound=testLNode.getUpperBound("e");
		assertThat(testUpperBound.size(), is(5));
		assertArrayEquals(testLNode.getUpperBound("e").toArray(), testUpperBound.toArray());
		assertTrue(testUpperBound.get(0)=="f");
		assertTrue(testUpperBound.get(1)=="g");
		assertTrue(testUpperBound.get(2)=="h");
		assertTrue(testUpperBound.get(3)=="i");
		assertTrue(testUpperBound.get(4)=="j");
		
		testUpperBound.clear();
		testUpperBound=null;
	}
	
	@Test
	void printAllKeysTest()
	{
		for(int index=0; index<4000000; ++index)
		{
			testLNode.insertKey("a");
		}
		
		assertThrows
		(
			StackOverflowError.class, ()-> 
			
			{
				testLNode.printAllKeys();
			}
		);
	}
	
	@Test
	void clearTest()
	{
		testLNode.clear();
		assertNull(testLNode.getNext());
		assertNull(testLNode.getParent());
		assertTrue(testLNode.getCapacity()==0);
		assertNull(testLNode.getKeys());
	}
	
	@Test
	void allKeysTest()
	{
		for(int index=0; index<700; ++index)
		{
			testLNode.getKeys().add(Integer.toBinaryString(index));
		}
		
		ArrayList<String> testAllKeys=testLNode.getAllKeys();
		assertArrayEquals(testLNode.getKeys().toArray(), testAllKeys.toArray());
		testAllKeys.clear();
		testAllKeys=null;
	}
	
	@Test
	void levelNodeKeysTest()
	{
		int level=0;
		HashMap<Integer, ArrayList<ArrayList<String>>> levelMap=new HashMap<Integer, ArrayList<ArrayList<String>>>();
		testLNode.getLevelNodeKeys(level, levelMap);
		
		assertThat(level, is(0));
		assertThat(levelMap.size(), is(1));
		assertThat(levelMap.get(level).size(), is(1));
		assertThat(levelMap.get(level).get(0).size(), is(0));
	}
}