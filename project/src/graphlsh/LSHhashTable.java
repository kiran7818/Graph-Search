/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kiran
 */
//package graphlsh;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class LSHhashTable {
     
    ArrayList<LSHElem> randObjs; //To store random objects
    int k; //Number of random oobjects;
    int m;//number of buckets in the hash table
    LinkedList <LSHElem>[]bucketTable;//array of linked list to store the data elements after hashing
    LSHElem elements;//the element to be inserted into the hash table
    LSHOps t;//wheather to use graph opsvector or graph opsgraph
    int heapOptions;//wheather to use max heap or min heap
    public LSHhashTable(int k, LSHElem e,LSHOps t,int options) //Constructor of the class
    {
      this.heapOptions = options;
      this.t = t;  
      this.elements = e;
      this.k = k;
      randObjs = new ArrayList();
      for(int i=0;i<k;i++)
      {
          randObjs.add(t.generateRandom(elements));//generate the random objects
      }  
      Iterator<LSHElem>itr = randObjs.iterator();//for each linked list in array of linked list
      m = 1 << k;
      bucketTable = new LinkedList[m];//Array of the linkedlsit used to store the vectors
      for(int i=0;i<m;i++)
      {
            bucketTable[i] = new <LSHElem>LinkedList();//Initialization of the linked list
      } 
       
    } 
   
    /*Method to find the index in the bucket*/
    int getBucketIndex(LSHElem v)
    {
        double[]sumArray = new double[k]; //sumArray will hold the position to look for the vector in the hash table
        Iterator<LSHElem> itr = randObjs.iterator();
        
        for(int i=0;i<k;i++)
        { 
            sumArray[i] = t.dotProduct(itr.next(), v);//according to the t we use the vector dotproduct method and graph dotproduct method
        } 
        return findIndex(sumArray);
        
    }
    /*Method to find the index position where the data element to be stored in the hash table*/
     int findIndex(double []sumArray)
     {
         
         int []position = new int [k];
         int sign = 0;
         for(int i=0;i<sumArray.length;i++)
         {
             if(sumArray[i]<0)
             {
                 sign = 1;
                 position[i] = sign;
                 
             } 
             else
             {    
                 sign = 0;
                 position[i] = sign;
             }    
         }  
         int index=0;
         
         
         for(int i = position.length-1;i>=0;i--)
         {
             index = 2*index + position[i];
            
             
         }  
         return index;
       
         
         
     }        
       
    
    /*This method is to put the vector at the specified location in the hash table obtained by taking the dot product of the input vector
      and the random vectors
      * Input:-vector to be inserted into the hash table
      */ 
    void put(LSHElem v)
    {
       bucketTable[getBucketIndex(v)].add(v);  
       
    }   
   /*Method to print the hash table*/
    void printHashTable(int j)
    {
        System.out.println("Hash Table"+j);
        for(int i = 0;i<m;i++)
        {
          Iterator<LSHElem>itr = bucketTable[i].iterator();//for each linked list in array of linked list
            if(itr.hasNext())//for each element in the hash table
            {
                while(itr.hasNext())
                {    
                    Graphs tmp = (Graphs)itr.next();
		    System.out.print(tmp.id);
                    System.out.println(" ");
                }   //*/ 
            }    
            else//if no element just print blank buckets
            {
                 System.out.println(i+" ");
            
            }    
             
            
        }    
    }        
    /*Method to retrive the nearest elements to the given elemenets*/        
    
           
    void getNearestElements(LSHElem v, int r, PriorityQueue<LSHElem> queue)
    {
    //  System.out.println("to find the bucket position");  
       int searchIndex = getBucketIndex(v);//find the location of the element in the hashtable
      //System.out.println("search index"+searchIndex);
      Iterator<LSHElem>iter = bucketTable[searchIndex].iterator();
      System.out.println("search index"+searchIndex);
      double dotProduct = 0.0; //for each vector stored in the search position,calculate the dot product and insert into the priority queue
      while(iter.hasNext())
      {
        Graphs bucketTableElement = (Graphs)iter.next();
      /*  for(int i =0;i<bucketTableElement.graphvec.size;i++)
        {
            System.out.print(bucketTableElement.graphvec.elements[i]+" ");
        }    
        System.out.println(" ");
        System.out.println("came here for searching");
        //*/ 
        dotProduct = t.dotProduct(bucketTableElement.graphvec, v);//find the dot product of the vectors
        System.out.println("the dot product for searching is "+dotProduct);
        if(queue.getSize() < r)//if the heap is not full,insert into the priority queue
        {
          queue.insert(bucketTableElement, dotProduct);
        }
        else                              //if heap is full extract the top element and insert the element
        {
          if(heapOptions == 1)//heapoption  1 means max heap is used to implement the priority queue
          {
  //implementing max heap for getting minimum elements so if the next input that comes is minimum than the root that means that input belongs 
  //to the minimum set so remove the root as it is the max and then insert the new one and now the heap will contain the minimum elements            
           //System.out.println("the max priority element at the top is"+queue.getMaxPriority());   
            if(queue.getMaxPriority() > dotProduct)//only those elements having dotproduct less than the root should be inserted into maxheap
            {
                
                queue.extractTop();//extract the top most element and create space for insertions
                queue.insert(bucketTableElement, dotProduct);//insert the element at the correct location
            }    
                
          }    
          if(heapOptions == 2)//heapOptiom 2 means min heap is used to implement the priority queue
          {
   //implementing min heap for getting max elements so if the input next comes is max than the root it should be inserted so remove the root
              
   //if it is the reverse case then heap will contain smaller values and those will not be the closest elements           
            if(queue.getMaxPriority() < dotProduct)//only those elements that have dotproduct greater than the root should be inserted into min heap
            {
               queue.extractTop();//extract the top most element and create space for insertions
               queue.insert(bucketTableElement, dotProduct);//insert the element at the correct location
            }    
          }    
        }
           
      } 
    }
 
}
      


