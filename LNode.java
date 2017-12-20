package bPlusTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class LNode<T extends Comparable<T>> extends Node<T>
{
	private LNode<T> next;
	
	LNode(int capacity, Node<T> parent) 
	{
		super(capacity, parent);
		this.setNext(null);
	}
	
	void setNext(LNode<T> next)
	{
		this.next=next;
	}
	
	LNode<T> getNext()
	{
		return this.next;
	}

	@Override int getKeyLocation(T key) 
	{
		return this.getKeys().indexOf(key);
	}

	@Override void insertKey(T key) 
	{
		this.getKeys().add(key);
		Collections.sort(this.getKeys());
		
		if(this.getKeys().size()>this.getCapacity())
		{
			this.split();
		}
	}

	@Override void split() 
	{
		//Make new Leaf Node and add half the keys from this Node to the new Leaf Node.
		LNode<T> newLeaf=new LNode<T>(this.getCapacity(), null);
		newLeaf.getKeys().addAll(this.getKeys().subList(this.getKeys().size()/2, this.getKeys().size()));
		
		//Link the new Leaf Node properly in the linked list at the leaf level of the B+ Tree.
		if(this.getNext()==null)
		{
			this.setNext(newLeaf);
		}
		
		else
		{
			newLeaf.setNext(this.getNext());
			this.setNext(newLeaf);
		}
		
		//Remove the previously transferred keys from the keys of this Node.
		this.getKeys().subList(this.getKeys().size()/2, this.getKeys().size()).clear();
		
		//Add the new Leaf Node to the current B+ Tree.
		//This if statement executes when this Node is the root of the B+ Tree.
		//Here, we set up a new Internal Node to be the parent of both this Node and the new Leaf Node and move the root up the B+ Tree after this executes.
		if(this.getParent()==null)
		{
			this.setParent(new INode<T>(this.getCapacity()+1, null));
			newLeaf.setParent(this.getParent());
			Collections.addAll(((INode<T>)this.getParent()).getChildren(), this, newLeaf);
			this.getParent().getKeys().add(newLeaf.getKeys().get(0));
			newLeaf=null;
		}
		
		//This executes when this Node is not the root of the B+Tree.
		//The new Leaf Node is added to this Node's parent's children at the correct position.
		//If this Node's parent overflows, the split mechanism moves up the B+ Tree.
		else
		{
			newLeaf.setParent(this.getParent());
			((INode<T>)this.getParent()).getChildren().add(((INode<T>)this.getParent()).getChildren().indexOf(this)+1, newLeaf);
			this.getParent().getKeys().add(newLeaf.getKeys().get(0));
			Collections.sort(this.getParent().getKeys());
			newLeaf=null;
			
			if(this.getParent().getKeys().size()>this.getParent().getCapacity())
			{
				this.getParent().split();
			}
		}
	}

	@Override void merge()
	{
		//This is the node that will be removed from the B+ Tree after all its keys have been transferred to another node.
		LNode<T> nodeToRemove=this.getNext();
		
		//This node will help with some of the merging operations.
		LNode<T> helperNode=null;
		
		//This ArrayList will help with some of the merging operations.
		//We transfer all the keys of the node we are removing to the helper array.
		ArrayList<T> helperArray=new ArrayList<T>();
		helperArray.addAll(nodeToRemove.getKeys());
		
		//We are removing the corresponding key from the parent that is used for finding the node to be removed. 
		//The index of the key to be removed from the parent is at the index of the node to remove -1 since for n keys
		//of the parent, there are n+1 children.
		this.getParent().getKeys().remove(((INode<T>)this.getParent()).getChildren().indexOf(nodeToRemove)-1);
		
		//We are re-chaining the leaf nodes before we remove the node to be removed.
		this.setNext(nodeToRemove.getNext());
		
		//We remove the node to be removed from the B+ Tree.
		((INode<T>)this.getParent()).getChildren().remove(nodeToRemove);
		
		//We now clear the node to be removed.
		nodeToRemove.clear();
		nodeToRemove=null;
		
		//Here we will merge the keys we got from the removed node to this node.
		//However, one could insert enough keys to split again so we need to check for that.
		boolean isSplit=false;
		for(T keyToMerge : helperArray)
		{
			if(!isSplit)
			{
				//We have not inserted enough keys into this node to split.
				if(this.getKeys().size()+1>this.getCapacity())
				{
					//Here, we are about to split so we need 
					//to switch to inserting keys to this node's right sibling, A.K.A, the helperNode.
					this.insertKey(keyToMerge);
					isSplit=true;
					helperNode=this.getNext();
				}
				
				else
				{
					this.insertKey(keyToMerge);
				}
			}
			
			else
			{
				//We have split and now inserting the remaining keys
				//from the helperArray into the helperNode.
				helperNode.insertKey(keyToMerge);
			}
		}
		
		if(helperNode!=null)
		{
			helperNode=null;
		}
		
		//We clear the helper array since we do not need it anymore.
		helperArray.clear();
		helperArray=null;
		
		//Now we check if the parent underflows.
		//If it does we carry the merge mechanism up to the parent.
		if(this.getParent().getParent()!=null&&this.getParent().getKeys().size()<this.getParent().getCapacity()/2)
		{
			//We check if the parent of this node is the last child of this nodes grandparent.
			//If it is we merge this nodes parent's left sibling.
			//Otherwise we merge this node's parent.
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
		if(this.getKeys().contains(key))
		{
			return 0;
		}
		
		return -1;
	}

	@Override int removeKey(T key) 
	{
		//This if statement is executed if the key desired to be removed is removed from the B+ Tree.
		if(this.getKeys().remove(key))
		{
			//We check for underflow and if this node has a parent.
			//If this node does not have a parent, then we cannot propagate the merge mechanism.
			if(this.getKeys().size()<this.getCapacity()/2+1&&this.getParent()!=null)
			{
				//Here we check if this node is the last child of its parent.
				//If it is, then we merge this node's left sibling.
				//Otherwise we merge this node.
				if(((INode<T>)this.getParent()).getChildren().get(((INode<T>)this.getParent()).getChildren().size()-1)==this)
				{
					((INode<T>)this.getParent()).getChildren().get(((INode<T>)this.getParent()).getChildren().indexOf(this)-1).merge();
				}
				
				else
				{
					this.merge();
				}
			}
			
			return 0;
		}
		
		return -1;
	}

	@Override ArrayList<T> getRange(T first, T last)
	{
		ArrayList<T> range=new ArrayList<T>();
		
		for(T keyInRange : this.getKeys())
		{
			//This checks if the key in the ArrayList is >= the first value in the range
			//and <= than the last value in the range.
			//If it is we add the key to the range list.
			if(keyInRange.compareTo(first)>=0&&keyInRange.compareTo(last)<=0)
			{
				range.add(keyInRange);
			}
		}
		
		//We check if this node's right sibling has any keys in the range.
		if(this.getNext()!=null)
		{
			range.addAll(this.getNext().getRange(first, last));
		}
		
		return range;
	}

	@Override ArrayList<T> getLowerBound(T lowerBoundKey) 
	{
		ArrayList<T> lowerBound=new ArrayList<T>();
		
		for(T keyInLowerBound : this.getKeys())
		{
			//This checks if the key in the ArrayList is < the lowerBoundKey argument.
			//If it is we add the key to the lowerBound list.
			if(keyInLowerBound.compareTo(lowerBoundKey)<0)
			{
				lowerBound.add(keyInLowerBound);
			}
		}
		
		//We check if this node's right sibling has any keys in the lower bound.
		if(this.getNext()!=null)
		{
			lowerBound.addAll(this.getNext().getLowerBound(lowerBoundKey));
		}
		
		return lowerBound;
	}

	@Override ArrayList<T> getUpperBound(T upperBoundKey) 
	{
		ArrayList<T> upperBound=new ArrayList<T>();
		
		for(T keyInUpperBound : this.getKeys())
		{
			//This checks if the key in the ArrayList is > the upperBoundKey argument.
			//If it is we add the key to the upperBound list.
			if(keyInUpperBound.compareTo(upperBoundKey)>0)
			{
				upperBound.add(keyInUpperBound);
			}
		}
		
		//We check if this node's right sibling has any keys in the upper bound.
		if(this.getNext()!=null)
		{
			upperBound.addAll(this.getNext().getUpperBound(upperBoundKey));
		}
		
		return upperBound;
	}

	@Override void printAllKeys() 
	{
		//This prints the ArrayList as a string without the opening an closing ArrayList brackets.
		System.out.print(this.getKeys().toString().substring(1, this.getKeys().toString().length()-1));
		
		//If there are more leaf nodes then we go on and print their values.
		if(this.getNext()!=null)
		{
			System.out.print(", ");
			this.getNext().printAllKeys();
		}
	}

	@Override void clear() 
	{
		this.setNext(null);
		this.setParent(null);
		this.setCapacity(0);
		
		if(this.getKeys()!=null)
		{
			this.getKeys().clear();
			this.setKeys(null);
		}
	}

	@Override ArrayList<T> getAllKeys() 
	{
		if(!this.getKeys().isEmpty())
		{
			ArrayList<T> allKeys=new ArrayList<T>();
			allKeys.addAll(this.getKeys());
			
			//If there are more leaf nodes then we add all the other leaf nodes' keys to this ArrayList.
			if(this.getNext()!=null)
			{
				allKeys.addAll(this.getNext().getAllKeys());
			}
			
			return allKeys;
		}
		
		return null;
	}

	@Override void getLevelNodeKeys(int level, HashMap<Integer, ArrayList<ArrayList<T>>> levelMap) 
	{
		//If the levelMap did not contain the height level before we add the height level to the map.
		if(!levelMap.containsKey(level))
		{
			levelMap.put(level, new ArrayList<ArrayList<T>>());
		}
		
		//Now we add the corresponding key list to the appropriate height level.
		//This is done by chaining.
		levelMap.get(level).add(this.getKeys());
	}
}