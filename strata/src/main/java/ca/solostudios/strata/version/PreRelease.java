/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PreRelease.java is part of Strata
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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * A class representing the pre-release data for a {@link Version}.
 *
 * @author solonovamax
 * @see PreReleaseIdentifier
 */
public final class PreRelease implements Comparable<PreRelease>, Formattable {
    /**
     * An empty pre-release instance used it no pre-release is provided.
     */
    public static final PreRelease NULL = new PreRelease(Collections.emptyList());
    
    @NotNull
    private final List<PreReleaseIdentifier> identifiers;
    
    /**
     * Constructs a new pre-release with the provided identifiers.
     *
     * @param identifiers The identifiers for this pre-release.
     */
    public PreRelease(@NotNull List<PreReleaseIdentifier> identifiers) {
        this.identifiers = identifiers;
    }
    
    @Override
    public int compareTo(@NotNull PreRelease o) {
        if (identifiers.isEmpty()) {
            if (o.identifiers.isEmpty())
                return 0;
            else
                return 1;
        } else if (o.identifiers.isEmpty()) {
            return -1;
        }
        
        int i = 0;
        int comparison;
        do {
            if (identifiers.size() <= i)
                if (o.identifiers.size() <= i)
                    return 0;
                else
                    return -1;
            else if (o.identifiers.size() <= i)
                return 1;
            
            comparison = identifiers.get(i).compareTo(o.identifiers.get(i));
            i++;
        } while (comparison == 0);
        return comparison;
    }
    
    @Override
    public String toString() {
        return String.format("PreRelease{identifiers=%s}", identifiers);
    }
    
    /**
     * The internal identifiers
     *
     * @return The internal identifies of this pre-release
     */
    public List<PreReleaseIdentifier> getIdentifiers() {
        return Collections.unmodifiableList(identifiers);
    }
    
    @NotNull
    @Override
    public String getFormatted() {
        if (identifiers.isEmpty()) {
            return "";
        } else {
            Iterator<PreReleaseIdentifier> iterator = identifiers.listIterator();
            StringBuilder builder = new StringBuilder();
            
            if (iterator.hasNext()) {
                builder.append('-').append(iterator.next().getFormatted());
            }
            
            while (iterator.hasNext()) {
                builder.append('.').append(iterator.next().getFormatted());
            }
            
            return builder.toString();
        }
    }
    
    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        PreRelease that = (PreRelease) o;
        
        return identifiers.equals(that.identifiers);
    }
    
    @Override
    public int hashCode() {
        return identifiers.hashCode();
    }
}
