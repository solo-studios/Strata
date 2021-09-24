/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PreReleaseIdentifier.java is part of Strata
 * Last modified on 24-09-2021 07:49 p.m.
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


public abstract class PreReleaseIdentifier implements Comparable<PreReleaseIdentifier>, Formattable {
    
    @Override
    public int compareTo(@NotNull PreReleaseIdentifier o) {
        if (isNumeric())
            if (o.isNumeric())
                return Integer.compare(asInt(), o.asInt());
            else
                return -1;
        else if (o.isNumeric())
            return 1;
        else
            return asString().compareTo(o.asString());
    }
    
    protected int asInt() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Numerical values are not supported by this implementation");
    }
    
    @NotNull
    protected abstract String asString();
    
    @NotNull
    @Override
    public String getFormatted() {
        return asString();
    }
    
    protected abstract boolean isNumeric();
    
    public static final class NumericalPreReleaseIdentifier extends PreReleaseIdentifier {
        private final int value;
    
        public NumericalPreReleaseIdentifier(int value) {
            this.value = value;
        }
    
        @Override
        public String toString() {
            return String.format("NumericalPreReleaseIdentifier{value=%d}", value);
        }
    
        @NotNull
        @Override
        protected String asString() {
            return Integer.toString(value);
        }
    
        @Override
        protected int asInt() {
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
            
            return value == that.value;
        }
        
        @Override
        public int hashCode() {
            return value;
        }
    }
    
    
    public static final class AlphaNumericalPreReleaseIdentifier extends PreReleaseIdentifier {
        @NotNull
        private final String value;
        
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
