/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.util.ArrayList;
import java.util.Iterator;

class PriorityRecord <LSHElem> {
    
        LSHElem elem; //elem is the data to be inserted into the hash table
        double priority;//It is the priority with which the data need to be inserted
        
        public PriorityRecord(double priority, LSHElem e)//constructor of the class
        {
            
            this.elem = e;
            this.priority = priority;
        }        
        double getPriority()//return the priority
        {
            return this.priority;
        }
        LSHElem getElem()//return the element
        {
            
            return this.elem;
        }
        void setElem(LSHElem element)
        {
            this.elem = element;
        }
   }

public class PriorityQueue <LSHElem> {
    
    PriorityRecord []recs;//recs is an array of type priority record ,to hold the data
    int heapSize;//size of the heap data structure
    int maxsize;
    int heapOptions;
    public PriorityQueue(int maxsize,int options)//constructor of the class
    {
       this.heapOptions = options;
       this.maxsize = maxsize;
       recs = new PriorityRecord[maxsize];
       heapSize = 0;
          
    } 
    
    int getSize()
    {
     return heapSize;   
    }
    
    int getParentIndex(int index)
    {
      if(index == 0)//if the element is the parent
      {
          return index;
      }    
      else
      {    
        if(index%2 == 0)
        {
            return((int)(index-1)/2);//if the index is even //(index-1)/2 or index-1>>1
        }    
        else
            return((int)(index)/2);//if index is odd(if index is odd then we can take the floor of index/2) index >>1
      } 
    }        
    /*Method  to insert the element into the priority queue with the priority using a heap data structre*/
    void insert(LSHElem elem, double priority)
    {
        if (heapSize == recs.length)//if heap is full t
        {
            //Heap full
            return;
        }
        
        PriorityRecord r = new PriorityRecord(priority, elem);
        heapSize++;//create the space for inserting the data
        int currentIndex = heapSize-1;
        recs[currentIndex] = new PriorityRecord(priority,elem);//insert the element at the current index
        int parentIndex = getParentIndex(currentIndex);//get the parent index of the current index
        PriorityRecord temp;
        if(heapOptions == 1)//maxheap is used to implement the priority queue
        {    
            while(recs[currentIndex].getPriority()>recs[parentIndex].getPriority()) // if the priority of the element inserted is higher than the parent element
            {
               temp = recs[parentIndex];
               recs[parentIndex] = recs[currentIndex];
               recs[currentIndex] = temp;
               currentIndex = parentIndex;
               parentIndex = getParentIndex(currentIndex);
            }
           
       /* System.out.println("recs------insert");
        for(int i=0;i<heapSize;i++)
        {
          Graph x = (Graph)recs[i].getElem();
          for(int j = 0;j<x.nNodes;j++)
          {    
            for(int k = 0;k<x.nNodes;k++)
            {
                System.out.print(x.graph[j][k]+" ");
            }    
            System.out.println(" ");
          }
           
          System.out.print(recs[i].getPriority());
       } */
            
      }
      else
      {
        while(recs[currentIndex].getPriority()<recs[parentIndex].getPriority()) // if the priority of the element inserted is hlesser  than the parent element
        {
            temp = recs[parentIndex];
            recs[parentIndex] = recs[currentIndex];
            recs[currentIndex] = temp;
            currentIndex = parentIndex;
            parentIndex = getParentIndex(currentIndex);
        }
            
      }    
   /*    System.out.println("recs------insert");
        for(int i=0;i<heapSize;i++)
        {
          Graph x = (Graph)recs[i].getElem();
          for(int j = 0;j<x.nNodes;j++)
          {    
            for(int k = 0;k<x.nNodes;k++)
            {
                System.out.print(x.graph[j][k]+" ");
            }    
            System.out.println(" ");
          }
          System.out.print(recs[i].getPriority());
       } 
       //*/ 
     }   
       
   /*Method to return the maximum element which is the root element*/ 
    LSHElem getMax()
    {
        return (LSHElem)recs[0].getElem();//return the maximum element
    }       
    
