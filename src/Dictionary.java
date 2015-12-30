
/**
 * Created by NikhilVerma on 12/10/15.
 */
public class Dictionary {
    Bucket[] buckets;
    int count;
    private int maxCode;
    private Hash hashStrategy;
    private int codeRange;

    public static Dictionary prePopulatedDictionary(int size){
        Dictionary dictionary=new Dictionary(size);
        for (char ch = 0; ch < 2*Byte.MAX_VALUE; ch++) {
//            if(ch==126){
//                System.out.println("reached last constant");
//            }
            dictionary.addShorthand(ch+"");
        }
        return dictionary;
    }

    public Dictionary(int size) {
        this.codeRange =size;
        buckets =new Bucket[size];
        count=0;
        maxCode=0;
        hashStrategy =new ShiftAddXORHash();
    }

    public boolean addShorthand(String symbol){
        if(maxCode>= codeRange){//TODO experimental
            return false;
        }
        int i= getBucketIndex(symbol);
        if(buckets[i]==null){
            buckets[i]=new Bucket();
        }
//        if(maxCode>=350&&maxCode<=360){
//            System.out.println("output: "+symbol+" : "+maxCode);
//        }
        buckets[i].addToBucket(symbol,maxCode++);
        return true;
    }

    public StringCode lookup(String symbol){
        int i= getBucketIndex(symbol);
        if(buckets[i]!=null){
            return buckets[i].findShorthand(symbol);
        }else{
            return null;
        }
    }

    public int getBucketIndex(String key){
        int hash = hashStrategy.hash(key);
        hash=hash<0?hash*-1:hash;
        return hash% buckets.length;
    }

}

interface Hash{
    int hash(String key);
}

class ShiftAddXORHash implements Hash{

    @Override
    public int hash(String key) {
        int hash=0;
        byte[] bytes=key.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            hash=hash^((hash<<5)+(hash>>2)+bytes[i]);
        }
        return hash;
    }
}

/**
 * Bucket used to distribute data uniformly across the array.
 * Its possible that 2 or more strings reside in the same bucket.
 * In such a case it linearly adds to this bucket as a linked buckets.
 *
 * @author Nikhil Verma
 * @version 1.0: Bucket.java
 *          Revisions:
 *          Initial revision
 */
class Bucket {
    StringCode start;

    /**
     * Adds to this bucket.
     * @param symbol the string to add
     * @return always true as its adding in a linked list
     */
    public boolean addToBucket(String symbol,int code){
//        if(symbol.equals("er")){
//            System.out.println("adding er at code: "+code);
//        }
        StringCode newStringCode =new StringCode(symbol,code);
        //we prepend,not append
        if(start==null){
            start= newStringCode;
        }else{
            newStringCode.next=start;
            start= newStringCode;
        }
        return true;
    }

    /**
     * Removes the first occurence of the string(node) from the bucket.
     * @param string the string to remove
     * @return true if removed(after finding it) false otherwise
     */
    public boolean removeFromBucket(String string){
        StringCode t=start;
        StringCode previous=null;
        while(t!=null){
            if(t.symbol.equals(string)){
                break;
            }
            previous=t;
            t=t.next;
        }
        if(t!=null){
            if(previous==null){//first node
                start=start.next;
            }else{
                previous.next=t.next;//skip t
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Finds a string node from this bucket by linearly traversing it.
     * @param string the string to search for
     * @return the Shorthand which contains the string.
     */
    public StringCode findShorthand(String string){
        StringCode t=start;
        while(t!=null){
            if(t.symbol.equals(string)){
                return t;
            }
            t=t.next;
        }
        return null;
    }

    /**
     * @return true if the bucket is empty,false otherwise
     */
    public boolean isEmpty(){
        if(start==null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Counts the total nodes in this bucket
     * @return total nodes in this bucket
     */
    public int totalShorthands(){
        int sum=0;
        StringCode t=start;
        while (t != null) {
            sum++;
            t=t.next;
        }
        return sum;
    }

    /**
     * Finds the node at the specified index
     * @param index starts from 0
     * @return the node at index or null if index is beyond the boundaries.
     */
    public StringCode nodeAtIndex(int index){
        StringCode stringNode;
        int i=0;
        StringCode t=start;
        while ((i<index)&&(t != null) ){
            if(i==index){
                return t;
            }
            i++;
            t=t.next;
        }

        return null;
    }
}
