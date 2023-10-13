package fr.blind.anonymous.rbpmn.enforcement;

import java.util.LinkedList;
import java.util.List;

public class Buffer<T> {
	
    private LinkedList<T> queue = new LinkedList<T>();
    
    
    public void add(T element) {
    	
    	if(!queue.contains(element)) {
    		queue.add(element);
    	}
    }
    
    public void addAll(List<T> elements) {
    	for(T element: elements) {
    		if(!queue.contains(element)) {
        		queue.add(element);
        	}
    	}
    }
    
    public void addFirst(T element) {
    	queue.addFirst(element);
    }
    
 // Add element to the end of the queue
    public void enqueue(T element) {
        queue.addLast(element);
    }
    
    // Remove the last element
    public void remove() {
    	queue.remove();
    }
    
    public void remove(int index) {
    	queue.remove(index);
    }
    
    public void remove(T element) {
    	queue.remove(element);
    }
    
    public T get(int index) {
        return queue.get(index);
    }
    
    // Remove and return the first element of the queue
    public T poll() {
        return queue.removeFirst();
    }

    // Return the first element of the queue without removing it
    public T peek() {
        return queue.getFirst();
    }

    // Return the size of the queue
    public int size() {
        return queue.size();
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public void clear() {
       // Delete operation back to is queue change
       int size = queue.size();
       
       for(int i=0; i< size;i++) {
    	   queue.poll();
       }
    }
    
    public List<T> subList(int indexStart, int indexEnd){
    	List<T> sublist = new LinkedList<>();
    	for(int i = indexStart; i < indexEnd; i++) {
    		sublist.add(queue.get(i));
    	}
    	return sublist;
    }
    
    public boolean contains(T element) {
        return queue.contains(element);
     }
    
    public String toString() {
    	return "(size:" + queue.size() + ")" + queue + "";
    }
}
