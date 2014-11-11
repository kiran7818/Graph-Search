/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.io.*;
import java.util.*;
import javax.tools.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphEmbed {
 
  /*Method to sum all the vectors corresponding to each node of the graph and to form a new vector*/  
  static vector toVector(Graphs g)throws InterruptedException
  { 
    vector[] vectors = new vector[g.nNodes];
    vectors = GraphEmbed.optimized(g);//vectors is the array that will hold the vectors
   
    vector v = new vector(11);
    for(int i =0;i<g.nNodes;i++)
    { 
       v = v.addVectors(vectors[i]);   //add all the vectors 
          
    } 
    v.normalize();
	//saving the vector into the file
    String vecFileName = utilities.outputFileName(g.id);
    BufferedWriter writer = null;
    try{
        File file = new File("/usr/local/hadoop/project/output/vectors/"+vecFileName+".vec");
        file.createNewFile();
        writer = new BufferedWriter(new FileWriter(file));
	writer.write("graphvector is=");
        for(int j = 0;j<v.size;j++)
        {
                writer.write(v.elements[j]+" ");
        }
        writer.newLine();  
	//graph vector from each node      
	for(int i = 0;i<vectors.length;i++)
        {
            writer.write("vector for node"+i+"=");
            vectors[i].normalize();
            for(int j = 0;j<vectors[i].size;j++)
            {
                writer.write(vectors[i].elements[j]+" ");
            }    
            writer.newLine();
        }   
        writer.close();
        
    }catch(Exception e){}

         
    return v;
  }
  /*This function calculates the graphlet vector from each node of the graph, it is stored in an array,whose size is the number of   nodes in the graph*/
  public static vector[] optimized(Graphs g1)
  {
       vector[] vectors = new vector[g1.nNodes];
       for(int i = 0;i<vectors.length;i++)
       {
           vectors[i] = new vector(11);
       }    
       int depth = 4;
       GraphletIsomorphism iso = new GraphletIsomorphism("/usr/local/hadoop/graphcount.txt");
 //A thread array optimizationJob is created, each thread calculates the graphlet vector from the purticular node     
       Runnable[] optimizationJob = new GraphVectors[g1.nNodes];
       Collection<Future> optPool = new LinkedList();
       ExecutorService optExecutor = Executors.newFixedThreadPool(g1.nNodes);
       for(int i = 0;i<optimizationJob.length;i++)
       {
          optimizationJob[i] = new GraphVectors(g1,i+1,depth,iso);
          optPool.add(optExecutor.submit(optimizationJob[i]));
       }    
       for(Future values: optPool)
       {
           try {
       
               values.get();
           } catch (Exception ex) {}
       }    
     
       
       for(int i = 0;i<optimizationJob.length;i++)
       {
         GraphVectors vecResult = (GraphVectors)optimizationJob[i];
         if(vecResult.resVector!=null)
         {    
            vectors[i] = vecResult.resVector;
         }
       }
       optExecutor.shutdown();
    
     return vectors;
  }
//Method that calculates the graphlet vector based on the hadoop implementation, this function returns the graphlet vector of the graph
  public static vector toVector(String graphInput,int numberofVectors)throws Exception
  {
   //the main file that contains hadoop driver programs and mapper and reducer classes.
    String fileToCompile =  "/usr/local/hadoop/project/src/graphlsh/OptimizedGraphEmbed.java";	
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();//compiler tool to compile the java program
    int compilationResult = compiler.run(null, null, null, fileToCompile);
    if(compilationResult == 0)
    {
            System.out.println("Compilation is successful");
    }
    else
    {
       System.out.println("Compilation Failed");
      
    }
   //creation of the jar for hadoop execution 
    File archfile = new File("/usr/local/hadoop/project/dist/OptimizedGraphEmbed.jar");
    File[] paths;
    File files = new File("/usr/local/hadoop/project/src/graphlsh/");
    //filtering the files at the location to add only .class files
     FilenameFilter filter = new FilenameFilter()
     {

	public boolean accept(File dir, String name) 
        {
               if(name.lastIndexOf('.')>0)
               {
                  // get last index for '.' char
                  int lastIndex = name.lastIndexOf('.');
                  
                  // get extension
                  String str = name.substring(lastIndex);
                  
                  // match path name extension
                  if(str.equals(".class"))
                  {
                     return true;
                  }
               }
               return false;
        }


    };

  paths = files.listFiles(filter);
  utilities.createJarArchive(archfile,paths);
   
  OptimizedGraphEmbed op = new OptimizedGraphEmbed();
  //generating the output folder  
  String dummy = utilities.outputFileName(graphInput);
  //call the createStartnode thing which will return the path name and substitute it as the input

  String input = new String("hdfs://master:54310/user/hduser/s10.txt");
  String output = new String("hdfs://master:54310/user/hduser/op"+dummy);
  
  vector resvec = op.runHadoop(graphInput,input,output,numberofVectors);//will return the vector of the graph
 
  return resvec;
 }
}
