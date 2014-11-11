/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

/**
 *
 * @author kiran
 */
public class GopsVector extends LSHOps{
    
    public GopsVector(){
        
    }
    

    @Override
   /*This function calculates the random vector needed to calculate the location in the LSH hashtables*/
    vector generateRandom(LSHElem element) 
    {
      vector randomVector = new vector(11);//random vector is generated according to the size of the graph
      randomVector.generateRandomVector();//calls the generate random function from the vector class
      randomVector.normalize();
      return randomVector;
      
    }
    
    /*Method to calculate the dot product*/
    @Override
    double dotProduct(LSHElem element1,LSHElem element2) 
    {  
        vector tempVector = (vector)element1;
        Graphs graphElement = (Graphs)element2;//instance element2 will be a graph so it need to be converted into the vector
        double sum = 0.0;
        sum = tempVector.dotProduct(graphElement.graphvec);//inner product is found
        return sum;
    }
    
    
    
}


