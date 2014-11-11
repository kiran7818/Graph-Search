/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Permutation 
{
       
    
    int permutationSize;
    int nPermutations;
    int []node_numbers;
    int nextPermutationIndex;
    int permutationIndex;
    int []permutation; 
    public Permutation(int size)  //Constructor of this class which takes the input, size the number of nodes
    { 
        
        permutationSize = size;   //contains the number of nodes
        node_numbers = new int[permutationSize];  // Array to store the node numbers from 1 to permutationSize
        nPermutations = factorial(permutationSize); // it contains the number of permutation that can be generated using permutationSize,which is the number of nodes
        nextPermutationIndex = 0;
        permutation = new int[permutationSize];
        
        for(int i=0;i<permutationSize;i++) //numbering of the nodes is done here,node_numbers array will contain the numbers of the nodes from 1 to permutationSize
        {
           node_numbers[i]=i+1;
          
        }   
        permutationIndex = 0;
    }  
    
    /*This function Generate all the permutations of the given nodes
     * Input:-An array Called "node_numbers" which contains the nodes from 1 to permutationSize,the number of nodes and an index value,index value is used for swapping the element
     * Output:- A matrix called "permutationTable" of size total number  of permutations x number of nodes, which contain all the permutations
     */
    public void swap(int a[],int i,int j)
    {
       int temp = a[i];
       a[i] = a[j];
       a[j] = temp;
    }        
    
     public int [] generateNextPermutation(int nextPermutation[])
    {
        int index1,index2;
        int index3=0;
        int index4=0;
        int N = nextPermutation.length;
        
        
        for(index1 = N-2;index1>=0;--index1)
        {
            if(nextPermutation[index1]<nextPermutation[index1+1])
            {
                index3 = index1;
                break;
               
            } 
        }   
        for(index2=N-1;index2>=0;index2--)
        {
          if(nextPermutation[index3]<nextPermutation[index2])
          {
            index4 = index2;
            break;
           }    
           
        }    
        swap(nextPermutation,index3,index4);
       
        
        for (int r = N-1, s =  index3 +1; r > s; r--, s++)
        {    
            swap(nextPermutation, r, s);
        }  
        
            return nextPermutation;
        } 
     
   
  
    /*This function is used to calculate the factorial of numbers.
     * Input:-permutationSize
     * Output:-The factorail
     * This is used to calculate the number of rows of the permutationTable
     */
    public  int factorial(int permutationSize)
    {
        int fact=1;
        for(int i=1;i<=permutationSize;i++)
        {
            fact = fact*i;
        }    
        return fact;
    }        
    
}   
   


