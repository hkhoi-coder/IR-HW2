package model;

/**
 *
 * @author hkhoi
 */
public class FileRelevant implements Comparable<FileRelevant> {

    private String name;
    private double relevant;

    public FileRelevant(String name, double relevant) {
        this.name = name;
        this.relevant = relevant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRelevant() {
        return relevant;
    }

    public void setRelevant(double relevant) {
        this.relevant = relevant;
    }

    @Override
    public int compareTo(FileRelevant o) {
        if (o.relevant > relevant) {
            return 1;
        } else if (o.relevant < relevant) {
            return -1;
        }
        
        return 0;
    }

    @Override
    public String toString() {
        return "FileRelevant{" + "name=" + name + ", relevant=" + relevant + '}';
    }
}
