/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRange.java is part of Strata
 * Last modified on 24-09-2021 02:21 p.m.
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


import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import org.jetbrains.annotations.Nullable;


public final class VersionRange {
    @Nullable
    private final Version startVersion;
    
    private final boolean startInclusive;
    
    @Nullable
    private final Version endVersion;
    
    private final boolean endInclusive;
    
    public VersionRange(@Nullable Version startVersion, boolean startInclusive,
                        @Nullable Version endVersion, boolean endInclusive) {
        this.startVersion = startVersion;
        this.startInclusive = startInclusive;
        this.endVersion = endVersion;
        this.endInclusive = endInclusive;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (startInclusive)
            sb.append('[');
        else
            sb.append('(');
        
        if (startVersion != null)
            sb.append(startVersion);
        
        sb.append(",");
        
        if (endVersion != null)
            sb.append(endVersion);
        
        return sb.toString();
    }
    
    @Nullable
    public Version getStartVersion() {
        return startVersion;
    }
    
    public boolean isStartInclusive() {
        return startInclusive;
    }
    
    @Nullable
    public Version getEndVersion() {
        return endVersion;
    }
    
    public boolean isEndInclusive() {
        return endInclusive;
    }
    
    public boolean isSatisfiedBy(String version) throws ParseException {
        return this.isSatisfiedBy(Versions.parseVersion(version));
    }
    
    public boolean isSatisfiedBy(Version version) {
        if (startVersion != null) {
            if (startInclusive) {
                if (0 < startVersion.compareTo(version))
                    return false;
            } else {
                if (0 <= startVersion.compareTo(version))
                    return false;
            }
        }
        if (endVersion != null) {
            if (endInclusive) {
                return 0 <= endVersion.compareTo(version);
            } else {
                return 0 < endVersion.compareTo(version);
            }
        }
        
        return true;
    }
}
