import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import org.apache.hadoop.mapred.lib.MultithreadedMapRunner;

public class OptimizedGraphEmbed{

 public static String filename;
 public static class Optimizedmap extends MapReduceBase implements Mapper<LongWritable,Text,Text,IntWritable>{
 
 // A map function which generate the bfs of the given graph and sends out the count for each of the graplet
   public void map(LongWritable key,Text value,OutputCollector<Text,IntWritable>output, Reporter reporter)throws IOException
   {
		
		String line = value.toString(); //stores the value provided in the input text file 
		int startnode = Integer.parseInt(line);//contains the root from which bfs traversal should be started
    		char mode = 'h';
		Graphs input = new Graphs(filename,mode);
		input.printElement();
 GraphletIsomorphism iso = new GraphletIsomorphism("hdfs://192.168.0.101:54310/user/hduser/graphcount.txt",input.nNodes);//
		
				
		int depth = 4; //depth upto which we need to traverse in Bfs
		Graphs BfsGraph = GraphVectors.BFS(input,startnode,depth);
		int sizeOfGraphlets = 4;		
		int[] firstCombination = new int[sizeOfGraphlets];
                int[] nextCombination = new int[sizeOfGraphlets];
                for(int i = 0;i<firstCombination.length;i++)
                {
                  firstCombination[i] = i+1;
                }
                Combination com = new Combination();
		double size = com.numberOfCombinations(BfsGraph.nNodes,sizeOfGraphlets);
		StringBuilder strnode = new StringBuilder();
//the output key will be like N1K1:-N1 repersents the node and K1 repersent the index in the 11 graphlets
		strnode.append("N");
		strnode.append(line);
		strnode.append("K");
		nextCombination = firstCombination;
                for(int j = 0;j<size;j++)
                {
                   nextCombination = com.nextCombination(nextCombination,BfsGraph.nNodes,sizeOfGraphlets,j);
                   int match2 = iso.compareGraphs(nextCombination,BfsGraph,iso);
                   if(match2!=-1)
                   {
		     StringBuilder str = new StringBuilder();
	             str.append(strnode);
		     str.append(match2);
		     String opstr = new String(str);
                     output.collect(new Text(opstr),new IntWritable(1));
                   }
                }
		
       		/*
	  if(BfsGraph.nNodes>=sizeOfGraphlets)
	  {
		int numberOfThreads = BfsGraph.nNodes-(sizeOfGraphlets-1); 
		Runnable[] combinationThreads = new FindMatch[numberOfThreads];
		Collection<Future>combPool = new LinkedList();
		ExecutorService comExecutor = Executors.newFixedThreadPool(numberOfThreads);
		long start = System.currentTimeMillis();			
		for(int i = 0;i<combinationThreads.length;i++)
		{
	   		combinationThreads[i] = new FindMatch(BfsGraph,i+1,iso,inputGraph.nNodes);
	   		combPool.add(comExecutor.submit(combinationThreads[i]));
		}
		for(Future future:combPool)
		{
	   	   try		 
	   	   {		 	
	     		future.get();
	   	   }catch(Exception e){}
	        }
		comExecutor.shutdown();
		
//each thread returns an arraylist that contains the matching graphlets, <K,V> to the reducer is the matching graphlet index and 1(which indicates the increment)
		start = System.currentTimeMillis();
		for(int i = 0;i<numberOfThreads;i++)
		{
	  		FindMatch match = (FindMatch)combinationThreads[i];
	  		ArrayList matchArray = match.res;
			Iterator<Integer>itr = matchArray.iterator();
	  		while(itr.hasNext())
		  	{
			  int temp = itr.next();
				  //System.out.println("the element is"+temp);
		    	  output.collect(new IntWritable(temp),new IntWritable(1));
		      	}
		}
		
	}*/
   }
   public void configure(JobConf job)
   {
	filename = job.get("filename");//input graph
	
   }
 }
 
  public static class Optimizedreduce extends MapReduceBase implements Reducer<Text,IntWritable,Text,IntWritable>
  {
    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text,IntWritable>output, Reporter reporter)throws 
IOException
		{
			int sum = 0;
		     while(values.hasNext())
		     {
			sum = sum+values.next().get();
		     }
		     output.collect(key,new IntWritable(sum));
				
		}

  }
