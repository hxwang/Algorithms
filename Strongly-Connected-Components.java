/****************
*  Coursera HW  *
****************/



/*
Download the text file here. Zipped version here. (Right click and save link as)
The file contains the edges of a directed graph. Vertices are labeled as positive integers from 1 to 875714. Every row indicates an edge, the vertex label in first column is the tail and the vertex label in second column is the head (recall the graph is directed, and the edges are directed from the first column vertex to the second column vertex). So for example, the 11th row looks liks : "2 47646". This just means that the vertex with label 2 has an outgoing edge to the vertex with label 47646

Your task is to code up the algorithm from the video lectures for computing strongly connected components (SCCs), and to run this algorithm on the given graph. 

Output Format: You should output the sizes of the 5 largest SCCs in the given graph, in decreasing order of sizes, separated by commas (avoid any spaces). So if your algorithm computes the sizes of the five largest SCCs to be 500, 400, 300, 200 and 100, then your answer should be "500,400,300,200,100". If your algorithm finds less than 5 SCCs, then write 0 for the remaining terms. Thus, if your algorithm computes only 3 SCCs whose sizes are 400, 300, and 100, then your answer should be "400,300,100,0,0".

WARNING: This is the most challenging programming assignment of the course. Because of the size of the graph you may have to manage memory carefully. The best way to do this depends on your programming language and environment, and we strongly suggest that you exchange tips for doing this on the discussion forums.

*/


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;


public class SCC {
	
	public void readFile(Hashtable<Integer, HashSet<Integer>> origin, Hashtable<Integer, HashSet<Integer>> reverse) throws NumberFormatException, IOException
	{
		FileInputStream fstream = new FileInputStream("/home/Desktop/SCC.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		while( (strLine = br.readLine())!=null)
		{
			String[] vertice = strLine.split(" ");
			int first = Integer.parseInt(vertice[0]);
			int second = Integer.parseInt(vertice[1]);
			HashSet<Integer> hs;
			if(origin.containsKey(first))
			{
				hs = origin.get(first);
			}else
			{
				hs = new HashSet<Integer>();
				origin.put(first, hs);
			}
			hs.add(second);
			
			HashSet<Integer> reverseHs;
			if(reverse.containsKey(second))
			{
				reverseHs = reverse.get(second);
			}else
			{
				reverseHs = new HashSet<Integer>();
				reverse.put(second, reverseHs);
			}
			reverseHs.add(first);			
		}	
	}
	
	public void getSCC() throws NumberFormatException, IOException
	{
		System.out.println("Reading file");
		Hashtable<Integer, HashSet<Integer>> origin = new Hashtable<Integer, HashSet<Integer>>();
		Hashtable<Integer, HashSet<Integer>> reverse = new Hashtable<Integer, HashSet<Integer>>();
		readFile(origin, reverse);
		ArrayList<Integer> finishTime = new ArrayList<Integer>();
		HashSet<Integer> visitedList = new HashSet<Integer>();
		
		System.out.println("Running DFS1");
		//DFS 1
		for(Integer s : reverse.keySet())
		{
			if(visitedList.contains(s)) continue;
			//System.out.println("Visting " + s);
			DFS1(s, reverse, finishTime, visitedList);
			finishTime.add(s);
		}
		
		visitedList.clear();
		
		ArrayList<Integer> count = new ArrayList<Integer>();
		
		System.out.println("Running DFS2");
		//DFS 2
		for(int i=finishTime.size()-1;i>=0;i--)
		{
			int index = finishTime.get(i);
			//System.out.println(index);
			if(visitedList.contains(index)) continue;
			ArrayList<Integer> SCC = new ArrayList<Integer>();;
			//System.out.println("DFS2 Visting " + index);
			SCC.add(index);
			DFS2(index, origin, SCC, visitedList);
			count.add(SCC.size());
			//System.out.println("SCC size = " + SCC.size());
		}
		
		Collections.sort(count);
		
		for(int i=0;i<Math.min(count.size(), 5);i++)
		{
			System.out.println(count.get(count.size()-i-1));
		}
	}
	
	public void DFS1(Integer root, Hashtable<Integer, HashSet<Integer>> graph, ArrayList<Integer> finishTime, HashSet<Integer> visitedList )
	{
		visitedList.add(root);
		if(graph.containsKey(root))
		{
			for(Integer k : graph.get(root))
			{
				if(visitedList.contains(k)) continue;
			//	System.out.println("DFS " + k);
				DFS1(k, graph, finishTime, visitedList);
			//	System.out.println("DFS finished add " + k);
				finishTime.add(k);
			}
		}
	}
	
	public void DFS2(Integer root, Hashtable<Integer, HashSet<Integer>> graph, ArrayList<Integer> SCC, HashSet<Integer> visitedList )
	{
		visitedList.add(root);
		if(graph.containsKey(root))
		{
			for(Integer k : graph.get(root))
			{
				if(visitedList.contains(k)) continue;
				SCC.add(k);
				DFS2(k, graph, SCC, visitedList);
			}
		}
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		SCC scc = new SCC();
		scc.getSCC();
	}
	

}


