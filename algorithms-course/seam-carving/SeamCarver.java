import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class SeamCarver {
    private static final boolean DEBUG = true;

    private enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    private Color[][] image;
    private boolean isTransposed;
    private int imageWidth;
    private int imageHeight;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }

        image = new Color[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                image[i][j] = picture.get(j, i);
            }
        }

        imageWidth = image[0].length;
        imageHeight = image.length;
        isTransposed = false;
    }

    // current picture
    public Picture picture() {
        setImageColumnsToOrientation(Orientation.VERTICAL);
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                pic.set(j, i, image[i][j]);
            }
        }

        return pic;
    }

    // width of current picture   
    public int width() {
        if (isTransposed) {
            return imageHeight;
        } else {
            return imageWidth;
        }
    }

    // height of current picture
    public int height() {
        if (isTransposed) {
            return imageWidth;
        } else {
            return imageHeight;
        }
    }

    // energy of pixel at col and row
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }

        if (isTransposed) {
            return imageEnergy(y, x);
        } else {
            return imageEnergy(x, y);
        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(Orientation.HORIZONTAL);
    }

    // Find a sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(Orientation.VERTICAL);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        removeSeam(seam, Orientation.HORIZONTAL);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        removeSeam(seam, Orientation.VERTICAL);
    }


    private double imageEnergy(int x, int y) {
        if (isImageBorderPixel(x, y)) {
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

    private boolean isImageBorderPixel(int col, int row) {
        return (col == 0) || (col == imageWidth - 1) ||
            (row == 0) || (row == imageHeight - 1);
    }

    // Transpose the image array. Creates a new array to copy into.
    private void transpose() {
        if (DEBUG) {
            StdOut.printf("transpose()\n");
        }

        Color[][] newImage = new Color[imageWidth][imageHeight];

        for (int row = 0; row < imageHeight; row++) {
            for (int col = 0; col < imageWidth; col++) {
                newImage[col][row] = image[row][col];
            }
        }

        image = newImage;

        // Swap internal imageHeight and imageWidth
        int temp = imageHeight;
        imageHeight = imageWidth;
        imageWidth = temp;
 
        isTransposed = !isTransposed;
    }


    private boolean isValidSeam(int[] seam, Orientation orientation) {
        int parallelDim, perpindicularDim;
        if (orientation == Orientation.VERTICAL) {
            parallelDim = height();
            perpindicularDim = width();
        } else {
            assert orientation == Orientation.HORIZONTAL;
            parallelDim = width();
            perpindicularDim = height();
        }

        if (seam.length != parallelDim) {
            return false;
        }

        if (seam[0] < 0 || seam[0] >= perpindicularDim) {
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

    // Find seam
    // If finding a horizontal seam we transpose the array so the algorithm always goes 
    // in row-column loop order.
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
    private int[] findSeam(Orientation orientation) {
        setImageColumnsToOrientation(orientation);

        // Use Double instead of double so we can use Collections utility min()
        Double[][] distTo = new Double[imageHeight][imageWidth];
        int [][] edgeTo = new int[imageHeight][imageWidth];

        // First row
        int row = 0, col;
        for (col = 0; col < imageWidth; col++) {
            distTo[row][col] = imageEnergy(col, row);
        }

        // Other rows
        for (row = 1; row < imageHeight; row++) {
            // First column ineligible
            distTo[row][0] = Double.MAX_VALUE;

            for (col = 1; col < imageWidth - 1; col++) {
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

                distTo[row][col] = minValue + imageEnergy(col, row);
                edgeTo[row][col] = minIndex;
            }

            // Last column ineligible
            distTo[row][imageWidth-1] = Double.MAX_VALUE;
        }

        int[] seamColIndices = new int[imageHeight];
        int lastRow = imageHeight - 1;
        List<Double> distToRow = Arrays.asList(distTo[lastRow]);
        seamColIndices[lastRow] = distToRow.indexOf(Collections.min(distToRow));
        for (row = lastRow-1; row >= 0; row--) {
            seamColIndices[row] = edgeTo[row+1][seamColIndices[row+1]];
        }

        return seamColIndices;
    }

    private void removeSeam(int[] seam, Orientation orientation) {
        if (seam == null) {
            throw new NullPointerException();
        }

        if (!isValidSeam(seam, orientation)) {
            throw new IllegalArgumentException();
        }

        setImageColumnsToOrientation(orientation);

        int newImageWidth = imageWidth - 1;
        for (int i = 0; i < imageHeight; i++) {
            // Squeeze out the seam pixel
            System.arraycopy(image[i], seam[i]+1, image[i], seam[i], newImageWidth - seam[i]);
        }

        imageWidth = newImageWidth;
    }

    // Is it worth it having two representations of the same thing?
    //  ("isTransposed" and "image columns orientation")
    private void setImageColumnsToOrientation(Orientation orientation) {
        if (orientation == Orientation.VERTICAL) {
            if (isTransposed) {
                transpose();
            }
        } else {
            assert orientation == Orientation.HORIZONTAL;
            if (!isTransposed) {
                transpose();
            }
        }
    }

    private void printEnergy() {
        setImageColumnsToOrientation(Orientation.VERTICAL);
        StdOut.printf("\nENERGY:\n");
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                StdOut.printf("%7.2f ", energy(j, i));
            }
            StdOut.println();
        }
    }

    public static void main(String[] args) {
        Picture picture = new Picture(10, 10);
        SeamCarver seamCarver = new SeamCarver(picture);

        // Test illegal argument exception behavior
        int[] seam = { -1, 0, 1, 2, 3, 3, 3, 3, 3, 3 };
        seamCarver.removeVerticalSeam(seam);
    }
}
