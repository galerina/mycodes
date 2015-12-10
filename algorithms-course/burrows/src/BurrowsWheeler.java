import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;   // extend ASCII alphabet size

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String inputStr = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(inputStr);

        int indexOfFirst = 0;
        char[] lastCharactersInCSA = new char[csa.length()];
        int rowOfOriginalStr = 0;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                rowOfOriginalStr = i;
            }

            int indexOfLastCharacter = (csa.index(i) + csa.length() - 1) % csa.length();
            lastCharactersInCSA[i] = inputStr.charAt(indexOfLastCharacter);
        }

        BinaryStdOut.write(rowOfOriginalStr, 32);
        BinaryStdOut.write(new String(lastCharactersInCSA));
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        char[] lastChars = BinaryStdIn.readString().toCharArray();
        char[] firstChars = radixSorted(lastChars);

        int[] next = new int[lastChars.length];

        // Keep track of the current index in first when constructing next
        int[] indexInFirst = new int[R];
        indexInFirst[firstChars[0]] = 0;
        char currentChar = firstChars[0];
        for (int i = 1; i < firstChars.length; i++) {
            if (firstChars[i] != currentChar) {
                indexInFirst[firstChars[i]] = i;
                currentChar = firstChars[i];
            }
        }

        for (int i = 0; i < lastChars.length; i++) {
            next[indexInFirst[lastChars[i]]++] = i;
        }

        char[] message = new char[firstChars.length];
        int currentIndex = first;
        int count = 0;
        while (count < firstChars.length) {
            message[count++] = firstChars[currentIndex];
            currentIndex = next[currentIndex];
        }

        BinaryStdOut.write(new String(message));
        BinaryStdOut.flush();
    }


    private static char[] radixSorted(char[] a) {
        int N = a.length;
        char[] sortedArr = new char[N];

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++) {
            count[a[i] + 1]++;
        }

         // compute cumulates
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // move data
        for (int i = 0; i < N; i++) {
            sortedArr[count[a[i]]++] = a[i];
        }

        return sortedArr;
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
