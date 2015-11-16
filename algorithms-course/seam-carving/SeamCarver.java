import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class SeamCarver {
    Color[][] image;
    boolean isTransposed;
    int width;
    int height;

    private static final boolean DEBUG = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }

        image = new Color[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                image[i][j] = picture.get(j,i);
            }
        }
        isTransposed = false;
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                pic.set(j,i,image[i][j]);
            }
        }

        return pic;
    }

    // width of current picture   
    public int width() {
        return image[0].length;
    }

    // height of current picture
    public int height() {
        return image.length;
    }

    // energy of pixel at col and row
    public double energy(int x, int y) {
        if (x < 0 || x >= image[0].length || y < 0 || y >= image.length) {
            throw new IndexOutOfBoundsException();
        }

        if (isBorderPixel(x, y)) {
            return 1000;
        }

        Color xMinusOne = image[y][x-1];
        Color xPlusOne = image[y][x+1];
        Color yMinusOne = image[y-1][x];
        Color yPlusOne = image[y+1][x];

        int Rx = xMinusOne.getRed() - xPlusOne.getRed();
        int Gx = xMinusOne.getGreen() - xPlusOne.getGreen();
        int Bx = xMinusOne.getBlue() - xPlusOne.getBlue();

        int xGradientSquared = Rx*Rx + Gx*Gx + Bx*Bx;

        int Ry = yMinusOne.getRed() - yPlusOne.getRed();
        int Gy = yMinusOne.getGreen() - yPlusOne.getGreen();
        int By = yMinusOne.getBlue() - yPlusOne.getBlue();

        int yGradientSquared = Ry*Ry + Gy*Gy + By*By;

        return Math.sqrt((double)xGradientSquared + yGradientSquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();

        int[] seam = findVerticalSeam();

        transpose();

        return seam;
    }

    // Find a sequence of indices for vertical seam
    // Algorithm:
    // This problem maps onto the topological sort shortest path algorithm; the energy
    // function determines the weight of the edges in the graph. We can create distTo
    // and edgeTo arrays that are the same dimensions as the image. For every (i,j) there
    // are directed edges to (i+1,j-1), (i+1,j), and (i+1,j+1) with weight equal toenergy(i,j).
    // If we process pixels (nodes) in the order (1,1),(1,2),(1,3),...,(2,1),(2,2),(2,3)... then
    // we will process nodes in a topological order. For an (i,j), set:
    //   distTo[i][j] = min(distTo[i-1][j-1],distTo[i-1][j],distTo[i-1][j+1]) + energy(i,j)
    //   edgeTo[i][j] = [j-1|j|j+1] where distTo[i-1][edgeTo[i][j]] is minimized
    //
    // Given these data structures we can reconstruct the least energy path by finding the minimum
    // distTo column in the last row and using edgeTo to follow the path backward.
    public int[] findVerticalSeam() {
        if (DEBUG) {
            printEnergy();
        }

        // Use Double instead of double so we can use Collections utility min()
        Double[][] distTo = new Double[image.length][image[0].length];
        int [][] edgeTo = new int[image.length][image[0].length];

        // First row
        int row = 0, col;
        for (col = 0; col < image[row].length; col++) {
            distTo[row][col] = energy(col,row);
        }

        // Other rows
        for (row = 1; row < image.length; row++) {
            // First column ineligible
            distTo[row][0] = Double.MAX_VALUE;

            for (col = 1; col < image[row].length - 1; col++) {
                // Consider (row-1,col-1),(row-1,col),(row-1,col+1)
                // and set distTo to the minimum energy path plus
                // this node's energy.
                double minValue = Double.MAX_VALUE;
                int minIndex = -1;

                for (int idx = col-1; idx <= col+1; idx++) {
                    if (distTo[row-1][idx] < minValue) {
                        minValue = distTo[row-1][idx];
                        minIndex = idx;
                    }
                }

                distTo[row][col] = minValue + energy(col,row);
                edgeTo[row][col] = minIndex;
            }

            // Last column ineligible
            distTo[row][image[row].length-1] = Double.MAX_VALUE;
        }

        int[] seamColIndices = new int[image.length];
        int lastRow = image.length - 1;
        List<Double> distToRow = Arrays.asList(distTo[lastRow]);
        seamColIndices[lastRow] = distToRow.indexOf(Collections.min(distToRow));
        for (row = lastRow-1; row >= 0; row--) {
            seamColIndices[row] = edgeTo[row+1][seamColIndices[row+1]];
        }

        return seamColIndices;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }

        if (!isValidHorizontalSeam(seam)) {
            throw new IllegalArgumentException();
        }

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }

        if (!isValidVerticalSeam(seam)) {
            throw new IllegalArgumentException();
        }

        Color[][] newImage = new Color[image.length][image[0].length-1];
        for (int i = 0; i < newImage.length; i++) {
            System.arraycopy(image[i], 0, newImage[i], 0, seam[i]);
            System.arraycopy(image[i], seam[i]+1, newImage[i], seam[i], newImage[i].length - seam[i]);
        }

        image = newImage;
    }

    private boolean isBorderPixel(int col, int row) {
        return (col == 0) || (col == image[0].length - 1) ||
            (row == 0) || (row == image.length - 1);
    }

    // Transpose the image array
    private void transpose() {
        Color[][] newImage = new Color[image[0].length][image.length];

        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {
                newImage[col][row] = image[row][col];
            }
        }

        image = newImage;
        isTransposed = !isTransposed;
    }

    private boolean isValidVerticalSeam(int[] seam) {
        return isValidSeam(seam, image.length, image[0].length);
    }

    private boolean isValidHorizontalSeam(int[] seam) {
        return isValidSeam(seam, image[0].length, image.length);
    }

    private boolean isValidSeam(int[] seam, int parallelDim, int perpindicularDim) {
        if (seam.length != parallelDim) {
            return false;
        }

        int previousSeamValue = seam[0];
        for (int i = 1; i < seam.length; i++) {
            // Is seam "connected"?
            if (Math.abs(previousSeamValue - seam[i]) > 1) {
                return false;
            }

            if (seam[i] < 0 || seam[i] >= perpindicularDim) {
                return false;
            }

            previousSeamValue = seam[i];
        }

        return true;
    }

    private void printEnergy() {
        StdOut.printf("\nENERGY:\n");
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                StdOut.printf("%7.2f ", energy(j,i));
            }
            StdOut.println();
        }
    }
}
