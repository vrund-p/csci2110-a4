import java.util.ArrayList;


public class Queue<T>{
    // declare all required fields
    private ArrayList<T> queue;
    private int size;
    private int cursor;

    /**
     Constructor 1
     No args.
     */
    public Queue(){
        queue = new ArrayList<>();
        size = 0;
        cursor = 0;
    }

    //YOUR WORK STARTS HERE

    //this method will return the size of queue
    public int size(){
        return queue.size();
    }

    // this method will return top element of queue
    public T peek(){
        return queue.get(queue.size()-1);
    }

    // this element will remove the first element of queue
    public T dequeue(){
        if(!isEmpty()){
            T temp;
            temp = queue.get(0);
            queue.remove(queue.get(0));
            size--;
            return temp;
        }
        else{
            return null;
        }


    }

    // this method will add elements to queue at end
    public void enqueue(T element){
        queue.add(element);
        size++;
    }

    // this method will check if queue is empty or not
    public boolean isEmpty(){
        return queue.size() == 0;
    }

    // this method will remove all elements of queue
    public void clear(){
        queue.clear();
    }

    // this method will return the position of  specific element
    public int positionOf(T item){
        for(int i=0; i<queue.size(); i++){
            if(item.equals(queue.get(i))){
                return i;
            }
        }
        return -1;
    }

    // this method will remove the first element of queue
    public void remove(T item){
        queue.remove(queue.get(0));
    }

    // this method will return first element of queue
    public T first(){
        if(queue.size() > 0){
            return queue.get(0);
        }
        else{
            return null;
        }
    }

    // this method will return the next item in the Queue relative to the previous call to first or next
    public T next(){
        if(cursor == queue.size()){
            return null;
        }
        else{
            cursor++;
            return queue.get(cursor);
        }
    }

    // printing format
    public void print(){
        for(int i=0; i<queue.size(); i++){
            System.out.println(queue.get(i));
        }
    }

}

