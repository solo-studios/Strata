/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PreReleaseIdentifier.java is part of Strata
 * Last modified on 24-09-2021 11:32 p.m.
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
     *
     * @throws UnsupportedOperationException If this identifier cannot be formatted as an integer.
     */
    protected BigInteger asInteger() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Numerical values are not supported by this implementation");
    }
    
    /**
     * This identifier formatted as a string.
     *
     * @return This identifier as a string.
     */
    @NotNull
    protected abstract String asString();
    
    @NotNull
    @Override
    public String getFormatted() {
        return asString();
    }
    
    /**
     * Whether or not this identifier can contain numerical values.
     *
     * @return {@code true} if this identifier can contain numerical values, {@code false} otherwise.
     */
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
        
        @Override
        public String toString() {
            return String.format("NumericalPreReleaseIdentifier{value=%d}", value);
        }
        
        @NotNull
        @Override
        protected String asString() {
            return value.toString();
        }
        
        @Override
        protected BigInteger asInteger() {
            return value;
        }
        
        @Override
        protected boolean isNumeric() {
            return true;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            NumericalPreReleaseIdentifier that = (NumericalPreReleaseIdentifier) o;
            
            return Objects.equals(value, that.value);
        }
        
        @Override
        public int hashCode() {
            return value.hashCode();
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
        
        @Override
        public String toString() {
            return String.format("AlphaNumericalPreReleaseIdentifier{value='%s'}", value);
        }
        
        @NotNull
        @Override
        protected String asString() {
            return value;
        }
        
        @Override
        protected boolean isNumeric() {
            return false;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            AlphaNumericalPreReleaseIdentifier that = (AlphaNumericalPreReleaseIdentifier) o;
            
            return value.equals(that.value);
        }
        
        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}
