package bPlusTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class INode<T extends Comparable<T>> extends Node<T>
{
	private ArrayList<Node<T>> children;
	
	INode(int capacity, Node<T> parent) 
	{
		super(capacity, parent);
		this.setChildren(new ArrayList<Node<T>>());
	}
	
	void setChildren(ArrayList<Node<T>> children)
	{
		this.children=children;
	}
	
	ArrayList<Node<T>> getChildren()
	{
		return this.children;
	}

	@Override int getKeyLocation(T key) 
	{
		//This first checks if the key is in the key list.
		//If it is we just return the index of the key in the key list.
		//Otherwise, we search for the appropriate child and go down one level in the tree to repeat the process.
		if(this.getKeys().indexOf(key)==-1)
		{
			int index=0;
			while(index<this.getKeys().size()&&key.compareTo(this.getKeys().get(index))>=0)
			{
				++index;
			}
			
			return this.getChildren().get(index).getKeyLocation(key);
		}
		
		return this.getKeys().indexOf(key);
	}

	@Override void insertKey(T key) 
	{
		//This searches for the appropriate child to repeat the insert process.
		//We go down the tree until we reach a leaf node.
		//Once we reach a leaf node we insert the key into it.
		int index=0;
		while(index<this.getKeys().size()&&key.compareTo(this.getKeys().get(index))>=0)
		{
			++index;
		}

		this.getChildren().get(index).insertKey(key);
	}

	@Override void split()
	{
		//We first get the key that we will be moving up to this node's parent.
		T parentKey=this.getKeys().get(this.getKeys().size()/2);
		
		//We make a new internal node to be this node's right sibling.
		//We then move half of this node's keys and children to the new internal node.
		INode<T> newInternal=new INode<T>(this.getCapacity(), null);
		newInternal.getKeys().addAll(this.getKeys().subList(this.getKeys().size()/2+1, this.getKeys().size()));
		newInternal.getChildren().addAll(this.getChildren().subList(this.getChildren().size()/2, this.getChildren().size()));
		for(Node<T> child : newInternal.getChildren())
		{
			child.setParent(newInternal);
		}
		
		//We now remove the keys and children that were transfered to this node's right sibling from this node.
		this.getKeys().subList(this.getKeys().size()/2, this.getKeys().size()).clear();
		this.getChildren().subList(this.getChildren().size()/2, this.getChildren().size()).clear();
		
		//If this node doesn't have a parent, i.e. is the root,
		//we make a new internal node as the parent and move the root up the tree
		//after the split function is finished.
		//Otherwise we add the new internal node to this node's parent and
		//if this node's parent overflows we move the split mechanism up to this node's parent.
		if(this.getParent()==null)
		{
			this.setParent(new INode<T>(this.getCapacity(), null));
			newInternal.setParent(this.getParent());
			Collections.addAll(((INode<T>)this.getParent()).getChildren(), this, newInternal);
			this.getParent().getKeys().add(parentKey);
			newInternal=null;
		}
		
		else
		{
			newInternal.setParent(this.getParent());
			((INode<T>)this.getParent()).getChildren().add(((INode<T>)this.getParent()).getChildren().indexOf(this)+1, newInternal);
			this.getParent().getKeys().add(parentKey);
			Collections.sort(this.getParent().getKeys());
			newInternal=null;
			
			if(this.getParent().getKeys().size()>this.getParent().getCapacity())
			{
				this.getParent().split();
			}
		}
	}

	@Override void merge()
	{
		//This is the node that will be removed from the B+ Tree after all its keys and children have been transferred to another node.
		INode<T> nodeToRemove=((INode<T>)((INode<T>)this.getParent()).getChildren().get(((INode<T>)this.getParent()).getChildren().indexOf(this)+1));
		
		//This node will help with some of the merging operations.
		INode<T> helperNode=null;
		
		//These ArrayLists will help with some of the merging operations.
		//We transfer all the keys of the node we are removing to the helperKeyArray.
		//We also transfer all the children of the node we are removing to the helperChildrenArray.
		ArrayList<T> helperKeyArray=new ArrayList<T>();
		ArrayList<Node<T>> helperChildrenArray=new ArrayList<Node<T>>();
		
		//Here we add the key from the parent corresponding to the search of the nodeToRemove's keys to the helperKeyArray.
		helperKeyArray.add(this.getParent().getKeys().get(((INode<T>)this.getParent()).getChildren().indexOf(nodeToRemove)-1));
		
		helperKeyArray.addAll(nodeToRemove.getKeys());
		helperChildrenArray.addAll(nodeToRemove.getChildren());
		
		//Here we remove the key borrowed from the parent as well as the nodeToRemove from the parent of the nodeToRemove.
		this.getParent().getKeys().remove(((INode<T>)this.getParent()).getChildren().indexOf(nodeToRemove)-1);
		((INode<T>)this.getParent()).getChildren().remove(nodeToRemove);
		
		//Here we clear the nodeToRemove and set it to null to possibly be cleaned up by the garbage collector sooner.
		nodeToRemove.clear();
		nodeToRemove=null;
		
		boolean isSplit=false;
		for(T keyToMerge: helperKeyArray)
		{
			//This checks if the node that we are merging does not split again while merging as that can happen.
			if(!isSplit)
			{
				//If the node we are merging is about to split, then we switch the node to be merged to be the node we are merging's right sibling.
				if(this.getKeys().size()+1>this.getCapacity())
				{
					this.getKeys().add(keyToMerge);
					this.getChildren().add(helperChildrenArray.get(0));
					this.getChildren().get(this.getChildren().size()-1).setParent(this);
					helperChildrenArray.remove(0);
					this.split();
					isSplit=true;
					helperNode=((INode<T>)((INode<T>)this.getParent()).getChildren().get(((INode<T>)this.getParent()).getChildren().indexOf(this)+1));
				}
				
				//We are merging the keys and children to the node to be merged before a split can occur, if it occurs that is.
				else
				{
					this.getKeys().add(keyToMerge);
					this.getChildren().add(helperChildrenArray.get(0));
					this.getChildren().get(this.getChildren().size()-1).setParent(this);
					helperChildrenArray.remove(0);
				}
			}
			
			//Here we have done a split and now are inserting the rest of the children into the right sibling, helperNode, of the original node that was to be merged.
			else
			{
				helperNode.getKeys().add(keyToMerge);
				helperNode.getChildren().add(helperChildrenArray.get(0));
				helperNode.getChildren().get(helperNode.getChildren().size()-1).setParent(helperNode);
				helperChildrenArray.remove(0);
			}
		}
		
		//Here we are just clearing the remaining variables we do not need and set them to null to possibly be retrieved by the garbage collector sooner.
		if(helperNode!=null)
		{
			helperNode=null;
		}
		
		helperKeyArray.clear();
		helperKeyArray=null;
		helperChildrenArray.clear();
		helperChildrenArray=null;
		
		//Here, we check if the grandparent of the node we merged is not null, if it is then we cannot move the merge mechanism up the B+ Tree.
		//If not then we check if the parent underflows, i.e. its size < half its capacity, and then we move the merge mechanism up the B+ Tree.
		if(this.getParent().getParent()!=null&&this.getParent().getKeys().size()<this.getParent().getCapacity()/2)
		{
			//If the parent of the node to merge is its grandparent's last child, we merge the node preceding the parent,
			//otherwise we merge the parent.
			if(((INode<T>)this.getParent().getParent()).getChildren().get(((INode<T>)this.getParent().getParent()).getChildren().size()-1)==this.getParent())
			{
				((INode<T>)this.getParent().getParent()).getChildren().get(((INode<T>)this.getParent().getParent()).getChildren().indexOf(this.getParent())-1).merge();
			}
			
			else
			{
				this.getParent().merge();
			}
		}
	}

	@Override int searchKey(T key)
	{
		//This simply searches the appropriate child node to repeat the search process until a leaf node is reached.
		//Once a leaf node is reached, then we search the leaf node for the value and return 0 if the search succeeds or -1 if it fails.
		int index=0;
		while(index<this.getKeys().size()&&key.compareTo(this.getKeys().get(index))>=0)
		{
			++index;
		}
		
		return this.getChildren().get(index).searchKey(key);
	}

	@Override int removeKey(T key)
	{
		//This simply searches the appropriate child node to repeat the removal process until a leaf node is reached.
		//Once a leaf node is reached, then we search the leaf node for the value to be removed and return 0 if the removal succeeds or -1 if it fails.
		int index=0;
		while(index<this.getKeys().size()&&key.compareTo(this.getKeys().get(index))>=0)
		{
			++index;
		}
		
		return this.getChildren().get(index).removeKey(key);
	}

	@Override ArrayList<T> getRange(T first, T last) 
	{
		//This searches the appropriate child node for the starting key and repeats this process until a leaf node is reached.
		//Once a leaf node is reached then the reached node as well as all subsequent leaf nodes are searched for the values in the range [first, last].
		//Finally, an ArrayList is returned containing the values in the range.
		int index=0;
		while(index<this.getKeys().size()&&first.compareTo(this.getKeys().get(index))>=0)
		{
			++index;
		}
		
		return this.getChildren().get(index).getRange(first, last);
	}

	@Override ArrayList<T> getLowerBound(T key) 
	{
		//This searches the appropriate child node with the key to start the lower bound with until a leaf node is encountered.
		//Once a leaf node is encountered all keys < the target key argument is added to an ArrayList which is returned.
		
		return this.getChildren().get(0).getLowerBound(key);
	}

	@Override ArrayList<T> getUpperBound(T key) 
	{
		//This searches the appropriate child node with the key to start the upper bound with until a leaf node is encountered.
		//Once a leaf node is encountered all keys > the target key argument is added to an ArrayList which is returned.
		int index=0;
		while(index<this.getKeys().size()&&key.compareTo(this.getKeys().get(index))>=0)
		{
			++index;
		}
		
		return this.getChildren().get(index).getUpperBound(key);
	}

	@Override void printAllKeys() 
	{
		this.getChildren().get(0).printAllKeys();
	}

	@Override void clear() 
	{
		this.setParent(null);
		this.setCapacity(0);
		
		if(this.getKeys()!=null)
		{
			this.getKeys().clear();
			this.setKeys(null);
		}
		
		if(this.getChildren()!=null)
		{
			this.getChildren().clear();
			this.setChildren(null);
		}
	}

	@Override ArrayList<T> getAllKeys() 
	{
		return this.getChildren().get(0).getAllKeys();
	}

	@Override void getLevelNodeKeys(int level, HashMap<Integer, ArrayList<ArrayList<T>>> levelMap) 
	{
		//This maps the height level in the B+ Tree to the appropriate key lists to help print the B+ Tree in level-order.
		
		if(!levelMap.containsKey(level))
		{
			levelMap.put(level, new ArrayList<ArrayList<T>>());
		}
		
		levelMap.get(level).add(this.getKeys());
		
		for(Node<T> child : this.getChildren())
		{
			child.getLevelNodeKeys(level+1, levelMap);
		}
	}
}