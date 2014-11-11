
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
public class LSH  {
    
  int k;//number of the random objects
  int n;//number of hash tables
  int r;//number of nearest elements
  LSHhashTable tables[];//array of hashtables.
  LSHElem element;//object to be inserted into the hash table
  int heapOptions;//to indicate the maxheap or minheap is used to implement the priority queue
 
 public LSH(int k,int n,int r,LSHElem elements,LSHOps options,int heapOptions)
 {
    this.heapOptions = heapOptions; 
    this.k = k;
    this.n = n;
    this.r = r;
    this.tables = new LSHhashTable[n];
    this.element = elements;
    
    for(int i=0;i<n;i++)
    {
            
        this.tables[i] = new LSHhashTable(k, this.element,options,heapOptions);
    }
 } 
 
  void put(LSHElem v) //this function puts the object in the hash table
  {
     
    for(int i=0;i<n;i++)
    {
        tables[i].put(v);//this put method is the function in lsh hash table
    }    
  }
 
 LSHElem []getRNearestNeighbours(LSHElem element) //this function return the nearest r vectors
 {
    PriorityQueue <LSHElem>pqueue[] = new PriorityQueue[n];
    for(int i = 0;i<n;i++)
    {    
        pqueue[i] = new PriorityQueue <LSHElem>(r,heapOptions);
    }
    for(int i=0;i<n;i++)
    {   
       tables[i].getNearestElements(element,r, pqueue[i]);
    }
     int size = n*r;
   
     PriorityRecord[] data = utilities.removeDuplicates(pqueue,size);
     
     LSHElem elements[] = new LSHElem[data.length];
     LSHElem arrayOfElements[] = new LSHElem[data.length];
  
     for(int i = 0;i<elements.length;i++)
     {
        elements[i] = (LSHElem)data[i].elem;
     }    
  
     arrayOfElements = utilities.reverse(elements);
  
     return arrayOfElements;
 }
 //create the vectors of the graphs in the file and save the result in a file in /usr/local/hadoop/project/output/graphvector.txt
 public void generateVector(String foldername,char mode)
 {
	File folder = new File(foldername);//folder in which input files are kept
        int nFiles = folder.listFiles().length;//number of files in the folder
	String []filename = new String[nFiles];
    	File []listOfFiles = new File[nFiles];//file array to hold the files in the config directory
    	listOfFiles = folder.listFiles();
	Graphs inputGraph[] = new Graphs[nFiles];
    	//to read the file name stored in the folder
    	for (int i=0;i<nFiles;i++) 
    	{
       	  if (listOfFiles[i].isFile()) 
          {
             filename[i] = listOfFiles[i].getName();
          }
        }  
	for(int i = 0;i<inputGraph.length;i++)
        {
	      inputGraph[i] = new Graphs(foldername+filename[i],mode);
              inputGraph[i].getVector(foldername+filename[i],mode);
        }    
	
 }
 //This function populates the LSH hashtable with the precomputed vectors of the graph
 public void populateHashtables(String graphFoldername, String vectorFoldername,char mode)
 {
	File graphFolder = new File(graphFoldername);//folder in which input files are kept
	int nFiles = graphFolder.listFiles().length;//number of files in the folder
	String []graphFilename = new String[nFiles];
    	File []graphListOfFiles = new File[nFiles];//file array to hold the files in the config directory
    	graphListOfFiles = graphFolder.listFiles();
	for (int i=0;i<nFiles;i++) 
    	{
       	  if (graphListOfFiles[i].isFile()) 
          {
             graphFilename[i] = graphListOfFiles[i].getName();
          }
        }  
	
	File vectorFolder = new File(vectorFoldername);//folder in which input files are kept
	String []vectorFilename = new String[nFiles];
    	File []vectorListOfFiles = new File[nFiles];//file array to hold the files in the config directory
    	vectorListOfFiles = vectorFolder.listFiles();
  	Graphs inputGraph[] = new Graphs[nFiles];
    	//to read the file name stored in the folder
	for(int j=0;j<nFiles;j++)//read the graphlets from the file folder
    	{   
           inputGraph[j] = new Graphs(graphFoldername+graphFilename[j],mode); 
	}
	for (int i=0;i<nFiles;i++) 
    	{
       	  if (vectorListOfFiles[i].isFile()) 
          {
             vectorFilename[i] = vectorListOfFiles[i].getName();
          }
        } 
	for(int i = 0;i<nFiles;i++)
	{
	    inputGraph[i].graphvec = new vector(11);	
	}
	
	String pattern = "graphvector is=";
	double[] elts = new double[11];
  	for(int i = 0;i<nFiles;i++)
  	{	 
	   
    	   try {
		    String newFilename = utilities.changeFilename(graphFilename[i]);
	            File file = new File(vectorFoldername+newFilename);
		    Scanner in = new Scanner(file);
		    String graphvector = in.nextLine();
		    String[] lineSplit;
	            lineSplit = graphvector.split(pattern);
		    String[] elementSplit;
	            elementSplit = lineSplit[1].split(" ");
		    
	            for(int k = 0;k<elementSplit.length;k++)
	            {  
		     inputGraph[i].graphvec.elements[k] = Double.parseDouble(elementSplit[k]);
	            } 
	            
	       }catch (Exception ex) {}
	       
         }

  	for(int j = 0;j<nFiles;j++)
  	{
        	 this.put(inputGraph[j]);
  	}
   
 }
 
 public static void main (String[] args)throws Exception
 {
     long startTime = System.currentTimeMillis();
     LSHOps option = new GopsVector();//the object of the LSHOps class....
     Scanner in = new Scanner(System.in);
     System.out.println("enter the mode of implementation");//Different Modes are a=automatic, h=hadoop
     char mode = in.next().charAt(0);
      
     Graphs g1 = new Graphs("/usr/local/hadoop/input/testGraph1.txt",mode);
     g1.getVector("/usr/local/hadoop/input/testGraph1.txt",mode);
     g1.graphvec.printElement();
     
     //*/
     //heapoption 1 = max heap is used to implement the priority queue\\
    //heapoption 2 = min heap is used to implement the priority queue
  

    LSH glsh1 = new LSH(2, 2, 7,g1,option,2);
    String graphFoldername = "/usr/local/hadoop/input/test/";
    String vectorFoldername = "/usr/local/hadoop/project/output/vectors/";
    //generateVector() method creates the vectors of the graphs and save the result in a file 
    // populateHashtables() method can read the vector stored in the file and can populate the hashtable
    glsh1.populateHashtables(graphFoldername,vectorFoldername,mode);
    
   
    Graphs test = new Graphs("/usr/local/hadoop/project/input/AIDSGRAPH/test71.txt",mode);
    
    test.getVector("/usr/local/hadoop/project/input/AIDSGRAPH/test71.txt",mode);
    
    LSHElem []nearestElements = glsh1.getRNearestNeighbours(test);//store the nearest elements into the nearest elements array
    System.out.println("the nearest elements are");
    for(int i = 0;i<nearestElements.length;i++)
    {
       System.out.println("-------");
       Graphs temp = (Graphs)nearestElements[i];
       System.out.println("filename:"+temp.id+"vector is:");
       temp.graphvec.printElement();	
       System.out.println("-------");
    }
 	                           
   
    long endTime = System.currentTimeMillis();
    long totalTime = endTime-startTime;
    System.out.println("total time is"+totalTime);
       
  }
  
}
