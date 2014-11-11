/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;



public class Isomorphism implements Runnable{
        
     Graphs tempGraph1;//graph to store the argument
     Graphs tempGraph2;//graph to store the argument
     double count;//count value to store the isomorphism count
     int[] combinations;
    public Isomorphism(Graphs graph1,Graphs graph2,int[] combination){
        
        tempGraph1 = graph1;
        tempGraph2 = graph2;
        combinations = combination;
     }
    public Isomorphism(){
        
    }
     @Override
    public void run()
    {
         count = subGraphIsomorphism(tempGraph1,tempGraph2,combinations);
        
    }

 /* This function tests if two graphs are isomers or not and returns the permutation for which it became isomers.
 * Input:-graph1 and graph2 of type Graph
 * Output:-returns the permtation for which the graphs became isomers
 * 
 */
      
static Permutation Testisomorphism(Graphs graph1,Graphs graph2,int[] combination)  
{   
    boolean comparison; //Its boolean variable used to store the value returned by the comparison function.
    int index=0; // This variable is used to start the permutation and is set to 0
    int nNodes = graph1.nNodes;
    int []nextpermutation = new int[nNodes]; //This is a one dimensional array to hold the permutation which is retrived from the permutation table,that store all the permuataion for the given number of nodes
    Graphs  graph3 = new Graphs(); //graph 3 will hold the trandformed grapah
    //System.out.println(nNodes);
    
    Permutation permuteobj = new Permutation(nNodes);   //Object of the permutation class.
     
   
    permuteobj.permutation = permuteobj.node_numbers;
       
       
       for(int k=0;k<permuteobj.nPermutations;k++)
       {
        Graphs tempgraph = new Graphs();
        tempgraph.nNodes =  graph2.nNodes;
        tempgraph.graph = graph2.graph;
           
          graph3 = transform(tempgraph,permuteobj.permutation);
         
          comparison = compare(graph1,graph3);
          
          if(comparison == true)
           {    
                return permuteobj;
           }   
         permuteobj.permutation = permuteobj.generateNextPermutation(permuteobj.permutation);    
       }
       
       return null; 
       
         
    }      
   
 /* This function transforms the given graph using a permutation matrix.
  * Input:-graph2 of type Graph and permutations for which the graph has to be transformed
  * Output:-graph2 transformed according to the given permutation.
  * 
  */  
    static Graphs transform(Graphs graph2,int[]permutations)
    {
        int nNodes = graph2.nNodes;  
        int [][] permutationMatrix = new int[nNodes][nNodes]  ;  //contains the permutation matrix for every permutation of nodes  
        int [][]rowPermutation = new int[nNodes][nNodes];        //contains the result after multiplying the permutation matrix and graph2.graph
        int [][]permutationMatrixTranspose = new int [nNodes][nNodes]; //contains the transpose of the permutation matrix
        int [][]transformedMatrix = new int [nNodes][nNodes];  //contains the final transformed graph
       
        for(int i=0;i<nNodes;i++)
        {
           for(int j=0;j<nNodes;j++)
           {
               permutationMatrix[i][j]=0;
           }    
        }    
        
        for(int j=0;j<nNodes;j++)
        {
              permutationMatrix[j][permutations[j]-1]=1;
        
        }    
        for(int i=0;i<nNodes;i++)
         {
           for(int j=0;j<nNodes;j++)
           {
               rowPermutation[i][j]=0;
               for(int k=0;k<nNodes;k++)
                {   
                    rowPermutation[i][j] = rowPermutation[i][j]+(permutationMatrix[i][k]*graph2.graph[k][j]);
           
                }      
               
           }    
          
         }  
         
         for(int i=0;i<nNodes;i++)
         {
          for(int j=0;j<nNodes;j++)
          {
           permutationMatrixTranspose[j][i] = permutationMatrix[i][j];
      
          }
         }
   
         for(int i=0;i<nNodes;i++)
         {
           for(int j=0;j<nNodes;j++)
           {
             transformedMatrix[i][j]=0;
             for(int k=0;k<nNodes;k++)
             {
                 transformedMatrix[i][j] = transformedMatrix[i][j]+rowPermutation[i][k]*permutationMatrixTranspose[k][j];
         
             }        
           }
          }
         graph2.graph = transformedMatrix;
        
     return graph2;
    }   
   
    /* This function is used to compare the adjacency matix of the graph1 and graph2
    * Input:-The graph graph2 and graph1.
    * Output:-True if the adjacency matrix of graph1 and graph2 are same, False if both are not same..
    * 
    */ 
    
    static boolean compare(Graphs g1,Graphs g2)
    {
      
          for(int m=0;m<g1.nNodes;m++)
          {
            for(int n=0;n<g1.nNodes;n++)
            {
                if(g1.graph[m][n]!=g2.graph[m][n])
                {
                    
                    return false;     
                }    
                              
           }    
                    
          }
     
     return true;    
     
    }  
    
    /* This function counts the number of subgraphs in a graph
     * Input: g1, the graph with large number of nodes
     * 
     */
    
   static int  subGraphIsomorphism(Graphs g1,Graphs g2,int[]combinations)//g1 is largegraph and g2 is small graph
   {
      boolean comparison=false;
      int counter = 0; 
      comparison = compareGraphs(combinations,g1,g2);
      
      if(comparison == true)
      {
        counter++;
      }
       return counter;
     
   }
   
   
  static boolean compareGraphs(int []combination,Graphs largeGraph,Graphs smallGraph )
  {
     Graphs graph4 = new Graphs();
     graph4.nNodes = smallGraph.nNodes;
     graph4.graph = new int[graph4.nNodes][graph4.nNodes];
     for(int i=0;i<smallGraph.nNodes;i++)
     {
       int element1 = combination[i]-1;
       for(int j=0;j<smallGraph.nNodes;j++)
       {
          int element2  = combination[j]-1;
           graph4.graph[i][j] = largeGraph.graph[element1][element2]; 
            
       }    
        
     } 
     
     if( Testisomorphism( smallGraph,graph4,combination) != null) 
     {    
      return true;
     }
     return false;
   
   }        

    
    
    
    
    
}
