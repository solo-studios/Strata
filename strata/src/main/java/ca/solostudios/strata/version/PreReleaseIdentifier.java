/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PreReleaseIdentifier.java is part of Strata
 * Last modified on 23-02-2022 12:23 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * STRATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.solostudios.strata.version;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;


/**
 * This class represents a pre-release identifier.
 * There are only two implementations: {@link NumericalPreReleaseIdentifier} and {@link AlphaNumericalPreReleaseIdentifier}.
 * <p>
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 * @author solonovamax
 */
public abstract /* sealed */ class PreReleaseIdentifier implements Comparable<PreReleaseIdentifier>, Formattable
        /* permits PreReleaseIdentifier.NumericalPreReleaseIdentifier,
                PreReleaseIdentifier.AlphaNumericalPreReleaseIdentifier */ {

    @Override
    public int compareTo(@NotNull PreReleaseIdentifier o) {
        if (isNumeric())
            if (o.isNumeric())
                return asInteger().compareTo(o.asInteger());
            else
                return -1;
        else if (o.isNumeric())
            return 1;
        else
            return asString().compareTo(o.asString());
    }

    /**
     * This identifier as an integer.
     *
     * @return This identifier as an integer.
     * @throws UnsupportedOperationException If this identifier cannot be formatted as an integer.
     */
    @NotNull
    @Contract(pure = true)
    protected BigInteger asInteger() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Numerical values are not supported by this implementation");
    }

    /**
     * This identifier formatted as a string.
     *
     * @return This identifier as a string.
     */
    @NotNull
    @Contract(pure = true)
    protected abstract String asString();

    @NotNull
    @Override
    @Contract(pure = true)
    public String getFormatted() {
        return asString();
    }

    /**
     * Whether or not this identifier can contain numerical values.
     *
     * @return {@code true} if this identifier can contain numerical values, {@code false} otherwise.
     */
    @Contract(pure = true)
    protected abstract boolean isNumeric();

    /**
     * A numerical identifier. This identifier can only contain a positive number.
     */
    public static final class NumericalPreReleaseIdentifier extends PreReleaseIdentifier {
        private final BigInteger value;

        /**
         * Constructs a new numerical identifier with the provided value.
         *
         * @param value The value of this identifier.
         */
        public NumericalPreReleaseIdentifier(BigInteger value) {
            this.value = value;
        }

        @NotNull
        @Override
        @Contract(pure = true)
        protected BigInteger asInteger() {
            return this.value;
        }

        @NotNull
        @Override
        @Contract(pure = true)
        protected String asString() {
            return this.value.toString();
        }

        @Override
        @Contract(pure = true)
        protected boolean isNumeric() {
            return true;
        }

        @Override
        @Contract(pure = true)
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        @Contract(value = "null -> false", pure = true)
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NumericalPreReleaseIdentifier that = (NumericalPreReleaseIdentifier) o;

            return Objects.equals(this.value, that.value);
        }

        @NotNull
        @Override
        @Contract(pure = true)
        public String toString() {
            return String.format("NumericalPreReleaseIdentifier{value=%d}", this.value);
        }
    }


    /**
     * An alphanumerical identifier. This identifier can only contain alpha-numeric values.
     */
    public static final class AlphaNumericalPreReleaseIdentifier extends PreReleaseIdentifier {
        @NotNull
        private final String value;

        /**
         * Constructs a new alphanumerical identifier with the provided value.
         *
         * @param value The value of this identifier.
         */
        public AlphaNumericalPreReleaseIdentifier(@NotNull String value) {
            this.value = value;
        }

        @NotNull
        @Override
        @Contract(pure = true)
        protected String asString() {
            return this.value;
        }

        @Override
        @Contract(pure = true)
        protected boolean isNumeric() {
            return false;
        }

        @Override
        @Contract(pure = true)
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        @Contract(value = "null -> false", pure = true)
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AlphaNumericalPreReleaseIdentifier that = (AlphaNumericalPreReleaseIdentifier) o;

            return this.value.equals(that.value);
        }

        @NotNull
        @Override
        @Contract(pure = true)
        public String toString() {
            return String.format("AlphaNumericalPreReleaseIdentifier{value='%s'}", this.value);
        }
    }
}
