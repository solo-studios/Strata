/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionParser.java is part of Strata
 * Last modified on 23-07-2021 11:09 p.m.
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

package com.solostudios.strata.parser;


import com.solostudios.strata.parser.tokenizer.Char;
import com.solostudios.strata.parser.tokenizer.LookaheadReader;
import com.solostudios.strata.parser.tokenizer.ParseException;
import com.solostudios.strata.version.BuildMetadata;
import com.solostudios.strata.version.CoreVersion;
import com.solostudios.strata.version.PreRelease;
import com.solostudios.strata.version.PreReleaseIdentifier;
import com.solostudios.strata.version.Version;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public final class VersionParser {
    private static final char PLUS = '+';
    
    private static final char DOT = '.';
    
    private static final char DASH = '-';
    
    private final LookaheadReader input;
    
    private final String versionString;
    
    public VersionParser(String versionString) {
        this.input = new LookaheadReader(new StringReader(versionString));
        this.versionString = versionString;
    }
    
    public Version parse() throws ParseException {
        CoreVersion coreVersion = parseCoreVersion();
        PreRelease preRelease = PreRelease.NULL;
        BuildMetadata buildMetadata = BuildMetadata.NULL;
        
        Char next = input.consume();
    
        if (next.is(DASH)) {
            preRelease = parsePreRelease();
        
            next = input.consume();
        }
        
        if (next.is(PLUS)) {
            buildMetadata = parseBuildMetadata();
            
            next = input.consume();
        }
        
        if (next.isEndOfInput())
            return new Version(coreVersion, preRelease, buildMetadata);
        else
            throw new ParseException("Expected end of version. Illegal character found.", versionString, next);
    }
    
    private CoreVersion parseCoreVersion() throws ParseException {
        BigInteger major = new BigInteger(consumeNumber());
        consumeCharacter(DOT);
        BigInteger minor = new BigInteger(consumeNumber());
        consumeCharacter(DOT);
        BigInteger patch = new BigInteger(consumeNumber());
        return new CoreVersion(major, minor, patch);
    }
    
    private PreRelease parsePreRelease() throws ParseException {
        List<PreReleaseIdentifier> identifiers = new ArrayList<>();
        
        identifiers.add(parsePreReleaseIdentifier());
        
        while (input.current().is('.')) {
            input.consume();
            
            identifiers.add(parsePreReleaseIdentifier());
        }
        
        return new PreRelease(identifiers);
    }
    
    private PreReleaseIdentifier parsePreReleaseIdentifier() throws ParseException {
        if (lookaheadAlphaNumeric())
            return new PreReleaseIdentifier.AlphaNumericalPreReleaseIdentifier(consumeAlphaNumeric());
        else
            return new PreReleaseIdentifier.NumericalPreReleaseIdentifier(Integer.parseInt(consumeNumber()));
    }
    
    private PreReleaseIdentifier.AlphaNumericalPreReleaseIdentifier parseAlphaNumericPreReleaseIdentifier() throws ParseException {
        return new PreReleaseIdentifier.AlphaNumericalPreReleaseIdentifier(consumeAlphaNumeric());
    }
    
    private BuildMetadata parseBuildMetadata() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!(input.current().isAlphaNumeric()))
            throw new ParseException("Alpha-Numeric identifier expected.", versionString, input.current());
        
        do {
            Char consumed = input.consume();
            if (consumed.is(DOT)) {
                if (input.current().is(DOT))
                    throw new ParseException("Alpha-Numeric identifier expected, but found period.", versionString, input.current());
                if (input.current().isEndOfInput())
                    throw new ParseException("Alpha-Numeric identifier expected, but found end of input.", versionString, input.current());
            }
            
            sb.append(consumed.getValue());
        } while (input.current().isAlphaNumeric() || input.current().is(DOT));
        
        return new BuildMetadata(sb.toString());
    }
    
    private String consumeNumber() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!input.current().isDigit())
            throw new ParseException("Numeric identifier expected.", versionString, input.current());
        
        if (input.current().is('0') && input.next().isDigit())
            throw new ParseException("Numeric identifier must not contain leading zeros.", versionString, input.current());
        
        do {
            sb.append(input.consume().getValue());
        } while (input.current().isDigit());
        
        return sb.toString();
    }
    
    private boolean lookaheadAlphaNumeric() throws ParseException {
        boolean foundNonDigit = false;
    
        for (int i = 0; !foundNonDigit; i++) {
            foundNonDigit = input.next(i).isLetter() || input.next(i).is(DASH);
        
            if (!input.next(i).isAlphaNumeric())
                return foundNonDigit;
        }
    
        return true;
    }
    
    private String consumeAlphaNumeric() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!(input.current().isAlphaNumeric()))
            throw new ParseException("Alpha-Numeric identifier expected.", versionString, input.current());
        
        do {
            sb.append(input.consume().getValue());
        } while (input.current().isAlphaNumeric());
        
        return sb.toString();
    }
    
    private Char consumeCharacter(char expected) throws ParseException {
        if (input.current().is(expected))
            return input.consume();
        else
            throw new ParseException(String.format("Illegal character. Character '%s' expected.", expected),
                                     versionString, input.current());
    }
}