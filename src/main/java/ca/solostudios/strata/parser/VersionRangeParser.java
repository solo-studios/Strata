/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRangeParser.java is part of Strata
 * Last modified on 24-09-2021 10:32 p.m.
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.StringReader;
import java.math.BigInteger;


/**
 * A parser for strings representing a valid version range.
 * Constructed with the string to parse, {@link VersionRangeParser#parse()} must be invoked to parse the version range.
 * This method returns the parsed version range.
 *
 * <p><br><br>
 * Version ranges must match the following specification:
 * <h1>Version Ranges</h1>
 * Ranges must fit into one of the two categories
 * <ul>
 *     <li>Glob ranges</li>
 *     <li>Version ranges</li>
 * </ul>
 *
 * <h2>Glob Ranges</h2>
 * Glob ranges are represented as follows:
 * <ol>
 *     <li>{@code +}:<br>This will match <i>any</i> version.</li>
 *     <li>
 *         {@code major.+}<br>This matches any version which begins with {@code major.},
 *         meaning: any version between {@code major.0.0} inclusively and {@code {major+1}.0.0} exclusively.
 *         <br>
 *         Example: The glob range {@code 1.+} will match {@code 1.0.0}, {@code 1.2.3}, and {@code 1.99.99} but not {@code 2.0.0}</li>
 *     <li>
 *         {@code major.minor.+}<br>This matches any version which begins with {@code major.minor},
 *         meaning: any version between {@code major.minor.0} inclusively and {@code major.{minor+1}.0} exclusively.
 *         <br>
 *         Example: The glob range {@code 1.2.+} will match {@code 1.2.0}, {@code 1.2.3}, and {@code 1.2.99} but not {@code 1.3.0}</li>
 *     <li>
 *         {@code major.minor.patch}<br>This matches <i>only</i>> version {@code major.minor.patch},
 *         <br>
 *         Example: The glob range {@code 1.2.+} will match {@code 1.2.0}, {@code 1.2.3}, and {@code 1.2.99} but not {@code 1.3.0}
 *     </li>
 * </ol>
 *
 * <h2>Version Ranges</h2>
 * Version ranges are represented by 2 versions surrounded by either brackets ("{@code []}") or braces ("{@code ()}").
 * <p>
 * They must fit the following format:
 *
 * <pre><code>
 *      ("[" or "(")VersionOne,VersionTwo("]" or ")")
 * </code></pre>
 * Where {@code VersionOne} and {@code VersionTwo} are valid {@link Version}s.
 * <p>
 * Note: Both {@code VersionOne} and {@code VersionTwo} are <i>optional</i>.
 * <p>
 * Here is a list of example versions and what they match
 *
 * <table summary="">
 * <thead>
 *   <tr>
 *     <th>Version</th>
 *     <th>Description<br></th>
 *   </tr>
 * </thead>
 * <tbody>
 *   <tr>
 *     <td>[1.0.0,2.0.0]</td>
 *     <td>all versions greater or equal to 1.0.0 and lower or equal to 2.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(1.0.0,2.0.0)</td>
 *     <td>all versions greater or equal to 1.0.0 and lower than 2.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(1.0.0,2.0.0]</td>
 *     <td>all versions greater than 1.0.0 and lower or equal to 2.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(1.0,2.0)</td>
 *     <td>all versions greater than 1.0.0 and lower than 2.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>[1.0.0,)</td>
 *     <td>all versions greater or equal to 1.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(1.0.0,)</td>
 *     <td>all versions greater than 1.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(,2.0.0]</td>
 *     <td>all versions lower or equal to 2.0.0</td>
 *   </tr>
 *   <tr>
 *     <td>(,2.0)</td>
 *     <td>all versions lower than 2.0.0</td>
 *   </tr>
 * </tbody>
 * </table>
 *
 * @author solonovamax
 * @see VersionParser
 */
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
    
    /**
     * Constructs a new version range parser with the provided string to parse.
     *
     * @param versionRangeString The version range string to parse
     */
    public VersionRangeParser(String versionRangeString) {
        this.input = new LookaheadReader(new StringReader(versionRangeString));
        this.versionRangeString = versionRangeString;
    }
    
    /**
     * Parses the provided version range string to a {@link VersionRange}.
     *
     * @return The {@link Version} parsed from the string this object was instantated with.
     *
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.
     */
    @NotNull
    @Contract(value = "-> new", pure = true)
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
    
    private void consumeCharacter(char expected) throws ParseException {
        if (input.current().is(expected))
            input.consume();
        else
            throw new ParseException(String.format("Illegal character. Character '%s' expected.", expected),
                                     versionRangeString, input.current());
    }
}
