/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package graphlsh;

public class queue {
    
       
 int rear ;//rear end of the queue
 int front ;//front end of the queue
 int []outArray;//temperory array where the nodes after deque is stored	
 int []queueArray;
 int numberOfNodes;
public queue(int num)
{
    this.rear = - 1; 
    this.front = - 1;
    this.numberOfNodes = num;
    this.outArray = new int[2*numberOfNodes];
    this.queueArray = new int[2*numberOfNodes];
}     

/*insert the element into the queue*/
public  void insert(int element)
{
    if (front == - 1)//if there is no element in the queue insert it as the first element and increment front
    {
	front = 0;
	
    }

    if(rear==((2*numberOfNodes)-1) && front >0 && front<((2*numberOfNodes)-1))//if there are elements in the queue
    {
    
        rear=-1;

    }
    
    rear = rear + 1;
    
    queueArray[rear] = element;//add the element into the rear end of the queue

}

//delete function

 public  int delete()
 {
     int deletedElement=0;
    
    if(front==rear)//if front  and rear equates that means queue is empty,reset it
    {
        deletedElement = front;
    	rear = -1;
        front = -1;    
    	
    }
    else
    {
        if(front==((2*numberOfNodes)-1) && rear<front)//delete counter is at 999 and insert counter is less than the value of delete counter(condition according to code)
    	{
            front=0;
    	
            deletedElement = (2*numberOfNodes)-1;
    	
        }
   	
        else
    	
        {
    	
            deletedElement = front;
    	
            front=front+1;
    	
        }
    	
    }
    
    return queueArray[deletedElement];

}
  
/*sort the elements in the queue using insertion sort*/
public void sort(int counter)
{
    int j=0;
    
    for(int i =1;i<counter;i++)
    {
        int key = outArray[i];
        j=i-1;
        while((j>=0)&&outArray[j]>key)
        {
            int temp = outArray[j];
            outArray[j] = outArray[j+1];
            outArray[j+1] = temp;
            j--;
        }    
    }   
}        
    
    
    
    
    
}
