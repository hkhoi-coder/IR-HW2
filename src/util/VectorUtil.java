package util;

import java.util.List;

/**
 *
 * @author hkhoi
 */
public class VectorUtil {
    public static double distance(List<Double> vector0, List<Double> vector1) {
        double sum = 0;
        for (int i = 0; i < vector0.size(); ++i) {
            double dif = vector0.get(i) - vector1.get(i);
            sum += Math.pow(dif, 2);
        }
        
        return Math.sqrt(sum);
    }
    
    public static double cosSim(List<Double> vector0, List<Double> vector1) {
        return dotProduct(vector0, vector1) / (length(vector0) * length(vector1));
    }
    
    public static double dotProduct(List<Double> vector0, List<Double> vector1) {
        double dotProduct = 0;
        for (int i = 0; i < vector0.size(); ++i) {
            dotProduct += (vector0.get(i) * vector1.get(i));
        }
        
        return dotProduct;
    }
    
    public static double length(List<Double> vector) {
        double result = 0;
        
        for (double it : vector) {
            result += Math.pow(it, 2);
        }
        
        return Math.sqrt(result);
    }
}
