/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

/**
 *
 * @author kiran
 */
/*This class is to calculate the Combination*/
public class Combination {
    
  /*This function is to find the next combination nCr*/
  public static int[] nextCombination(int []com,int n, int r,int counter) 
  {
      int i = r;
     if(counter == 0)
     {
	return com;
     }
     else
     {        
     
      while (com[i - 1] == ((n - r) + i)) 
        {
            i = i - 1;
                    
        }
 
        com[i - 1] = com[i - 1] + 1;
               
        for (int j = i + 1; j <= r; j++) 
        {
                 
            com[j - 1] = com[i - 1] + j - i;
                   
        }
        return com; 
     }  
  }
 
  /*This function calculates the factorial*/
  static double factorial(double n) 
  {
     double fact = 1;
     for(int i = 1;i<=n;i++)
     {
        fact = fact*i;
     }    
     return fact; 
  }
  /*this function calculate the number of  combination*/
 static double numberOfCombinations(double n, double  r) 
  {
        double sumN = 1;
        for(int i = 0;i<r;i++)
        {
            sumN = sumN*(n-i);
        }    
        double sumR = 1;
        for(int i = 1;i<=r;i++)
        {
            sumR = sumR*i;
        }    
        return (sumN/sumR);
    // return (factorial(n) / (factorial(n - r) * factorial(r)));
  
  }
      
}

