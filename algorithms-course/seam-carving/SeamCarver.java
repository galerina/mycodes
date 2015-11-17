import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class SeamCarver {
    private static final boolean DEBUG = false;

    private enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    private int[][] image;

    private double[][] distTo;
    private int [][] edgeTo;

    private boolean isTransposed;
    private int imageWidth;
    private int imageHeight;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }

        image = new int[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                image[i][j] = picture.get(j, i).getRGB();
            }
        }

        imageWidth = image[0].length;
        imageHeight = image.length;
        isTransposed = false;

        int maxDim = Math.max(imageHeight, imageWidth);
        edgeTo = new int[maxDim][maxDim];

        // Space optimization: we only need to keep track of two rows of distTo at a time
        distTo = new double[2][maxDim];
    }

    // current picture
    public Picture picture() {
        setImageColumnsToOrientation(Orientation.VERTICAL);
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                pic.set(j, i, new Color(image[i][j]));
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

        int xMinusOne = image[y][x-1];
        int xPlusOne = image[y][x+1];
        int yMinusOne = image[y-1][x];
        int yPlusOne = image[y+1][x];

        int Rx = getRed(xMinusOne) - getRed(xPlusOne);
        int Gx = getGreen(xMinusOne) - getGreen(xPlusOne);
        int Bx = getBlue(xMinusOne) - getBlue(xPlusOne);

        int xGradientSquared = Rx*Rx + Gx*Gx + Bx*Bx;

        int Ry = getRed(yMinusOne) - getRed(yPlusOne);
        int Gy = getGreen(yMinusOne) - getGreen(yPlusOne);
        int By = getBlue(yMinusOne) - getBlue(yPlusOne);

        int yGradientSquared = Ry*Ry + Gy*Gy + By*By;

        return Math.sqrt((double)xGradientSquared + yGradientSquared);
    }

    private boolean isImageBorderPixel(int col, int row) {
        return (col == 0) || (col == imageWidth - 1) ||
            (row == 0) || (row == imageHeight - 1);
    }

    // Transpose the image array. Creates a new array to copy into.
    private void transpose() {
        int[][] newImage = new int[imageWidth][imageHeight];

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
        initDistTo();

        double[] currentDistTo = distTo[1];
        double[] lastDistTo = distTo[0];

        // First row
        int row = 0, col;
        for (col = 1; col < imageWidth - 1; col++) {
            currentDistTo[col] = imageEnergy(col, row);
        }

        // Other rows
        for (row = 1; row < imageHeight; row++) {
            double[] temp = lastDistTo;
            lastDistTo = currentDistTo;
            currentDistTo = temp;


            // First and last columns ineligible; for imageWidth==1 the default
            // value of 0 in edgeTo is sufficient for creating a seam later.
            for (col = 1; col < imageWidth - 1; col++) {
                // Consider (row-1,col-1),(row-1,col),(row-1,col+1)
                // and set distTo to the minimum energy path plus
                // this node's energy.
                double minValue = Double.MAX_VALUE;
                int minIndex = col-1;

                for (int idx = col-1; idx <= col+1; idx++) {
                    if (lastDistTo[idx] < minValue) {
                        minValue = lastDistTo[idx];
                        minIndex = idx;
                    }
                }

                currentDistTo[col] = minValue + imageEnergy(col, row);
                edgeTo[row][col] = minIndex;
            }
        }

        int[] seamColIndices = new int[imageHeight];
        int lastRow = imageHeight - 1;

        seamColIndices[lastRow] = getIndexOfMin(currentDistTo, imageWidth);
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

    private void initDistTo() {
        for (int i = 0; i < distTo.length; i++) {
            for (int j = 0; j < distTo[i].length; j++) {
                distTo[i][j] = Double.MAX_VALUE;
            }
        }
    }

    private int getRed(int x) {
        return (x >> 16) & 0xff;
    }

    private int getGreen(int x) {
        return (x >> 8) & 0xff;
    }

    private int getBlue(int x) {
        return x & 0xff;
    }

    private int getIndexOfMin(double[] d, int length) {
        int minIndex = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if (d[i] < min) {
                minIndex = i;
                min = d[i];
            }
        }

        return minIndex;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(8,1);

        SeamCarver seamCarver = new SeamCarver(picture);
        seamCarver.findHorizontalSeam();
        // Test illegal argument exception behavior
        // int[] seam = { -1, 0, 1, 2, 3, 3, 3, 3, 3, 3 };
        // seamCarver.removeVerticalSeam(seam);
    }
}
