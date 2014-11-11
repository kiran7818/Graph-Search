/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;


public class GopsGraph extends LSHOps {
    
    public GopsGraph(){
      
    }
    
  /*This function generates the random graphs needed for the calculation of location in the LSH hashtable*/  
    @Override
    vector generateRandom(LSHElem element) 
    {
      Graphs tempGraph = (Graphs)element;//tempGraph holds the arguments passed while calling this method in the LSHhashTable 
     
      //initialization of the random graph....   
      Graphs randomGraph = new Graphs(tempGraph);
      Random rand = new Random(); //create the random object
      double probability = 0.5;//the probability with which the edges will be present
      int flag = 0;//flag value is used to indicate the presence of the edge
   //generation of the random graph....   
      for(int i=0;i<randomGraph.nNodes;i++)//for every pair of nodes check the probability of edge forming
      {
         for(int j=i+1;j<randomGraph.nNodes;j++)
         {
             /*if the random number generated is less than the probability then there will be edge between that pair of vertices*/
               double randomValue = rand.nextFloat();//generate the random values
               if(randomValue >= probability)
               {    
                   flag = 0;
               }    
               else
               {    
                   flag = 1;
               }
               randomGraph.graph[i][j] = flag;//assign the flag value to indicate the presence of the edges
               if(randomGraph.graph[i][j] == 1)//for undirected graph if there is edge between a and b then there will be edge between b and a
               {   
                   randomGraph.graph[j][i] = 1;
               } 
        }    
     }
  //embedding the random graph to the vector space and the graphvec will have the vector corresponding to the random graph    
     try {
            randomGraph.graphvec = GraphEmbed.toVector(randomGraph);
        } catch (InterruptedException ex) {ex.printStackTrace();}
     
        randomGraph.graphvec.normalize(); 
     
        return randomGraph.graphvec;
    }

    @Override
   /*Method to calculate the dot product of the graphs by embedding the graphs onto the vector space */         
   double dotProduct(LSHElem element1, LSHElem element2) 
    {
        vector graphElement1 = (vector)element1;
        Graphs graphElement2 = (Graphs)element2;
        double sum = 0.0;
        sum = graphElement1.dotProduct(graphElement2.graphvec);//the inner product of the two vector is calculated and stored in sum
        return sum;
    }
   
}
