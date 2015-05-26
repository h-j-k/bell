/*
 * Copyright 2015 h-j-k. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ikueb.bell;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    enum TestCase {
        ZERO(1), ONE(1), TWO(2), THREE(5), FOUR(15), FIVE(52), SIX(203), SEVEN(877), EIGHT(4140);

        private final Long result;

        TestCase(long result) {
            this.result = Long.valueOf(result);
        }
    }

    enum ExceptionTestCase {
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
                .map(row -> Arrays.stream(row).mapToObj(Long::toString)
                        .collect(Collectors.joining(", "))).forEach(log::debug);
    }

    @DataProvider(name = STANDARD)
    public static Iterator<Object[]> getTestCases() {
        return EnumSet.allOf(TestCase.class).stream()
                .map(v -> new Object[] { Integer.valueOf(v.ordinal()), v.result })
                .iterator();
    }

    @Test(dataProvider = STANDARD)
    public void testGetSizeResultAndValue(Integer index, Long expected) {
        final int size = index.intValue();
        final BellTriangle bellTriangle = new BellTriangle(size);
        assertThat(Integer.valueOf(bellTriangle.getSize()), equalTo(index));
        assertThat(Long.valueOf(bellTriangle.getBellNumber()), equalTo(expected));
        assertThat(Long.valueOf(bellTriangle.getValue(size, 0)), equalTo(expected));
        if (size == 0) {
            displayTriangle(bellTriangle);
            return;
        }
        assertThat(Long.valueOf(bellTriangle.getValue(size - 1, size - 1)), equalTo(expected));
        final long nextValue = (bellTriangle.getValue(size - 1, 0) + bellTriangle.getBellNumber());
        assertThat(Long.valueOf(bellTriangle.getValue(size, 1)), equalTo(Long.valueOf(nextValue)));
        displayTriangle(bellTriangle);
    }

    @DataProvider(name = EXCEPTIONS)
    public static Iterator<Object[]> getExceptionTestCases() {
        return EnumSet.allOf(ExceptionTestCase.class).stream()
                .map(v -> new Object[] { v.row, v.field }).iterator();
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
    public void testCloning() {
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
