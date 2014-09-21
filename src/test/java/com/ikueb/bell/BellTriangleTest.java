package com.ikueb.bell;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit testing for {@link BellTriangle}.
 */
public class BellTriangleTest {

    private static final int SIZE = 5;
    private static final BellTriangle TEST = new BellTriangle(SIZE);
    private static final Logger log = LoggerFactory.getLogger(BellTriangleTest.class);
    private static final String STANDARD = "test-cases";
    private static final String EXCEPTIONS = "exception-test-cases";

    private static enum TestCase {
        ZERO(0, 1), ONE(1, 1), TWO(2, 2), THREE(3, 5), FOUR(4, 15), FIVE(5, 52), SIX(6, 203);

        private final Integer index;
        private final Long result;

        TestCase(int index, long result) {
            this.index = Integer.valueOf(index);
            this.result = Long.valueOf(result);
        }
    }

    private static enum ExceptionTestCase {
        NEG_ROW(-1, 0), NEG_FIELD(0, -1), BAD_ROW(SIZE + 1, 0), BAD_FIELD(0, SIZE + 1);

        private final Integer row;
        private final Integer field;

        ExceptionTestCase(int row, int field) {
            this.row = Integer.valueOf(row);
            this.field = Integer.valueOf(field);
        }
    }

    /**
     * Displays a {@link BellTriangle} using a left-aligned format.
     *
     * @param bellTriangle the {@link BellTriangle} to display.
     */
    private static final void displayTriangle(final BellTriangle bellTriangle) {
        Arrays.stream(bellTriangle.getTriangle())
                .map((row) -> Arrays.stream(row).boxed().map(Object::toString)
                        .collect(Collectors.joining(", "))).forEach(log::debug);
    }

    @DataProvider(name = STANDARD)
    public Iterator<Object[]> getTestCases() {
        return Stream.of(TestCase.values())
                .map((current) -> new Object[] { current.index, current.result }).iterator();
    }

    @Test(dataProvider = STANDARD)
    public void testGetSizeResultAndValue(Integer index, Long expected) {
        final BellTriangle bellTriangle = new BellTriangle(index.intValue());
        assertThat(Integer.valueOf(bellTriangle.getSize()), equalTo(index));
        assertThat(Long.valueOf(bellTriangle.getBellNumber()), equalTo(expected));
        assertThat(Long.valueOf(bellTriangle.getValue(index.intValue(), 0)), equalTo(expected));
        displayTriangle(bellTriangle);
    }

    @DataProvider(name = EXCEPTIONS)
    public Iterator<Object[]> getExceptionTestCases() {
        return Stream.of(ExceptionTestCase.values())
                .map((current) -> new Object[] { current.row, current.field }).iterator();
    }

    @Test(dataProvider = EXCEPTIONS, expectedExceptions = IllegalArgumentException.class)
    public void testExceptions(Integer row, Integer field) {
        TEST.getValue(row.intValue(), field.intValue());
    }

    @Test
    public void testGetTriangle() {
        assertThat("Matching triangles", Arrays.deepEquals(TEST.getTriangle(), TEST.getTriangle()));
        assertThat("Different triangles' references", TEST.getTriangle() != TEST.getTriangle());
    }

    @Test
    public void testTriangles() {
        assertThat("Matching objects", Objects.equals(TEST, TEST.clone()));
        assertThat("Matching objects' hash codes", TEST.hashCode() == TEST.clone().hashCode());
        assertThat("Different objects' references", TEST != TEST.clone());
    }

    public static void main(String[] args) {
        System.out.println("Enter integers between 0 and " + BellTriangle.LIMIT
                + " inclusive, any other inputs to exit.");
        try (Scanner scanner = new Scanner(System.in)) {
            int input = -1;
            while (scanner.hasNextInt() && (input = scanner.nextInt()) >= 0) {
                displayTriangle(new BellTriangle(input));
            }
        }
    }

}
