import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        ArrayList<Character> charSequence = new ArrayList<Character>();
        for (int i = 0; i <= 255; i++) {
            charSequence.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = charSequence.indexOf(c);
            BinaryStdOut.write(index, 8);

            moveCharToFront(charSequence, index, c);
        }

        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> charSequence = new ArrayList<Character>();
        for (int i = 0; i <= 255; i++) {
            charSequence.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = charSequence.get(index);
            BinaryStdOut.write(c);

            moveCharToFront(charSequence, index, c);
        }

        BinaryStdOut.flush();
    }

    private static void moveCharToFront(ArrayList<Character> list, int index, char c) {
        for (int i = index - 1; i >= 0; i--) {
            list.set(i+1, list.get(i));
        }

        list.set(0, c);
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
