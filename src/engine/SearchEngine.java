package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import model.TriDict;
import util.QuickScan;

/**
 *
 * @author hkhoi
 */
public class SearchEngine {

    private static final String DATA_DIR = "data";

    private static final String STOP_WORDS_NAME = "stopwords.txt";

    private static final String REGEX = "[^A-Za-z']";

    private final HashSet<String> stopWordSet;

    private final TreeMap<String, TriDict> fullInvertedIndex;

    private final File[] dataFiles;

    private final int[] terms;

    public SearchEngine() throws IOException {
        stopWordSet = new HashSet<>();
        fullInvertedIndex = new TreeMap<>();
        dataFiles = (new File(DATA_DIR)).listFiles();
        terms = new int[dataFiles.length];

        loadStopWords();
        buildInvertedIndex();
    }

    /* PART 1 AND 2 */
    private void buildInvertedIndex() throws FileNotFoundException, IOException {
        for (int i = 0; i < dataFiles.length; ++i) {
            loadTermsFromFile(i);
        }
    }

    public List<String> queryInvertedIndex(String term) {
        BitSet bitSet = fullInvertedIndex.get(term).getBitSet();

        if (bitSet == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        for (int i = 0; i < bitSet.length(); ++i) {
            if (bitSet.get(i)) {
                result.add(dataFiles[i].getName());
            }
        }

        return result;
    }

    private void loadStopWords() throws FileNotFoundException, IOException {
        QuickScan scan = new QuickScan(new FileInputStream(STOP_WORDS_NAME));
        boolean stop = false;
        while (!stop) {
            String curLine = scan.next();
            if (curLine != null) {
                stopWordSet.add(curLine);
            } else {
                stop = true;
            }
        }
    }

    private void loadTermsFromFile(int id) throws FileNotFoundException, IOException {
        File curFile = dataFiles[id];
        QuickScan scan = new QuickScan(new FileInputStream(curFile));
        boolean stop = false;
        while (!stop) {
            String curLine = scan.nextLine();
            if (curLine == null) {
                stop = true;
            } else {
                for (String token : curLine.split(REGEX)) {
                    String normalized = token.toLowerCase();
                    terms[id] += 1;
                    if (!normalized.isEmpty() && normalized.charAt(0) == '\'') {
                        normalized = normalized.substring(1);
                    }
                    if (!normalized.isEmpty() && normalized.charAt(normalized.length() - 1) == '\'') {
                        normalized = normalized.substring(0, normalized.length() - 1);
                    }

                    if (!normalized.isEmpty() && !stopWordSet.contains(normalized)) {
                        if (!fullInvertedIndex.containsKey(normalized)) {
                            fullInvertedIndex.put(normalized, new TriDict());
                        }
                        fullInvertedIndex.get(normalized).setBitSet(id);
                        fullInvertedIndex.get(normalized).increaseFreq(id);
                    }
                }
            }
        }
    }

    public List<String> generateRandomDictionary(int num) {
        ArrayList<String> list = new ArrayList<>(fullInvertedIndex.keySet());
        Collections.shuffle(list);
        return list.subList(0, num);
    }

    /* PART 3 */
    public List<Double> getTF_IDF(String query) {
        int terms = 0;
        TreeMap<String, Integer> termFreq = new TreeMap<>();
        for (String it : fullInvertedIndex.keySet()) {
            termFreq.put(it, 0);
        }

        for (String token : query.split(REGEX)) {
            String normalized = token.toLowerCase();
            terms += 1;
            if (!normalized.isEmpty() && normalized.charAt(0) == '\'') {
                normalized = normalized.substring(1);
            }
            if (!normalized.isEmpty() && normalized.charAt(normalized.length() - 1) == '\'') {
                normalized = normalized.substring(0, normalized.length() - 1);
            }

            if (!normalized.isEmpty() && !stopWordSet.contains(normalized)) {
                int freq = termFreq.get(normalized);
                termFreq.put(normalized, freq + 1);
            }
        }

        List<Double> vector = new ArrayList<>();

        for (Map.Entry<String, Integer> it : termFreq.entrySet()) {
            double tf = (double) it.getValue() / terms;
            int docsContainTerm = fullInvertedIndex.get(it.getKey()).getBitSet().cardinality();
            double ratio = (double) dataFiles.length / docsContainTerm;
            double idf = Math.log(ratio);
            vector.add(tf * idf);
        }

        return vector;
    }

    // For documents
    public List<Double> getTF_IDF(int fileId) {
        List<Double> vector = new ArrayList<>();

        for (Map.Entry<String, TriDict> it : fullInvertedIndex.entrySet()) {
            // TF
            int termFrequency = 0;
            try {
                termFrequency = it.getValue().getIdFreq(fileId);
            } catch (NullPointerException e) {
                // Do nothing
            }

            double tf = (double) termFrequency / terms[fileId];

            // IDF
            int docsContainTerm = it.getValue().getBitSet().cardinality();
            double ratio = (double) dataFiles.length / docsContainTerm;
            double idf = Math.log(ratio);

            vector.add(tf * idf);
        }

        return vector;
    }

    /* TESTING SECTION */
    public void testContainsStopWords() {
        for (String it : stopWordSet) {
            if (fullInvertedIndex.containsKey(it)
                    || it.charAt(0) == '\''
                    || it.charAt(it.length() - 1) == '\'') {
                System.out.println("ERROR: " + it);
            }
        }

        System.out.println("TEST #2 DONE");
    }

    public void testRandomWords() throws FileNotFoundException {
        String[] fileNames = {"RANDOM_0", "RANDOM_1", "RANDOM_2", "RANDOM_3"};
        for (String itFileName : fileNames) {
            PrintWriter out = new PrintWriter(new FileOutputStream(itFileName), false);
            for (String itWord : generateRandomDictionary(500)) {
                out.println(itWord);
            }
            out.close();
        }
    }

    public void testFileIds() throws FileNotFoundException {
        PrintWriter out
                = new PrintWriter(new FileOutputStream("FILE_INDEX"), false);

        for (int i = 0; i < dataFiles.length; ++i) {
            out.println(i + ": " + dataFiles[i].getName());
        }

        out.close();
    }

    public String getFileName(int docId) {
        return dataFiles[docId].getName();
    }
}
