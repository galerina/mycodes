import java.util.Arrays;

public class CircularSuffixArray {
    private int[] suffixIndices;
    private String string;


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        string = s;
        suffixIndices = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixIndices[i] = i;
        }
        SuffixSort.sort(s, suffixIndices);
    }

    // length of s
    public int length() {
        return suffixIndices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return suffixIndices[i];
    }

    private void print() {
        for (int index : suffixIndices) {
            System.out.println(string.substring(index) + string.substring(0, index));
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        new CircularSuffixArray("I am not a crook!").print();
    }
}
