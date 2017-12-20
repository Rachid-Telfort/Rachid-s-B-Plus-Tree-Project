package Main;

import bPlusTree.BPlusTree;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main 
{
	public static void main(String[] args) 
	{
		String userInput=null;
		String fileData=null;
		BufferedReader userInputReader=new BufferedReader(new InputStreamReader(System.in));
		BufferedReader dataFileReader=null;
		BPlusTree<Integer> sample=new BPlusTree<Integer>();
		
		System.out.println("Welcome to the sample B+ Tree program.\n");
		System.out.println("Now Please input the filename of the data you want to insert into the sample B+ Tree\n");
		
		try 
		{
			userInput=userInputReader.readLine();
			userInputReader.close();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		try 
		{
			dataFileReader=new BufferedReader(new FileReader(userInput));
			
			try 
			{
				while((fileData=dataFileReader.readLine())!=null)
				{
					sample.insertKey(Integer.parseInt(fileData));
				}
				
				dataFileReader.close();
			} 
			
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println();
		
		sample.printLevelOrder();
	}
}