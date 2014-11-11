/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;

public class Graphs extends LSHElem{
    
  int nNodes;//number of nodes of the graph
  int[][]graph ;//the adjacency matrix of the graph
  vector graphvec;//graphlet vector of the corresponding graph
  String id;//filename for using in the hadoop mode.
  //filename is the file which contains the adjacency matrix of the graph, impOption decides the methods of implementation of finding vector
  //1:- Use hadoop for finding the grahletvector
  //2:- Use non-distributed method for finding graphletvector
  public Graphs(String filename,char mode)
  {
	id = filename;
      try{
	   Scanner in = utilities.defineScanner(filename,mode);
	   this.readGraph(in);
	 }catch(Exception e){}
	 
  }       
 //function to read the graph from the text file 
  public void readGraph(Scanner in)
  {
      try{
	  if(in.hasNextInt())
          {    
            this.nNodes = in.nextInt();
          }
	  
	  graph = new int[nNodes][nNodes];
          while(in.hasNextInt())
          {
            for(int i=0;i<nNodes;i++)
            {
              for(int j=0;j<nNodes;j++)
              {
                 this.graph[i][j] = in.nextInt();
              }    
            }    
          }  
          
      }catch(Exception e){}
      
  }     
  public Graphs(int nNodes)//constructor with the argument as the size of the graph
  {
        this.nNodes = nNodes;
        this.graph = new int[nNodes][nNodes];  
        try {
            graphvec = GraphEmbed.toVector(this);
        } catch (InterruptedException ex) {}
	
        
  }
  public Graphs()
  {
        
  }

  public Graphs(String Filename,int numberOfGraphlets)//overloaded constructor for the 11 non distinct graphs
  {
     try
     {
       File file = new File(Filename);
       Scanner input1 = new Scanner(file);
       if(input1.hasNextInt())
       {    
          nNodes = input1.nextInt();
       }
       graph = new int[nNodes][nNodes];
       while(input1.hasNextInt())
       {
         for(int i=0;i<nNodes;i++)
         {
           for(int j=0;j<nNodes;j++)
           {
               graph[i][j] = input1.nextInt();
           }    
         }    
       }  
     }catch(Exception e){}  
       
  }
  public Graphs(Graphs g)
  {
     this.nNodes = g.nNodes;
       
  }
  public void getVector(String filename, char mode)
  {
    try{
	if(mode == 'a')//a-automatic, whereby if node size>300 it goes for hadoop implementation else it goes for normal /////////implementation

	{
	   if(this.nNodes>300)
	   {
		String hdfsfile = utilities.copyFile(filename);
		this.graphvec = GraphEmbed.toVector(hdfsfile,this.nNodes);
	   }
	   else
	   {
		this.graphvec = GraphEmbed.toVector(this);
		
	   }
	}
	else if(mode == 'h')
	{
		String hdfsfile = utilities.copyFile(filename);
		this.graphvec = GraphEmbed.toVector(hdfsfile,this.nNodes);
	}
	else
	{
		
		this.graphvec = GraphEmbed.toVector(this);
		
	}
       }catch(Exception e){}
  }
    /*Method to print the graph elements*/
  @Override
  void printElement() 
  {
    for(int i = 0;i<this.nNodes;i++)
    {
      for(int j = 0;j<this.nNodes;j++)
      {
          System.out.print(this.graph[i][j]+" ");
      }    
      System.out.println(" ");
    }
      
  }
//string repersentation of the graph
  public String toString()
  {
     StringBuilder graphstring = new StringBuilder();
       
     for(int i = 0;i<this.nNodes;i++)
     {
       for(int j = 0;j<this.nNodes;j++)
       {
         graphstring.append(this.graph[i][j]);
       }    
     }    
     String str = new String(graphstring);
     return str;
  }        
}