//Function that reads the file part-00000 stored in the hdfs and convert into vectors
  public vector toVector(String filename, Configuration conf,int numberofVectors,String graphfilename)
  {
	vector resVector = new vector(11);
	vector[] nodeVectors = new vector[numberofVectors];
	for(int i = 0;i<nodeVectors.length;i++)
	{
	   nodeVectors[i] = new vector(11);
	}
	for(int i = 0;i<nodeVectors.length;i++)
	{
	  for(int j = 0;j<nodeVectors[i].size;j++)
	  {
	      nodeVectors[i].elements[j] = 0;
	  }
	}
	
    try{
	   Path auxPath = new Path(filename+"/part-00000");
	   FileSystem fs = FileSystem.get(conf);
	   Scanner in = new Scanner(new InputStreamReader(fs.open(auxPath))) ;
	   while(in.hasNextLine())
           {  	
	        String line = in.nextLine();
	  	String[] lineValue = line.split("\t");//make the modification.....
		int index1 = lineValue[0].lastIndexOf("N");
    		int index2 = lineValue[0].lastIndexOf("K");
    		String nodeStr = lineValue[0].substring(index1+1, index2);
    		int nodeNumber = Integer.parseInt(nodeStr);
    		String graphIndexStr = lineValue[0].substring(index2+1, lineValue[0].length());
    		int graphIndexNumber = Integer.parseInt(graphIndexStr);
    		nodeVectors[nodeNumber-1].elements[graphIndexNumber] = Integer.parseInt(lineValue[1]); 	
			
	   }
     }catch(Exception e){}
     for(int i = 0;i<nodeVectors.length;i++)
     {
  	resVector = resVector.addVectors(nodeVectors[i]);
     } 	
     resVector.normalize();
     String vecFilename = utilities.outputFileName(graphfilename);
     BufferedWriter writer = null;
    try{
        File file = new File("/usr/local/hadoop/project/output/vectors/"+vecFilename+".vec");
        file.createNewFile();
        writer = new BufferedWriter(new FileWriter(file));
	writer.write("graphvector is=");
        for(int j = 0;j<resVector.size;j++)
        {
                writer.write(resVector.elements[j]+" ");
        }   
	writer.newLine();        
	for(int i = 0;i<nodeVectors.length;i++)
        {
            writer.write("vector for node"+i+"=");
            nodeVectors[i].normalize();
            for(int j = 0;j<nodeVectors[i].size;j++)
            {
                writer.write(nodeVectors[i].elements[j]+" ");
            }    
            writer.newLine();
        }   
        writer.close();
        
    }catch(Exception e){}


     return resVector;


  }
  public vector runHadoop(String filename,String input,String output,int numberofVectors)throws Exception
  {
     //String input = new String("hdfs://192.168.0.101:54310/user/hduser/searchnodes50.txt"); 
     //String output = new String("hdfs://192.168.0.101:54310/user/hduser/"+"op50");	
     
     String jarPath = new String("/usr/local/hadoop/project/dist/OptimizedGraphEmbed.jar");
     
     Configuration conf = new Configuration();
     conf.set("mapred.job.tracker", "master:54311"); 
     conf.set("fs.default.name", "hdfs://master:54310");
     
     JobConf job1 = new JobConf(conf);
     job1.set("filename",filename);
     job1.setJar(jarPath);
     job1.setJobName("GraphEmbed");
     //job1.setJarByClass(OptimizedGraphEmbed.class);
     job1.setNumMapTasks(30);
	
     job1.setMapperClass(Optimizedmap.class);
     job1.setCombinerClass(Optimizedreduce.class);
     job1.setReducerClass(Optimizedreduce.class);
     //job1.setMapRunnerClass(MultithreadedMapRunner.class);		  
     
     job1.setOutputKeyClass(Text.class);
     job1.setOutputValueClass(IntWritable.class);
     
     FileInputFormat.setInputPaths(job1, new Path(input));
     FileOutputFormat.setOutputPath(job1,new Path(output));
     JobClient.runJob(job1);
     vector resVec = new vector(11);
     resVec = this.toVector(output,conf,numberofVectors,filename);
     return resVec;

  }

}
