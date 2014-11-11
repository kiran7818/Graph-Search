/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;
import java.io.InputStream;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.*;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
/**
 *
 * @author kiran
 */
public class utilities {
    
  public static int BUFFER_SIZE = 10240;
  public static boolean findFileSystem(String filename)
  {
      String hdfs = "hdfs";
      if(filename.startsWith(hdfs))
      {
          return true;
      }    
      else
      {    
          return false;
      }    
      
 }
  
 protected static void createJarArchive(File archiveFile, File[] tobeJared) 
 {
    try 
    {
      byte buffer[] = new byte[BUFFER_SIZE];
      // Open archive file
      FileOutputStream stream = new FileOutputStream(archiveFile);
      JarOutputStream out = new JarOutputStream(stream, new Manifest());

      for (int i = 0; i < tobeJared.length; i++)
      {
        if (tobeJared[i] == null || !tobeJared[i].exists() || tobeJared[i].isDirectory())
          continue; // Just in case...
    
        // Add archive entry
        JarEntry jarAdd = new JarEntry(tobeJared[i].getName());
        jarAdd.setTime(tobeJared[i].lastModified());
        out.putNextEntry(jarAdd);

        // Write file to archive
        FileInputStream in = new FileInputStream(tobeJared[i]);
        while (true) 
        {
          int nRead = in.read(buffer, 0, buffer.length);
          if (nRead <= 0)
            break;
          out.write(buffer, 0, nRead);
        }
        in.close();
      }

      out.close();
      stream.close();
     
    } catch (Exception ex){}
  }
  public static Scanner defineScanner(String filename, char mode)
  {
    Scanner in = null;
    /*If mode = h scanner is assigned to read the adj.matrix of the graph from hdfs*/
    if(mode == 'h')
    {
      try{
	   
      	    Path filepath = new Path(filename);//path of the file where it is stored   
      	    FileSystem fs = FileSystem.get(new Configuration());//filesystem object to access it
      	    in = new Scanner(new InputStreamReader(fs.open(filepath)));//scanner is defined read from hdfs
	 }catch(Exception e){}
	
      return in;
   }
   else 
   {
     try{     	
	  File file = new File(filename);
	  in = new Scanner(file);
	}catch(Exception e){}
	
     return in;
    }  
   
  }
//return the path name after copying the file from local file system to the hdfs
  public static String copyFile(String filename)
  {
	String hdfsfilename = "";
    try{
	Configuration conf = new Configuration();	
	conf.set("mapred.job.tracker", "master:54311"); 
        conf.set("fs.default.name", "hdfs://master:54310");
     	Path src = new Path(filename);//from where we need to copy the file;
     	Path dest = new Path("hdfs://192.168.0.101:54310/user/hduser/graphInputsnew/");//dest to where the file is copied
     	FileSystem fs = FileSystem.get(conf);
     	fs.copyFromLocalFile(src,dest);
	int start = filename.lastIndexOf("/");//need to take only the filename, so it scans the entire path and takes only the needed name
 	int end = filename.lastIndexOf("");
	String newNam= filename.substring(start+1,end);
	hdfsfilename = new String("hdfs://192.168.0.101:54310/user/hduser/graphInputsnew/"+newNam);//append the filename with the hdfs path
	
	}catch(Exception e){}
    return hdfsfilename;
  }
  public static void sort(DotProductRecords[] recs)
  {
   for(int i = 1;i<recs.length;i++)
   {
	DotProductRecords key = recs[i];
        int j = i-1;	
 	while(j>=0&&recs[j].dotProduct<key.dotProduct)
	{
	   DotProductRecords temp = recs[j];
	   recs[j] = recs[j+1];
	   recs[j+1] = temp;
	   j--;
	}
	recs[j+1] = key;

   } 
 }
 public static void findDotProduct(vector graphvector,char mode)
 {
    Properties graphConfig  = new Properties();
     InputStream inf = null;
     try{
  	File file = new File("/usr/local/hadoop/project/output/AIDS1graphVectors.properties");
	inf = new FileInputStream(file);
	graphConfig.load(inf);
	}catch(Exception e){}
     
     File folder = new File("/usr/local/hadoop/project/input/AIDSGRAPH/");
     String []filename = new String[10746];
     File []listOfFiles = new File[10746];//file array to hold the files in the config directory
     listOfFiles = folder.listFiles();
    //to read the file name stored in the folder
    for (int i=0;i<listOfFiles.length;i++) 
    {
       if (listOfFiles[i].isFile()) 
       {
          filename[i] = listOfFiles[i].getName();
       }
        
    }  
    Graphs inputGraph[] = new Graphs[10746];  
  
    for(int j=0;j<10746;j++)//read the graphlets from the file folder
    {   
        inputGraph[j] = new Graphs("/usr/local/hadoop/project/input/AIDSGRAPH/"+filename[j],mode);  
    }
    String[] vectorString = new String[10746];
    //to convert the string content stored in the file back to the vector repersenting the corresponding graph 
    for(int i = 0;i<vectorString.length;i++)
    {
        String[] lineSplit;
        inputGraph[i].graphvec = new vector(11);
        vectorString[i] = graphConfig.getProperty(filename[i]);
        lineSplit = vectorString[i].split(" ");
        for(int j = 0;j<11;j++)
        {    
            inputGraph[i].graphvec.elements[j] = Double.parseDouble(lineSplit[j]);
    	}       
    }
    DotProductRecords recs[] = new DotProductRecords[10746];
    double dotProduct[] = new double[10746];
    for(int i = 0;i<10746;i++)
    {
	dotProduct[i] = graphvector.dotProduct(inputGraph[i].graphvec);
	recs[i] = new DotProductRecords(inputGraph[i].id,dotProduct[i]);
	
    }
    
    utilities.sort(recs);
    for(int i = 0;i<10746;i++)
    {
	System.out.println(recs[i].dotProduct+" "+recs[i].id);
    }	

 }
 public static void HeapSort(PriorityRecord[] elems)
 {
     BuildMax(elems);
     int len = elems.length-1;
     for(int i = elems.length-1;i>=1;--i)
     {
          PriorityRecord temp = elems[i];
          elems[i] = elems[0];
          elems[0] = temp;
          --len;
          maxHeapify(elems,0,len);
          
     }    
      
 }
 public static void maxHeapify(PriorityRecord[] elems,int index,int n)
 { 
      int leftChild = 2*index+1;
      int rightChild = 2*index+2;
      int largest = -1;
      if(leftChild<=n&&elems[leftChild].priority>=elems[index].priority)
      {
          largest = leftChild;
      }
      else
      {
          largest = index;
      }    
      if(rightChild<=n&&elems[rightChild].priority>=elems[largest].priority)
      {
          largest = rightChild;
      }    
      if(largest!=index)
      {
          PriorityRecord temp = elems[largest];
          elems[largest] = elems[index];
          elems[index] = temp;
          maxHeapify(elems,largest,n);
      }    
 }
 public static void BuildMax(PriorityRecord[] elems)
 {
   int len = elems.length-1;
   for(int i = (int)len/2;i>=0;--i)
   {
       maxHeapify(elems,i,len);
   }   
      
 }
 public static  PriorityRecord[] removeDuplicates(PriorityQueue[] elementsArray,int size)
 {
	
      ArrayList<PriorityRecord> elements  = new ArrayList<PriorityRecord>();
    
      for(int i = 0;i<elementsArray.length;i++)
      {
         for(int j = 0;j<elementsArray[i].recs.length;j++)
           {    
	    if(elementsArray[i].recs[j]!=null)//each hashtables heap will have a r elts, it is possible that there can be null elts  
	    {
		elements.add(elementsArray[i].recs[j]);
	    }
           } 
           
      }
      PriorityRecord[] elems = new PriorityRecord[elements.size()];
      elems = elements.toArray(elems);
      
      utilities.HeapSort(elems);
     
      
      Hashtable<Integer,PriorityRecord> table = new Hashtable(); 
      for(int i = 0;i<elems.length;i++)
      {
          
          Double key =  elems[i].priority;
          table.put(key.hashCode(),elems[i]);
           
      }    
      Enumeration names = table.keys();
      ArrayList<PriorityRecord> recs = new ArrayList();
      while(names.hasMoreElements())
      {
           Integer temp = (Integer)names.nextElement();
           recs.add(table.get(temp));
      }  
      PriorityRecord[] records = new PriorityRecord[recs.size()];
      records = recs.toArray(records);
      
      utilities.HeapSort(records);
     
      return records;   
 }
 public static LSHElem[] reverse(LSHElem[] elements)
 {
      LSHElem[] revarr = new LSHElem[elements.length];
      for(int i = elements.length-1,j=0;i>=0&&j<elements.length;--i,j++)
      {
          revarr[j] = elements[i];
      }    
      return revarr;
      
 }        
 //method to find the name of the vector file
  public static String outputFileName(String filename)
  {
      int startIndex = filename.lastIndexOf("/"); // index of "/"
      int endIndex = filename.lastIndexOf("."); //index of "."
      String str = filename.substring(startIndex+1, endIndex);
      return str;
      
  }
//method to create the start nodes for Bfs in hdfs
//file name is the name of the input graph file
  public static void createStartNodes(String filename, int numNodes)
  {
      int startIndex = filename.lastIndexOf("/"); // index of "/"
      int endIndex = filename.lastIndexOf("."); //index of "."
      String str = filename.substring(startIndex+1, endIndex);
      String hdfsPath = new String("hdfs://master:54310/user/hduser/");  
      String start = "start";
      StringBuilder sb = new StringBuilder();
      sb.append(hdfsPath);
      sb.append(start);
      sb.append(str);   
      sb.append(".txt");
      String bfsStartnodes = new String(sb);
      System.out.println(bfsStartnodes);
      try{
            Path pt=new Path(bfsStartnodes);
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.create(pt,true)));
            for(int i = 1;i<=numNodes;i++)
	    {                      
                 br.write(i+"");
		 br.newLine();
	    }
            br.close();
         }catch(Exception e){}
	
  }
//change the .txt to .vec
  public static String changeFilename(String filename)
  {
	int startIndex = 0;	
	int endIndex = filename.lastIndexOf(".");
	String temp = filename.substring(startIndex, endIndex);
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(temp);
        strBuf.append(".vec");
        String str = new String(strBuf);

	return str;
  }

}
