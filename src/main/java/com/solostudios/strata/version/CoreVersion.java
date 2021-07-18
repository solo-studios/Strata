/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CoreVersion.java is part of Strata
 * Last modified on 17-07-2021 10:40 p.m.
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


import org.jetbrains.annotations.NotNull;


public class CoreVersion implements Comparable<CoreVersion>, Formattable {
    private final int major;
    
    private final int minor;
    
    private final int patch;
    
    public CoreVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    @Override
    public int compareTo(@NotNull CoreVersion o) {
        int majorComparison = Integer.compare(major, o.major);
        int minorComparison = Integer.compare(minor, o.minor);
        return (majorComparison != 0) ? majorComparison : ((minorComparison != 0) ? minorComparison : Integer.compare(patch, o.patch));
    }
    
    @Override
    public String toString() {
        return String.format("NormalVersion{major=%d, minor=%d, patch=%d}", major, minor, patch);
    }
    
    public int getMajor() {
        return major;
    }
    
    public int getMinor() {
        return minor;
    }
    
    public int getPatch() {
        return patch;
    }
    
    @NotNull
    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s", major, minor, patch);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
    
        CoreVersion that = (CoreVersion) o;
        
        if (major != that.major) return false;
        if (minor != that.minor) return false;
        return patch == that.patch;
    }
    
    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        return result;
    }
}
