import java.util.Arrays;

public class CircularSuffixArray {
    private CircularSuffix[] suffixes;

    private static class CircularSuffix implements Comparable {
        private String string;
        private int index;

        private CircularSuffix(String s, int i) {
            this.string = s;
            this.index = i;
        }

        private char charAt(int i) {
            return string.charAt((index + i) % string.length());
        }

        public int compareTo(Object o) {
            CircularSuffix other = (CircularSuffix) o;
            int i = 0;
            while (i < string.length() && this.charAt(i) == other.charAt(i)) {
                i++;
            }

            if (i == string.length()) {
                return 0;
            } else {
                return ((int) this.charAt(i) - other.charAt(i));
            }
        }

        public String toString() {
            return string.substring(index) + string.substring(0, index);
        }

        private int getIndex() {
            return index;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        suffixes = new CircularSuffix[s.length()];

        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }

        Arrays.sort(suffixes);
    }

    // length of s
    public int length() {
        return suffixes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return suffixes[i].getIndex();
    }

    private void print() {
        for (CircularSuffix s : suffixes) {
            System.out.println(s);
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        new CircularSuffixArray("I am not a crook!").print();
    }
}
