package model;

import java.util.BitSet;
import java.util.TreeMap;

/**
 *
 * @author hkhoi
 */
public class TriDict {
    
    private final BitSet bitSet;
    private final TreeMap<Integer, Integer> idFreq;
    
    public TriDict() {
        idFreq = new TreeMap<>();
        bitSet = new BitSet();
    }
    
    public void increaseFreq(int docId) {
        if (!idFreq.containsKey(docId)) {
            idFreq.put(docId, 0);
        }
        int curFreq = idFreq.get(docId);
        idFreq.put(docId, curFreq + 1);
    }
    
    public void setBitSet(int docId) {
        bitSet.set(docId);
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public int getIdFreq(int docId) {
        return idFreq.get(docId);
    }

    @Override
    public String toString() {
        return "TriDict{" + "bitSet=" + bitSet + ", idFreq=" + idFreq + '}';
    }
}
