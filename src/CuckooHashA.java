import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Hashes using Standard Hash Table
 */
public class CuckooHashA<E>  {

    private final int capacity;
    private int size = 0;
    private ArrayList<LinkedList<HashPair<E>>> table;
    private final int numHash;
    private ArrayList<HashFunction> hashFunctions;
    private final int threshold;
    private final int cycleLimit;
    private long numOperations;
    private long numAccess;
    private long numEvaluate;
    private int numRehash;

    public CuckooHashA (int capacity, int numHash, int threshold) {
        this.hashFunctions = new ArrayList<>();
        for (int i = 0; i < numHash; i++) {
            this.hashFunctions.add(new HashFunction(capacity));
        }
        this.capacity = capacity;
        this.numHash = numHash;
        this.threshold = threshold;
        this.cycleLimit = Math.toIntExact(Math.round(10 * Math.log(capacity)));
        this.table = new ArrayList<>();
        this.size = 0;
        for (int i = 0; i < capacity; i++) {
            table.add(new LinkedList<>());
        }
        this.numOperations = 0;
        this.numEvaluate = 0;
        this.numAccess = 0;
        this.numRehash = 0;
    }

    public boolean contains(E X) {
        for (int i = 0; i < numHash; i++) {
            HashFunction hf = hashFunctions.get(i);
            LinkedList<HashPair<E>> list = table.get(hf.evaluate(X.hashCode()));
            if (!list.isEmpty()) {
                for (HashPair<E> pair : list) {
                    this.numOperations++;
                    if (pair.getX().equals(X)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean add(E X) {
        /* dont allow duplicates */
        if (contains(X)) {
            return false;
        }
        E toAdd = X;
        int index = 0;
        int key = Math.toIntExact(hashFunctions.get(0).evaluate(toAdd.hashCode()));
        LinkedList<HashPair<E>> list = table.get(key);
        int cost = 0;
        /*Until we find a list where its size is smaller than threshold
        * or we exceed cycleLimit*/
        while (list.size() >= threshold && cost <= cycleLimit) {
            cost++;
            //System.out.println("Cost= "+ cost);
            HashPair<E> removed = list.poll();
            list.add(new HashPair<>(toAdd, index));
            toAdd = removed.getX();
            index = (removed.getY() + 1) % numHash;
            key = Math.toIntExact(hashFunctions.get(index).evaluate(toAdd.hashCode()));
            list = table.get(key);
        }
        //list size below threshold
        if (list.size() < threshold) {
            list.add(new HashPair<>(toAdd, index));
           //System.out.println(key);
            return true;
        } else {
            rehash();
            add(toAdd);
            return true;
        }

    }

    private void rehash() {
        numRehash++;
        System.out.println("rehash number: " + numRehash);
        ArrayList<LinkedList<HashPair<E>>> temp = table;
        LinkedList<HashPair<E>> allPairs = new LinkedList<>();

        for (LinkedList<HashPair<E>> list : table) {
            allPairs.addAll(list);
        }
        //get new hash functions
        hashFunctions = new ArrayList<>();
        for (int i = 0; i < numHash; i++) {
            this.hashFunctions.add(new HashFunction(capacity));
        }
        //get new hash table
        table = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            table.add(new LinkedList<>());
        }

        //add back, if infinite loop occurs again,
        //add recursively implements rehash
        for (HashPair<E> pair : allPairs) {
            add(pair.getX());
        }
    }

    public int getSize() {
        int size = 0;
        for (LinkedList<HashPair<E>> list : table) {
            size += list.size();
        }
        return size;
    }
}
