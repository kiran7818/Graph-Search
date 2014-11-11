/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;


import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;


public class GraphletIsomorphism {
    
  class Records{
        
        String graphString;//string repersentation of the adjacency matrix of the permuted graphlets
        int index;//graphlet index for repersenting the 11 graphlets
        int intValue;//integer value of the graphstring
        public Records(String str, int index)
        {
            this.graphString = str;
            this.index = index;
            this.intValue = this.stringToInt(this.graphString);
        }
       
        public Records()
        {
            
        }    
        public Records(String str)
        {
            this.graphString = str;
            this.intValue = this.stringToInt(this.graphString);
            
        }      
        public int stringToInt(String str)//given str this function returns the integer value of that string (binary string)
        {
         int bucketindex = 0;
         int index = 0;
        
         for(int i = str.length()-1,j=0;i>=0;--i,++j)
         {
            index = 1<<j;
            bucketindex = bucketindex+index*Integer.parseInt(""+str.charAt(i));
                 
         }
         return bucketindex;
      }        
        
    }


    Hashtable<Integer,Records> buckets;//hashtable to store the graphlet strings
    public GraphletIsomorphism(String filename,int size)
    {
      buckets = new Hashtable<Integer,Records>(1000);
      
      Records[] recs = new Records[64];//recs will have number of permuted graphs temporary storage
      String line = new String();
      int counter = 0;
      String[] lineValue = new String[2];
      try {
             Path path = new Path(filename);
	     FileSystem fs = FileSystem.get(new Configuration());
	     Scanner in = new Scanner(new InputStreamReader(fs.open(path)));		
             while(in.hasNextLine())
             {
                 line = in.nextLine();//for each line of input text filSystem.out.println("hash code"+this.graphString.hashCode());e 
	         lineValue = line.split("\t");//split the file by tab
                 recs[counter] = new Records(lineValue[0],Integer.parseInt(lineValue[1]));
                 ++counter;
            }    
            
         } catch (Exception ex) {}
        
     // this.scanAndSort(recs);
      
    // for(int j = 0;j<buckets.length;j++)
     //{
      for(int i = 0;i<recs.length;i++) //load the hash table with the permutedgraphs
      {
         Integer temp = recs[i].intValue;
         buckets.put(temp.hashCode(),recs[i]);
      }
     //}  
        
    /* Enumeration names = buckets.keys();
      while(names.hasMoreElements())
      {
            Integer temp = (Integer)names.nextElement();
            System.out.print(temp.hashCode()+":");
            System.out.println(buckets.get(temp).graphString);
      }    
    //  */      
    }
    public GraphletIsomorphism()
    {
        
    }         
    public GraphletIsomorphism(String filename)
    {
      buckets = new Hashtable<Integer,Records>(1000);//a hash table to store all the permuted graphs
      Records[]recs = new Records[64];//recs will have number of permuted graphs temporary storage
      String line = new String();
      int counter = 0;
     // System.out.println("file name is"+filename);
      String[] lineValue = new String[2];
      try {
             Scanner in = new Scanner(new File(filename));
             while(in.hasNextLine())
             {
                    line = in.nextLine();
                    lineValue = line.split("\t");//split the file by tab
                    recs[counter] = new Records(lineValue[0],Integer.parseInt(lineValue[1]));
                    ++counter;
            }    
            
         } catch (Exception ex) {}
        
     // this.scanAndSort(recs);
      
     
      for(int i = 0;i<recs.length;i++) //load the hash table with the permutedgraphs
      {
         Integer temp = recs[i].intValue;
         buckets.put(temp.hashCode(),recs[i]);
      }  
         
     Enumeration names = buckets.keys();
    
  }
    public void scanAndSort(Records[] recs)
    {
        for(int i = 1;i<recs.length;i++)
        {
            Records key = recs[i];
            int j = i-1;
            while(j>=0&&key.intValue<recs[j].intValue)
            {
                Records temp = recs[j];
                recs[j] = recs[j+1];
                recs[j+1] = temp;
                --j;
            }    
            recs[j+1] = key;
        }    
      
        for (int i = 0; i < recs.length; i++) 
        {
            boolean isInArray = false;
            for (int j = 0; (j < i) && (!isInArray); j++) 
            {
                if (recs[j].intValue == recs[i].intValue) 
                {
                    isInArray = true;
                }
            }
            if (!isInArray) 
            {
                System.out.print(recs[i].graphString+"\t"+recs[i].index);
                System.out.println();
            }
        //    */ 
    }
        
    }        
   
     public int checkIsomorphism(Graphs g1,int[] com)
     {
         String graphstring = g1.toString();
         Records graphrecord = new Records(graphstring);
         int match = -1;
         Integer temp = graphrecord.intValue;
	 if( this.buckets.containsKey(temp.hashCode()))
         {
	      match = this.buckets.get(temp.hashCode()).index;
         }   
         return match;
     }        
     
     
   int compareGraphs(int []combination,Graphs largeGraph,GraphletIsomorphism iso)
   {
     Graphs graph4 = new Graphs();
     graph4.nNodes = combination.length;
     graph4.graph = new int[graph4.nNodes][graph4.nNodes];
     for(int i=0;i<combination.length;i++)
     {
       int element1 = combination[i]-1;
       for(int j=0;j<combination.length;j++)
       {
           int element2  = combination[j]-1;
           graph4.graph[i][j] = largeGraph.graph[element1][element2]; 
       }    
     } 
     
    return iso.checkIsomorphism(graph4,combination);
   
   }        
   
  
}
	
