package com.ikueb.bell;


/**
 * Computes a Bell Triangle.
 * <p>
 * A Bell Triangle is a triangle of numbers, which count partitions of a set in which a given
 * element is the largest singleton. It is named for its close connection to the Bell numbers, which
 * may be found on both sides of the triangle.
 */
public class BellTriangle implements Cloneable {

    public static final int LIMIT = 24;
    private static final long[] ROW_ZERO = new long[] { 1 };
    private final long[][] triangle;

    /**
     * Computes a triangle up to the number of rows, <code>n</code>.
     *
     * @param n the number of rows, excluding <code>row 0</code>, to compute.
     * @see #compute(int)
     */
    public BellTriangle(int n) {
        triangle = compute(n);
    }

    /**
     * Compute the triangle up to the number of rows, <code>n</code>.
     * <p>
     * The smallest Bell Triangle contains a single-field row, <code>row 0</code>, with the value 1
     * representing the number of partitions for an <em>empty</em> set. Therefore, in computing the
     * triangle for a set of <code>n</code> elements, the resulting array has <code>n + 1</code>
     * rows. Row indices run from 0 to <code>n</code>, inclusive. Field indices also run in the same
     * range. The Bell Number of the triangle is simply the first array value of the
     * <code>n-th</code> row.
     * <p>
     * Due to maximum value of the <code>long</code> datatype, <code>n</code> must not be greater
     * than {@link BellTriangle#LIMIT}.
     *
     * @param n the number of rows, excluding <code>row 0</code>, to compute.
     * @return a two-dimension jagged array representing the computed triangle.
     * @see BellTriangle#LIMIT
     */
    private long[][] compute(int n) {
        if (n < 0 || n > LIMIT) {
            throw new IllegalArgumentException("Input must be between 0 and " + LIMIT
                    + ", inclusive.");
        }
        final long[][] result = new long[++n][];
        result[0] = ROW_ZERO;
        for (int i = 1; i < n; i++) {
            result[i] = new long[i + 1];
            result[i][0] = result[i - 1][i - 1];
            for (int j = 0; j < i; j++) {
                result[i][j + 1] = result[--i][j] + result[++i][j];
            }
        }
        return result;
    }

    /**
     * Gets the size of the triangle, i.e. the value of <code>n</code> used in the constructor.
     *
     * @return the triangle's size.
     */
    public final int getSize() {
        return triangle.length - 1;
    }

    /**
     * Gets the Bell Number for the triangle's size.
     *
     * @return the Bell Number.
     */
    public final long getBellNumber() {
        return triangle[getSize()][0];
    }

    /**
     * Given a positive <code>row</code> and <code>field</code>, return the value within the
     * triangle. Both indices start from zero.
     * <p>
     * <code>row</code> must be equal to or less than the triangle's size, and <code>field</code>
     * must also be equal to or less than <code>row</code>.
     *
     * @param row the row to seek.
     * @param field the field to return.
     * @return the value for the specified <code>row</code> and <code>field</code>.
     */
    public final long getValue(int row, int field) {
        if (row < 0 || field < 0) {
            throw new IllegalArgumentException("Both indices must be greater than 0.");
        }
        if (row > getSize()) {
            throw new IllegalArgumentException("Row index must be " + getSize() + " or less.");
        }
        if (row < field) {
            throw new IllegalArgumentException("Field index must not be greater than row index.");
        }
        return triangle[row][field];
    }

    /**
     * Copies the <code>triangle</code>.
     *
     * @return a copy of the <code>triangle</code>.
     */
    public final long[][] getTriangle() {
        long[][] result = new long[triangle.length][];
        int i = 0;
        for (long[] row : triangle) {
            long[] newRow = new long[row.length];
            System.arraycopy(row, 0, newRow, 0, row.length);
            result[i++] = newRow;
        }
        return result;
    }

    @Override
    public final BellTriangle clone() {
        return new BellTriangle(getSize());
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof BellTriangle && ((BellTriangle) o).getSize() == getSize();
    }

    @Override
    public final int hashCode() {
        return getSize();
    }

}
