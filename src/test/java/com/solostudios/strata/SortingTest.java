/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file SortingTest.java is part of Strata
 * Last modified on 17-07-2021 09:32 p.m.
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

package com.solostudios.strata;


import com.solostudios.strata.version.Version;

import java.util.Comparator;


public class SortingTest {
    Comparator<Version> versionComparator = Comparator.naturalOrder();

//    @Test
//    void testMajor() {
//        List<Version> unsorted = Arrays.asList(new Version(3, 0, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 0, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(2, 0, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        List<Version> sorted = Arrays.asList(new Version(1, 0, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(2, 0, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(3, 0, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        unsorted.sort(versionComparator);
//
//        assertTrue(CollectionUtils.isEqualCollection(unsorted, sorted), "Major versions not sorted property.");
//    }
//
//    @Test
//    void testMajorMinor() {
//        List<Version> unsorted = Arrays.asList(new Version(3, 1, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 5, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(2, 3, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        List<Version> sorted = Arrays.asList(new Version(1, 5, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(2, 3, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(3, 1, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        unsorted.sort(versionComparator);
//
//        assertTrue(CollectionUtils.isEqualCollection(unsorted, sorted), "Major and minor versions not sorted property.");
//    }
//
//    @Test
//    void testMinor() {
//        List<Version> unsorted = Arrays.asList(new Version(1, 2, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 3, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 1, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        List<Version> sorted = Arrays.asList(new Version(1, 1, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 2, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 3, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        unsorted.sort(versionComparator);
//
//        assertTrue(CollectionUtils.isEqualCollection(unsorted, sorted), "Minor versions not sorted property.");
//    }
//
//    @Test
//    void testMinorPatch() {
//        List<Version> unsorted = Arrays.asList(new Version(1, 2, 5, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 3, 0, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 1, 3, PreRelease.NULL, BuildMetadata.NULL));
//
//        List<Version> sorted = Arrays.asList(new Version(1, 1, 3, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 2, 5, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 3, 0, PreRelease.NULL, BuildMetadata.NULL));
//
//        unsorted.sort(versionComparator);
//
//        assertTrue(CollectionUtils.isEqualCollection(unsorted, sorted), "Minor and patch versions not sorted property.");
//    }
//
//    @Test
//    void testPatch() {
//        List<Version> unsorted = Arrays.asList(new Version(1, 0, 3, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 0, 2, PreRelease.NULL, BuildMetadata.NULL),
//                                               new Version(1, 0, 1, PreRelease.NULL, BuildMetadata.NULL));
//
//        List<Version> sorted = Arrays.asList(new Version(1, 0, 1, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 0, 2, PreRelease.NULL, BuildMetadata.NULL),
//                                             new Version(1, 0, 3, PreRelease.NULL, BuildMetadata.NULL));
//
//        unsorted.sort(versionComparator);
//
//        assertTrue(CollectionUtils.isEqualCollection(unsorted, sorted), "Patch versions not sorted property.");
//    }
}
