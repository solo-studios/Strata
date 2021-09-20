/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CoreVersion.java is part of Strata
 * Last modified on 20-09-2021 05:52 p.m.
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

package com.solostudios.strata.version;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;


public final class CoreVersion implements Comparable<CoreVersion>, Formattable {
    @NotNull
    private final BigInteger major;
    
    @NotNull
    private final BigInteger minor;
    
    @NotNull
    private final BigInteger patch;
    
    public CoreVersion(@NotNull BigInteger major, @NotNull BigInteger minor, @NotNull BigInteger patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    @Override
    public int compareTo(@NotNull CoreVersion o) {
        int majorComparison = major.compareTo(o.major);
        int minorComparison = minor.compareTo(o.minor);
        return (majorComparison != 0) ? majorComparison : ((minorComparison != 0) ? minorComparison : patch.compareTo(o.patch));
    }
    
    @Override
    public String toString() {
        return String.format("NormalVersion{major=%d, minor=%d, patch=%d}", major, minor, patch);
    }
    
    @NotNull
    @Contract(pure = true)
    public BigInteger getMajor() {
        return major;
    }
    
    @NotNull
    @Contract(pure = true)
    public BigInteger getMinor() {
        return minor;
    }
    
    @NotNull
    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s", major, minor, patch);
    }
    
    @NotNull
    @Contract(pure = true)
    public BigInteger getPatch() {
        return patch;
    }
    
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        CoreVersion coreVersion = (CoreVersion) o;
        
        if (major.compareTo(coreVersion.major) != 0)
            return false;
        if (minor.compareTo(coreVersion.minor) != 0)
            return false;
        return patch.compareTo(coreVersion.patch) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = major.hashCode();
        result = 31 * result + minor.hashCode();
        result = 31 * result + patch.hashCode();
        return result;
    }
}
