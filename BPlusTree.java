package bPlusTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BPlusTree<T extends Comparable<T>>
{
	private int capacity;
	private Node<T> root;
	
	public BPlusTree()
	{
		this.setCapacity(3);
		this.setRoot(null);
	}
	
	public BPlusTree(int capacity)
	{
		if(capacity%2==0)
		{
			--capacity;
		}
		
		if(capacity<3)
		{
			capacity=3;
		}
		
		this.setCapacity(capacity);
		this.setRoot(null);
	}
	
	public BPlusTree(BPlusTree<T> original)
	{
		if(original!=null)
		{
			this.setCapacity(original.getCapacity());
			this.setRoot(null);
			
			ArrayList<T> allOriginalKeys=original.getAllKeys();
			
			if(allOriginalKeys!=null)
			{
				for(T originalKey: allOriginalKeys)
				{
					this.insertKey(originalKey);
				}
				
				allOriginalKeys.clear();
				allOriginalKeys=null;
			}
		}
		
		else
		{
			this.setCapacity(3);
			this.setRoot(null);
		}
	}
	
	private void setCapacity(int capacity)
	{
		this.capacity=capacity;
	}
	
	private int getCapacity()
	{
		return this.capacity;
	}
	
	private void setRoot(Node<T> root)
	{
		this.root=root;
	}
	
	private Node<T> getRoot()
	{
		return this.root;
	}
	
	public int getKeyLocation(T key)
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getKeyLocation(key);
		}
		
		return -1;
	}
	
	public void insertKey(T key)
	{
		if(this.getRoot()==null)
		{
			this.setRoot(new LNode<T>(this.getCapacity(), null));
		}
		
		this.getRoot().insertKey(key);
		
		//This happens if a split occurred at the root. In this case, the root steps up to become its parent,
		//effectively increasing the height of the B+ Tree by 1.
		if(this.getRoot().getParent()!=null)
		{
			this.setRoot(this.getRoot().getParent());
		}
	}
	
	public int searchKey(T key)
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().searchKey(key);
		}
		
		return -1;
	}
	
	public int removeKey(T key)
	{
		if(this.getRoot()!=null)
		{
			int removedStatus=this.getRoot().removeKey(key);
			
			//This happens if a merge occurred at the root. In this case, the root steps down to be its zeroth child, 
			//effectively decreasing the B+ Tree in height by 1.
			if(this.getRoot().getKeys().isEmpty()&&this.getRoot() instanceof INode<?>)
			{
				this.setRoot(((INode<T>)this.getRoot()).getChildren().get(0));
				this.getRoot().getParent().clear();
				this.getRoot().setParent(null);
			}
			
			else if(this.getRoot().getKeys().isEmpty())
			{
				this.getRoot().clear();
				this.setRoot(null);
			}
			
			return removedStatus;
		}
		
		return -1;
	}
	
	public ArrayList<T> getRange(T first, T last)
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getRange(first, last);
		}
		
		return null;
	}
	
	public ArrayList<T> getLowerBound(T key)
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getLowerBound(key);
		}
		
		return null;
	}
	
	public ArrayList<T> getUpperBound(T key)
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getUpperBound(key);
		}
		
		return null;
	}
	
	public void printAllKeys()
	{
		System.out.print("[");
		
		if(this.getRoot()!=null)
		{
			this.getRoot().printAllKeys();
		}
		
		System.out.print("]");
	}
	
	public void clear()
	{
		if(this.getRoot()!=null)
		{
			ArrayList<T> allKeys=this.getRoot().getAllKeys();
			for(T keyToClear : allKeys)
			{
				this.removeKey(keyToClear);
			}
			
			allKeys.clear();
			allKeys=null;
			
			//This is in case some keys are failed to be removed 
			//when removing all the keys from the B+ Tree
			if(this.getRoot()!=null)
			{
				this.getRoot().clear();
				this.setRoot(null);
			}
		}
	}
	
	public boolean isEmpty()
	{
		return this.getRoot()==null;
	}
	
	public ArrayList<T> getAllKeys()
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getAllKeys();
		}
		
		return null;
	}
	
	public BPlusTree<T> uniquate()
	{
		if(this.getRoot()!=null)
		{
			BPlusTree<T> uniquatedBPlusTree=new BPlusTree<T>();
			
			ArrayList<T> allKeys=this.getRoot().getAllKeys();
			HashSet<T> uniqueKeys=new HashSet<T>();
			uniqueKeys.addAll(allKeys);
			
			for(T uniqueKey : uniqueKeys)
			{
				uniquatedBPlusTree.insertKey(uniqueKey);
			}
			
			allKeys.clear();
			uniqueKeys.clear();
			
			allKeys=null;
			uniqueKeys=null;
			
			return uniquatedBPlusTree;
		}
		
		return null;
	}
	
	//This function returns an ArrayList of the keys in the B+ Tree in string format.
	//This was implemented so the B+ Tree could be used with other java libraries more easily.
	public String toString()
	{
		if(this.getRoot()!=null)
		{
			return this.getRoot().getAllKeys().toString();
		}
		
		return null;
	}
	
	public void modifyCapacity(int capacity)
	{
		if(capacity%2==0)
		{
			--capacity;
		}
		
		if(capacity<3)
		{
			capacity=3;
		}
		
		this.setCapacity(capacity);
		
		if(this.getRoot()!=null)
		{
			ArrayList<T> allKeys=this.getRoot().getAllKeys();
			this.clear();
			for(T key : allKeys)
			{
				this.insertKey(key);
			}
			allKeys.clear();
			allKeys=null;
		}
	}
	
	//This returns the height, i.e. how many levels it has, of the B+ Tree minus one
	//as a height of zero means the root is a leaf node.
	//I believe, on average, the height will be ~ log(n) with the base of the log being the capacity.
	//Also, a height of -1 means the B+ Tree is empty, i.e. it has no true height.
	public int getHeight()
	{
		if(this.getRoot()!=null)
		{
			int height=0;
			Node<T> heightNode=this.getRoot();
			while(heightNode instanceof INode<?>)
			{
				++height;
				heightNode=((INode<T>)heightNode).getChildren().get(0);
			}
			
			heightNode=null;
			
			return height;
		}
		
		return -1;
	}
	
	//This function prints the B+ Tree in terms of both internal node and leaf node keys in level order 
	//similar to breadth first search on a binary search tree.
	public void printLevelOrder()
	{
		if(this.getRoot()!=null)
		{
			HashMap<Integer, ArrayList<ArrayList<T>>> levelMap=new HashMap<Integer, ArrayList<ArrayList<T>>>();
			this.getRoot().getLevelNodeKeys(0, levelMap);
			
			int treeLevel=levelMap.size();
			for(int level=0; level<treeLevel; ++level)
			{
				for(int subLevel=0; subLevel<levelMap.get(level).size(); ++subLevel)
				{
					System.out.print(levelMap.get(level).get(subLevel).toString());
				}
				
				System.out.println();
			}
			
			System.out.println();
			
			levelMap.clear();
			levelMap=null;
		}
		
		//This is the case when one is trying to print an empty B+ Tree in level order.
		else
		{
			System.out.println("[]\n");
		}
	}
}