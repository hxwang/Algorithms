/*The file contains the adjacency list representation of a simple undirected graph. There are 200 vertices labeled 1 to 200. The first column in the file represents the vertex label, and the particular row (other entries except the first column) tells all the vertices that the vertex is adjacent to. So for example, the 6th row looks like : "6	155	56	52	120	......". This just means that the vertex with label 6 is adjacent to (i.e., shares an edge with) the vertices with labels 155,56,52,120,......,etc
Your task is to code up and run the randomized contraction algorithm for the min cut problem and use it on the above graph to compute the min cut. (HINT: Note that you'll have to figure out an implementation of edge contractions. Initially, you might want to do this naively, creating a new graph from the old every time there's an edge contraction. But you should also think about more efficient implementations.) (WARNING: As per the video lectures, please make sure to run the algorithm many times with different random seeds, and remember the smallest cut that you ever find.) Write your numeric answer in the space provided. So e.g., if your answer is 5, just type 5 in the space provided.
*/

/*******************
*    Coursera HW   *
*                  *
*******************/

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class kargerMinCut {

	/**
	 * Edge: each edge contain two points(firstPoint & secondPoint) 
	 */
	public class Edge{
		int firstPoint;
		int secondPoint;
	}
	
	/**
	 * 
	 * @return EdgeList
	 * @throws IOException
	 */
	ArrayList<Edge> readFile() throws IOException
	{
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		try {
			/*
			 * Read file
			 */
			FileInputStream fstream = new FileInputStream("C:\\Users\\hwang14\\Desktop\\kargerMinCut.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while((strLine = br.readLine())!=null)
			{
				//use split to get data
				String[] points = strLine.split("\t");
				int len = points.length;
				int pointsFirst = Integer.parseInt(points[0]);
				for(int i=1; i<len; i++)
				{
					Edge newedge = new Edge();
					int pointsSecond = Integer.parseInt(points[i]);
					newedge.firstPoint = pointsFirst;
					newedge.secondPoint = pointsSecond;
					edgeList.add(newedge);
				}
			}
		} catch (FileNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return edgeList;
		
	}
	  
	  public int subGetMinCut(ArrayList<Edge> OriginedgeList, ArrayList<Integer> OriginvertexList, int loop)
	  {
		  int min=1000000;
		  Random random = new Random(System.currentTimeMillis());
		  for(int k=0; k<loop; k++)
		  {
			  ArrayList<Edge> edgeList = (ArrayList<Edge>) OriginedgeList.clone();
			  ArrayList<Integer> vertexList = (ArrayList<Integer>) OriginvertexList.clone();
			  while(vertexList.size()>2)
			  {
					/*
					 * targetIndex: the index of edge in edgeList to be contracted
					 */
					int targetIndex = random.nextInt(edgeList.size());			
					Edge targetEdge = edgeList.get(targetIndex);
					int targetVertex = targetEdge.firstPoint;
					int deleteVertex = targetEdge.secondPoint;
									
					/*
					 * delete edges whose two vertice are the vertice of targetEdge
					 */
					for(int i=0; i<edgeList.size();i++)
					{
						Edge candidateEdge = edgeList.get(i);
						if(candidateEdge.firstPoint == targetVertex && candidateEdge.secondPoint == deleteVertex 
						|| candidateEdge.secondPoint == targetVertex && candidateEdge.firstPoint == deleteVertex)
						{
							edgeList.remove(candidateEdge);
							i--;
						}
					}
					
					/*
					 * update edge to replace deleteVertex with targetVertex
					 */
					for(int i=0; i<edgeList.size(); i++)
					{
						Edge candidateEdge = edgeList.get(i);
						if(candidateEdge.firstPoint==deleteVertex)
						{
							candidateEdge.firstPoint = targetVertex;
						}
						if(candidateEdge.secondPoint==deleteVertex)
						{
							candidateEdge.secondPoint = targetVertex;
						}								
					}
					
					/*
					 * remove deleteVertex from vertexList
					 */
					vertexList.remove(vertexList.indexOf(deleteVertex));
				}
			  /*
			   * calculate the min-cut whose value equals the number of remaining edges
			   */
			  if(edgeList.size()<min) min = edgeList.size();
			  System.out.println("Current min-cut= "+edgeList.size());
		  }
		  return min;
	  }
	 
	  public int getMinCut(int loop) throws IOException
	  {
		    
			kargerMinCut mincut = new kargerMinCut();
			ArrayList<Edge> edgeList = new ArrayList<Edge>();
			ArrayList<Integer> vertexList = new ArrayList<Integer>();
			edgeList = mincut.readFile();
			int len = edgeList.size();
			/*
			 * create vertexList
			 */
			for(int i=0; i<len; i++)
			{
				Edge newedge = edgeList.get(i);
				int firstPoint = newedge.firstPoint;
				int secondPoint = newedge.secondPoint;
				if(!vertexList.contains(firstPoint)) vertexList.add(firstPoint);
				if(!vertexList.contains(secondPoint)) vertexList.add(secondPoint);		
			}
			
			return subGetMinCut(edgeList, vertexList, loop);
		  
	  }
	
	public static void main(String[] args) throws IOException {
		kargerMinCut mincut = new kargerMinCut();
		int min = 10000;
		/*
		 * since the graph is undirected, the result needs to devide 2
		 */
		for(int i=0;i<1;i++)
			min = Math.min(min, mincut.getMinCut(200)/2);
		
		System.out.println("min-cut="+min);
		
	}

}
