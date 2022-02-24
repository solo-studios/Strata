/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionPerformanceTest.java is part of Strata
 * Last modified on 24-02-2022 12:11 p.m.
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

package ca.solostudios.strata;


import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import static ca.solostudios.strata.Versions.parseVersion;
import static ca.solostudios.strata.Versions.parseVersionRange;


@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 5, batchSize = 1)
@Fork(2)
public class VersionPerformanceTest {
    private static final int ITERATIONS = 1000;
    
    @Benchmark
    @OperationsPerInvocation(ITERATIONS)
    public void testVersionParseSpeed(Blackhole blackhole) throws ParseException {
        for (int i = 0; i < ITERATIONS; i++) {
            Version version = parseVersion("1.2.3");
            blackhole.consume(version);
        }
    }
    
    @Benchmark
    @OperationsPerInvocation(ITERATIONS)
    public void testVersionBoundedRangeParseSpeed(Blackhole blackhole) throws ParseException {
        for (int i = 0; i < ITERATIONS; i++) {
            VersionRange range = parseVersionRange("[1.2.3,4.5.6]");
            blackhole.consume(range);
        }
    }
    
    @Benchmark
    @OperationsPerInvocation(ITERATIONS)
    public void testVersionGlobRangeParseSpeed(Blackhole blackhole) throws ParseException {
        for (int i = 0; i < ITERATIONS; i++) {
            VersionRange range = parseVersionRange("1.2.+");
            blackhole.consume(range);
        }
    }
}
