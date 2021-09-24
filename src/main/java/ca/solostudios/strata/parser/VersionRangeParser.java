/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRangeParser.java is part of Strata
 * Last modified on 24-09-2021 07:21 p.m.
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

package ca.solostudios.strata.parser;


import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.Char;
import ca.solostudios.strata.parser.tokenizer.LookaheadReader;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import java.io.StringReader;
import java.math.BigInteger;


public class VersionRangeParser {
    private static final char OPEN_PAREN = '(';
    
    private static final char CLOSE_PAREN = ')';
    
    private static final char OPEN_BRACKET = '[';
    
    private static final char CLOSE_BRACKET = ']';
    
    private static final char COMMA = ',';
    
    private static final char PLUS = '+';
    
    private static final char DOT = '.';
    
    private final LookaheadReader input;
    
    private final String versionRangeString;
    
    public VersionRangeParser(String versionRangeString) {
        this.input = new LookaheadReader(new StringReader(versionRangeString));
        this.versionRangeString = versionRangeString;
    }
    
    public VersionRange parse() throws ParseException {
        if (input.current().is(OPEN_BRACKET, OPEN_PAREN))
            return parseVersionRange();
        else
            return parseVersionGlob();
    }
    
    private VersionRange parseVersionRange() throws ParseException {
        boolean startInclusive = input.consume().is(OPEN_BRACKET);
        boolean endInclusive;
        
        Version startVersion = null;
        Version endVersion = null;
        
        if (input.current().is(COMMA)) {
            input.consume();
        } else {
            StringBuilder sb = new StringBuilder();
            
            do {
                Char consumed = input.consume();
                
                if (input.current().isEndOfInput())
                    throw new ParseException("Found end of input while looking for comma in version range string.", versionRangeString,
                                             input.current());
                
                sb.append(consumed.getValue());
            } while (!input.current().is(COMMA));
            
            try {
                startVersion = new VersionParser(sb.toString()).parse();
            } catch (ParseException e) {
                throw new ParseException(e, versionRangeString, e.getPosition());
            }
            
            Char next = input.consume();
            
            if (!next.is(COMMA))
                throw new ParseException("Was expecting comma after version", versionRangeString, next);
        }
        
        if (input.current().is(CLOSE_BRACKET, CLOSE_PAREN)) {
            endInclusive = input.consume().is(CLOSE_BRACKET);
        } else {
            StringBuilder sb = new StringBuilder();
            
            do {
                Char consumed = input.consume();
                
                if (input.current().isEndOfInput())
                    throw new ParseException("Found end of input while looking for comma in version range string.", versionRangeString,
                                             input.current());
                
                sb.append(consumed.getValue());
            } while (!input.current().is(CLOSE_BRACKET, CLOSE_PAREN));
            
            try {
                endVersion = new VersionParser(sb.toString()).parse();
            } catch (ParseException e) {
                throw new ParseException(e, versionRangeString, e.getPosition());
            }
            
            Char next = input.consume();
            
            switch (next.getValue()) {
                case CLOSE_BRACKET:
                    endInclusive = true;
                    break;
                case CLOSE_PAREN:
                    endInclusive = false;
                    break;
                default:
                    throw new ParseException(
                            String.format("Was looking for '%s' or '%s' but couldn't find one", CLOSE_BRACKET, CLOSE_PAREN),
                            versionRangeString,
                            next);
            }
        }
        
        return new VersionRange(startVersion, startInclusive, endVersion, endInclusive);
    }
    
    private VersionRange parseVersionGlob() throws ParseException {
        Version lowestVersion;
        Version highestVersion;
        
        if (input.current().is(PLUS)) {
            highestVersion = null;
            lowestVersion = Versions.getVersion(0, 0, 0);
        } else {
            BigInteger major = new BigInteger(consumeNumber());
            
            consumeCharacter(DOT);
            if (input.current().is(PLUS)) {
                lowestVersion = Versions.getVersion(major, BigInteger.ZERO, BigInteger.ZERO);
                highestVersion = Versions.getVersion(major.add(BigInteger.ONE), BigInteger.ZERO, BigInteger.ZERO);
            } else {
                BigInteger minor = new BigInteger(consumeNumber());
                
                consumeCharacter(DOT);
                if (input.current().is(PLUS)) {
                    lowestVersion = Versions.getVersion(major, minor, BigInteger.ZERO);
                    highestVersion = Versions.getVersion(major, minor.add(BigInteger.ONE), BigInteger.ZERO);
                } else {
                    BigInteger patch = new BigInteger(consumeNumber());
                    
                    lowestVersion = Versions.getVersion(major, minor, patch);
                    highestVersion = Versions.getVersion(major, minor, patch.add(BigInteger.ONE));
                }
            }
        }
        
        return new VersionRange(lowestVersion, true, highestVersion, false);
    }
    
    @SuppressWarnings("DuplicatedCode")
    private String consumeNumber() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!input.current().isDigit())
            throw new ParseException("Numeric identifier expected.", versionRangeString, input.current());
        
        if (input.current().is('0') && input.next().isDigit())
            throw new ParseException("Numeric identifier must not contain leading zeros.", versionRangeString, input.current());
        
        do {
            sb.append(input.consume().getValue());
        } while (input.current().isDigit());
        
        return sb.toString();
    }
    
    private Char consumeCharacter(char expected) throws ParseException {
        if (input.current().is(expected))
            return input.consume();
        else
            throw new ParseException(String.format("Illegal character. Character '%s' expected.", expected),
                                     versionRangeString, input.current());
    }
}
