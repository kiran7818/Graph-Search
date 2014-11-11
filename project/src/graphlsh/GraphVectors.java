/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphVectors implements Runnable
{
  Graphs tempGraph;//to store the graph passed from graph embed
  int sNodes;//search node , which is taken as the root to calculate the bfs
  int depths;//the depth
  vector resVector;//vector corresponding to each node of the graph
  GraphletIsomorphism isomorphism;//graphlet isomorphism object to acess the hash table that store the graphlet details
    public GraphVectors(Graphs g,int sNode, int depth, GraphletIsomorphism iso)
    {
        tempGraph = g;
        sNodes = sNode;
        depths = depth;
        isomorphism = iso;
    }         
    public void run()
    {
        Graphs resGraph;//it will contain the bfs graph from a purticular node..
        resGraph = BFS(tempGraph,sNodes,depths);
        resVector = optimizedVectorCount(resGraph,isomorphism);
        
    }
 /*Method to calculate Bfs of a graph from a node of that graph*/   
    static Graphs BFS(Graphs g,int searchnode,int depth)//
    {
        queue queue = new queue(g.nNodes); //create a queue object
        int numberOfNodes = g.nNodes;//number of nodes of the graph
        int nodedepth=-1;//depth upto which we need to perform bfs
        int counter=0;//to indicate the number of nodes in the bfs graph
        int dequeelement=0;//element to be dequeued
        int [] accessStatus=new int[numberOfNodes]; //Status of the element
        for(int i=0;i<numberOfNodes;i++)
        {
            accessStatus[i]=0; //initalizing all with zero
    
        }  
		
        if(searchnode>=1 && searchnode<=numberOfNodes)//if the node is valid
        {
            queue.insert(searchnode-1);//insert the node to the queue
            queue.insert(-1);//insert -1 to queue as a delimiter to measure the depth
            accessStatus[searchnode-1] = 1;//mark the access status of the searchnode as visited
            int index = 0;
            while(queue.rear!= -1 && queue.front!=-1)//until the queue becomes empty
            {
                dequeelement= queue.delete();//dequeue
                if(dequeelement!=-1)
                {
                    queue.outArray[index] = dequeelement;//insert the search node into the array 
                    counter+=1;//for sorting
                    for(int j=0;j<numberOfNodes;j++)
                    {
                        if(g.graph[dequeelement][j]==1)//insert all the nodes of breadth wise in the queue and mark the access status as visited
                        {
                            if(accessStatus[j]==0)
                            {
                                queue.insert(j);
                                accessStatus[j]=1;
                            }
                        }
                    }
                    index++;
                }
                else//if we get -1 for deququing
                {
                    nodedepth = nodedepth+1;
                    if(nodedepth==depth)//checking code depth value whether it equates user depth or not!
                    {
                        break;
                    }
                    queue.insert(-1);	//q.insert
                }	
        
            }	
       
        }
        else
        {
            System.out.println("Searching element is not present!");        	

        }
        queue.sort(counter);
        Graphs bfsGraph=new Graphs();
        bfsGraph.nNodes = counter;
        bfsGraph.graph = new int[counter][counter];
        for(int i=0;i<counter;i++)//creating a graph out of the bfs tree
        {
            int element1=queue.outArray[i];
            for(int j=0;j<counter;j++)
            {
                int element2=queue.outArray[j];
                bfsGraph.graph[i][j]=g.graph[element1][element2];
            }	
        }		
   
        return bfsGraph;
    }
 
 static vector optimizedVectorCount(Graphs g,GraphletIsomorphism iso)
 {
       //System.out.println("the nodes in the bfs Graph is "+g.nNodes);
       
       int []firstCombination = new int[4];//array to hold the first combination
       int []nextCombinationArray = new int[4];//array to hold the new combinations
       for(int j=0;j<firstCombination.length;j++)//the first combination will be 1 2 3 4
       {    
          firstCombination[j] = j+1;
       } 
       nextCombinationArray = firstCombination;
       vector vectors = new vector(11);
       if(g.nNodes>=firstCombination.length)
       {    
               
            double sizeOfCombination = Combination.numberOfCombinations(g.nNodes,firstCombination.length);//total number of combinations
            for(int i = 0;i<sizeOfCombination;i++)
            {
                nextCombinationArray = Combination.nextCombination(nextCombinationArray,g.nNodes,firstCombination.length,i);
                int match2 = iso.compareGraphs(nextCombinationArray,g,iso);
                if(match2!=-1)
                {    
                    ++vectors.elements[match2];
                } 
            }
             return vectors;
       }    
       else
       {
	   return null;
       }    
       
     
       
 }   
 
 
}
