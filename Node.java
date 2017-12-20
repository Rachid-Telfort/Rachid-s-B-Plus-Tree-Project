package bPlusTree;

import java.util.ArrayList;
import java.util.HashMap;

abstract class Node<T extends Comparable<T>> 
{
	private int capacity;
	private ArrayList<T> keys;
	private Node<T> parent;
	
	Node(int capacity, Node<T> parent)
	{
		this.setCapacity(capacity);
		this.setKeys(new ArrayList<T>());
		this.setParent(parent);
	}
	
	void setCapacity(int capacity)
	{
		this.capacity=capacity;
	}
	
	int getCapacity()
	{
		return this.capacity;
	}
	
	void setKeys(ArrayList<T> keys)
	{
		this.keys=keys;
	}
	
	ArrayList<T> getKeys()
	{
		return this.keys;
	}
	
	void setParent(Node<T> parent)
	{
		this.parent=parent;
	}
	
	Node<T> getParent()
	{
		return this.parent;
	}
	
	//getKeyLocation returns the index in the key list that matches the key.
	//For LNodes, it returns i, the index of the key such that keys[i]==key, and -1 if the key cannot be found.
	//For INodes, it returns i, the index of the key such that keys[i-1]<=key<keys[i]; note the boundary conditions.
	abstract int getKeyLocation(T key);
	
	//insertKey inserts the key into the key list.
	abstract void insertKey(T key);
	
	//split is called when a key list overflows (i.e., it exceeds its capacity).
	//split splits a node into two nodes and pushes up a key to the caller's parent node if the caller has a parent.
	abstract void split();
	
	//merge is called when a key list underflows (i.e., its size is less than half its capacity).
	//merge merges two nodes into one node and removes a key from the caller's parent node if the caller has a parent.
	abstract void merge();
	
	//searchKey searches the key list for the key.
	//searchKey returns 0 if the search succeeds or -1 if it fails.
	abstract int searchKey(T key);
	
	//removeKey removes the key from the key list.
	//removeKey returns 0 if the removal succeeds or -1 if it fails.
	abstract int removeKey(T key);
	
	//getRange returns an ArrayList of all the keys in the key list in the range of [first, last].
	//getRange returns an empty ArrayList if no keys in the key list fit in the range.
	//NOTE: If the B+ Tree is empty, this function return null.
	abstract ArrayList<T> getRange(T first, T last);
	
	//getLowerBound returns an ArrayList of all the keys in the key list that is < key.
	//getLowerBound returns an empty ArrayList when no element in the key list < key.
	//NOTE: If the B+ Tree is empty, this function return null.
	abstract ArrayList<T> getLowerBound(T key);
	
	//getUpperBound returns an ArrayList of all the keys in the key list that is > key.
	//getUpperBound returns an empty ArrayList when no element in the key list > key.
	//NOTE: If the B+ Tree is empty, this function return null.
	abstract ArrayList<T> getUpperBound(T key);
	
	//printAllKeys prints out all the keys currently in the key list.
	//printAllKeys prints nothing if the key list is empty.
	abstract void printAllKeys();
	
	//clear set all a Node's attributes to null, enabling it to possibly be removed sooner by the garbage collector.
	abstract void clear();
	
	//getAllKeys returns an ArrayList of all the keys currently in the key list.
	//getAllKeys returns null if the key list is empty.
	//NOTE: If the B+ Tree is empty, this function return null.
	abstract ArrayList<T> getAllKeys();
	
	//getLevelNodeKeys puts a Node's height level as well as the key list into a HashMap.
	//This is used to uncover certain properties of Nodes.
	abstract void getLevelNodeKeys(int level, HashMap<Integer, ArrayList<ArrayList<T>>> levelMap);
}