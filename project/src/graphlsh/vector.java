/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;
import java.util.Random;
public class vector extends LSHElem {
    
    int size;//size of the vector,dimension of the vector
    double elements[];//array to store the elements, can be any type
    
    public vector(int length) //constructor for the class
    {
        this.size = length;
        this .elements = new double[this.size];
    }
    
         
    void generateRandomVector()//generate the random vectors according to the gaussian distribution with mean = 0 and variance = 1
    {
        Random randNumber = new Random();
        for(int i=0;i<size;i++)
        {
            elements[i] = randNumber.nextGaussian();
        } 
       
    }
    
    @Override
    LSHElem generateRandom(){
        vector ret = new vector(this.size);
        ret.generateRandomVector();
        return ret;
    }
    
    /*method to find the dot prodcut of the vector v1 and v2*/
    @Override
    double dotProduct(LSHElem v)
    {
        double sum = 0.0;
        vector v2 = (vector) v;
       /* System.out.println("vector 1");
        for(int i = 0;i<v2.size;i++)
        {
            System.out.print(v2.elements[i]+" ");
        }    
        System.out.println(" ");
        System.out.println("vector 2");
        for(int i=0;i<this.size;i++)
        {
            System.out.print(this.elements[i]+" ");
        }    
       // */
        for(int i = 0;i<size;i++)
        {
           // System.out.print("the element that we are going to multiply are"+this.elements[i]+"\t"+v2.elements[i]);
            sum += this.elements[i]*v2.elements[i]; //sum will hold the dot product
        }    
        return sum;
    }        
    
    /*method to add the elements of the vector v1 and v2*/
    vector addVectors(vector v2)
    {
        
        vector sumVector = new vector(size);
        
        for(int i = 0;i<size;i++)
        {
            sumVector.elements[i] = this.elements[i]+v2.elements[i];
        }    
        
        return sumVector;
    }        

    @Override
    void printElement() 
    {
        for(int i = 0;i<this.size;i++)
        {
            System.out.print(this.elements[i]+" ");
        }    
    }
   
    vector normalize()//method to normalize the vectors
    {
	vector zerovector = new vector(11);
	for(int i = 0;i<zerovector.size;i++)
	{
	  zerovector.elements[i] = 0;
	}
        float sum = 0;
        /*System.out.println("vector before normalization");
        for(int i = 0;i<this.size;i++)
        {
            System.out.print(this.elements[i]+" ");
        } 
        */ 
        for(int i = 0;i<this.size;i++)//square each elements
        {
            sum += this.elements[i]*this.elements[i];
        }
       if(sum>0)
       { 
        float magnitude = (float)Math.sqrt(sum);//find the magnitude of the sum
        for(int i = 0;i<this.size;i++)//normalize each elements
        {
            this.elements[i] = this.elements[i]/magnitude;
        }    
        return this;
      }
      else
      {
	return zerovector;
      }
 
    }
    
    public  String toString()
    {
      StringBuilder result = new StringBuilder();
       //result.append(s);
        for(int i = 0;i<this.size;i++)
        {
            result.append(this.elements[i]);
            result.append(" ");
        } 
        //*/
        return result.toString();
    }
    
    
}