    double getMaxPriority()
    {
        return recs[0].getPriority();//return the priority
    }
    int getLeftChild(int index)
    {
        return 2*index+1;
    }        
    int getRightChild(int index)
    {
        return 2*index+2;
    }        
    
    /*Method to remove the max element from the root and then arrange the remaining data to saisfy the heap property*/
    LSHElem extractTop()//extract the max
    { 
        
        LSHElem max = getMax();//initializing the max variable
        int  index = 0;
        int largestRecordIndex = 0,smallestRecordIndex = 0;
        int leftIndex =  getLeftChild(index);//index of the left child
        int rightIndex = getRightChild(index);//index of the right child
        if(heapSize==0)//heap is empty
        {
            return null;
        }  
         max = (LSHElem)recs[0].getElem();//assign the root to the max
         recs[0] = recs[heapSize-1]; //copy the last element to the root
        
        if(heapSize==1)//if there is only one element in the heap,only one root
        {
             heapSize--; //decrement the heapsize 
            return max;
             
        }    
        else if(heapSize == 2)//if there is only two elements,one root and and one child
        {
            heapSize--; //decrement the heapsize 
           return max;
        }        
        else 
        {
         PriorityRecord temp;   
         heapSize--; //decrement the heapsize 
         
         if(heapOptions == 1)//maxheap is used to implement the priority queue
         {   
             /*if the parent is less than the left child and right child move the parent till it satisfies the max heap property*/
             while(recs[index].getPriority()<recs[leftIndex].getPriority()||recs[index].getPriority()<recs[rightIndex].getPriority())
             {
              //find the child with the largest priority
              if(recs[leftIndex].getPriority()>recs[rightIndex].getPriority())
              {
                 largestRecordIndex = leftIndex;
              }    
              else
                 largestRecordIndex = rightIndex;
              
              if(largestRecordIndex!=index)//swap the child with the largest priority and the parent
              {    
                 temp = recs[largestRecordIndex];
                 recs[largestRecordIndex] = recs[index];
                 recs[index] = temp;
              }
              index = largestRecordIndex;
              leftIndex = getLeftChild(index);
              rightIndex = getRightChild(index);
              if(leftIndex>heapSize||rightIndex>heapSize)//if the leaf is reached,then no more checking
              {    
                 break;
              }
             }   
         }
         if(heapOptions == 2)
         {
           /*if the parent is greater than the left child and right child move the parent till it satisfies the max heap property*/  
           while(recs[index].getPriority()>recs[leftIndex].getPriority()||recs[index].getPriority()>recs[rightIndex].getPriority())
           {
              //find the child with the smallest priority
             if(recs[leftIndex].getPriority()<recs[rightIndex].getPriority())
             {
                smallestRecordIndex = leftIndex;
             }    
             else
               smallestRecordIndex = rightIndex;
             
             if(smallestRecordIndex!=index)//swap the child with the smallest priority and the parent
             {    
                 temp = recs[smallestRecordIndex];
                 recs[smallestRecordIndex] = recs[index];
                 recs[index] = temp;  
             }
             index = smallestRecordIndex;
             leftIndex = getLeftChild(index);
             rightIndex = getRightChild(index);
             if(leftIndex>heapSize||rightIndex>heapSize)//if the leaf is reached,then no more checking
             {    
               break;
             }

           }
          }     
        return max;    
        } 
        
    }
    
   /* method to convert the priority records into an arraylist*/
    ArrayList<LSHElem> toArrayList()
    {
        ArrayList<LSHElem> arr = new ArrayList<LSHElem>();
        ArrayList<LSHElem> revarr = new ArrayList<LSHElem>();
        int size = 0;
        if(recs.length>heapSize)//if only small number of elements are there
            
             size = heapSize;
        else
            size = recs.length;
        for(int i=0; i< size;i++)
        {
            arr.add(this.extractTop());//extract the max element from the root and arrange according to max heap property
        }
        for(int i=size-1; i >=0;--i)
        {
            revarr.add(arr.get(i));//reverse the array to obtain the minimum elements at the first posititons
        }
      return revarr;
    }
}
