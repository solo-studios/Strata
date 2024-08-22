/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionRangeParser.java is part of Strata
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

package ca.solostudios.strata.parser;


import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.Char;
import ca.solostudios.strata.parser.tokenizer.LookaheadReader;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;
import java.math.BigInteger;


/**
 * A parser for strings representing a valid version range.
 * Constructed with the string to parse, {@link VersionRangeParser#parse()} must be invoked to parse the version range.
 * This method returns the parsed version range.
 *
 * <p><br>
 * Version ranges must match the following specification:
 * <h2>Version Ranges</h2>
 * Ranges must fit into one of the two categories
 * <ul>
 *     <li>Version ranges</li>
 *     <li>Version comparison</li>
 *     <li>Version carets</li>
 *     <li>Version globs</li>
 * </ul>
 *
 * <h3>Version Ranges</h3>
 * Version ranges are 2 versions surrounded by either brackets ({@code [} or {@code ]}) or braces ({@code (} or {@code )}).
 * <p>
 * They must fit the following format:
 *
 * <pre><code>
 *      {[,(}[lower version],[upper version]{],)}
 * </code></pre>
 * Where {@code [lower version]} and {@code [upper version]} are valid {@link Version}s.
 *
 * <ul>
 *     <li>
 *         When a bracket is used ({@code [} or {@code ]}), then the respective version is inclusive.
 *         <br>
 *         If a brace is used ({@code (} or {@code )}), then the respective version is exclusive.
 *     </li>
 *     <li>
 *         When a version is omitted, then the range is considered boundless on that end.
 *         <br>
 *         If the lower version is omitted, then it matches any version below the upper bound.
 *         If the upper version is omitted, then it matches any version above the lower bound.
 *         eg. If both are omitted, then all versions will match.
 *     </li>
 * </ul>
 *
 * <h3>Version comparisons</h3>
 * Version comparisons are a comparison operator, followed by a version.
 * <p>
 * They must fit the following format:
 *
 * <pre><code>
 *      [operator][version]
 * </code></pre>
 * Where {@code [version]} is a valid {@link Version} and {@code [operator]} is a valid comparison operator.
 * <p>
 * A list of comparison operators are:
 * <ol>
 *     <li>{@code >}:<br>Greater than. This matches any version &gt; the proceeding version.</li>
 *     <li>{@code >=}:<br>Greater than or equal to. This matches any version &ge; the proceeding version.</li>
 *     <li>{@code <}:<br>Less than. This matches any version &lt; the proceeding version, excluding itself.</li>
 *     <li>{@code <=}:<br>Less than or equal to. This matches any version &le; the proceeding version.</li>
 * </ol>
 *
 * <h3>Version Carets</h3>
 * Version carets are a caret followed by a version.
 * <p>
 * They must fit the following format:
 *
 * <pre><code>
 *      ^[version]
 * </code></pre>
 * Where {@code [version]} is a valid {@link Version}.
 * <p>
 * They match all versions that are greater than itself but less than one plus the first non-zero number.
 * <p>
 * Example:
 * The caret range {@code ^1.2.3} will match {@code 1.2.3}, {@code 1.2.4}, and {@code 1.3.0} but not {@code 2.0.0} or {@code 1.2.2}.
 * <br>
 * The caret range {@code ^0.1.2} will match {@code 0.1.3}, but not {@code 0.2.0} or {@code 0.1.1}.
 *
 * <h3>Version Globs</h3>
 * Glob ranges are a partial version as well as a glob.
 * <p>
 * They must fit the following format:
 * <ol>
 *     <li>{@code +} or {@code *}:<br>This will match <i>any</i> version.</li>
 *     <li>
 *         {@code [major].+}<br>This matches any version which begins with {@code major.},
 *         meaning: any version between {@code major.0.0} inclusively and {@code [major+1].0.0} exclusively.
 *         <br>
 *         Example: The glob range {@code 1.+} will match {@code 1.0.0}, {@code 1.2.3}, and {@code 1.99.99} but not {@code 2.0.0}</li>
 *     <li>
 *         {@code [major].[minor].+}<br>This matches any version which begins with {@code major.minor},
 *         meaning: any version between {@code major.minor.0} inclusively and {@code major.[minor+1].0} exclusively.
 *         <br>
 *         Example: The glob range {@code 1.2.+} will match {@code 1.2.0}, {@code 1.2.3}, and {@code 1.2.99} but not {@code 1.3.0}</li>
 *     <li>
 *         {@code [major].[minor].[patch]}<br>This matches <i>only</i> version {@code major.minor.patch},
 *         <br>
 *         Example: The glob range {@code 1.2.+} will match {@code 1.2.0}, {@code 1.2.3}, and {@code 1.2.99} but not {@code 1.3.0}.
 *         <br>
 *         This means that all versions are valid version ranges.
 *     </li>
 * </ol>
 *
 * <p>
 * Here is a list of example version ranges and what they match
 *
 * <table>
 *     <caption>Table of version examples</caption>
 * <thead>
 *   <tr>
 *     <th>Version Range</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *   </tr>
 * </thead>
 * <tbody>
 *   <tr>
 *     <td>{@code [1.0.0,2.0.0]}</td>
 *     <td>Range</td>
 *     <td>all versions &ge; to {@code 1.0.0} and &le; {@code 2.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code (1.0.0,2.0.0)}</td>
 *     <td>Range</td>
 *     <td>all versions &ge; to {@code 1.0.0} and &lt; {@code 2.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code (1.0.0,2.0.0]}</td>
 *     <td>Range</td>
 *     <td>all versions &gt; {@code 1.0.0} and &le; {@code 2.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code [1.0.0,)}</td>
 *     <td>Range</td>
 *     <td>all versions &ge; {@code 1.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code >=1.0.0}</td>
 *     <td>Comparison</td>
 *     <td>All versions &gt; {@code 1.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code <1.0.0}</td>
 *     <td>Comparison</td>
 *     <td>All versions &lt; {@code 1.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ^1.2.3}</td>
 *     <td>Caret</td>
 *     <td>All versions &ge; {@code 1.2.3} and &lt; {@code 2.0.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code ^0.1.2}</td>
 *     <td>Caret</td>
 *     <td>All versions &ge; {@code 0.1.2} and &lt; {@code 0.2.0}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code *}</td>
 *     <td>Glob</td>
 *     <td>All versions</td>
 *   </tr>
 *   <tr>
 *     <td>{@code 1.2.+}</td>
 *     <td>Glob</td>
 *     <td>All versions &ge; {@code 1.2.0}</td>
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
    private static final char STAR = '*';
    private static final char CARET = '^';
    private static final char GREATER_THAN = '>';
    private static final char LESS_THAN = '<';
    private static final char EQUALS = '=';

    private final LookaheadReader input;

    private final String versionRangeString;

    /**
     * Constructs a new version range parser with the provided string to parse.
     *
     * @param versionRangeString The version range string to parse
     */
    @Contract(pure = true)
    public VersionRangeParser(String versionRangeString) {
        this.input = new LookaheadReader(new StringReader(versionRangeString));
        this.versionRangeString = versionRangeString;
    }

    /**
     * Parses the provided version range string to a {@link VersionRange}.
     *
     * @return The {@link Version} parsed from the string this object was instantated with.
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.
     */
    @NotNull
    @Contract(value = "-> new", pure = true)
    public VersionRange parse() throws ParseException {
        if (this.input.current().is(OPEN_BRACKET, OPEN_PAREN))
            return parseVersionRange();
        else if (this.input.current().is(GREATER_THAN, LESS_THAN))
            return parseVersionComparison();
        else if (this.input.current().is(CARET))
            return parseVersionCaret();
        else
            return parseVersionGlob();
    }

    @NotNull
    private VersionRange parseVersionRange() throws ParseException {
        boolean startInclusive = this.input.consume().is(OPEN_BRACKET);
        boolean endInclusive;

        Version startVersion = null;
        Version endVersion = null;

        if (!this.input.current().is(COMMA)) {
            startVersion = consumeVersionUntil(COMMA);
        }
        consumeCharacter(COMMA);

        if (this.input.current().is(CLOSE_BRACKET, CLOSE_PAREN)) {
            endInclusive = this.input.consume().is(CLOSE_BRACKET);
        } else {
            endVersion = consumeVersionUntil(CLOSE_BRACKET, CLOSE_PAREN);

            Char next = this.input.consume();

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
                            this.versionRangeString,
                            next
                    );
            }
        }

        consumeEndOfInput();
        return Versions.getVersionRange(startVersion, startInclusive, endVersion, endInclusive);
    }

    @NotNull
    private VersionRange parseVersionComparison() throws ParseException {
        boolean greaterThan = this.input.consume().is(GREATER_THAN);
        boolean inclusive = this.input.current().is(EQUALS);
        if (inclusive)
            consumeCharacter(EQUALS);

        Version version = consumeVersionUntil();
        consumeEndOfInput();

        if (greaterThan) {
            return Versions.getVersionRange(version, inclusive, null, true);
        } else {
            return Versions.getVersionRange(null, true, version, inclusive);
        }
    }

    @NotNull
    private VersionRange parseVersionCaret() throws ParseException {
        consumeCharacter(CARET);

        Version lowVersion;

        lowVersion = consumeVersionUntil();

        Version highVersion = highVersionForCaret(lowVersion);

        consumeEndOfInput();
        return Versions.getVersionRange(lowVersion, true, highVersion, false);
    }

    @NotNull
    private Version highVersionForCaret(Version lowVersion) {
        if (!lowVersion.getMajor().equals(BigInteger.ZERO))
            return Versions.getVersion(lowVersion.getMajor().add(BigInteger.ONE), BigInteger.ZERO, BigInteger.ZERO);
        else if (!lowVersion.getMinor().equals(BigInteger.ZERO))
            return Versions.getVersion(BigInteger.ZERO, lowVersion.getMinor().add(BigInteger.ONE), BigInteger.ZERO);
        else
            return Versions.getVersion(BigInteger.ZERO, BigInteger.ZERO, lowVersion.getPatch().add(BigInteger.ONE));
    }

    @NotNull
    private VersionRange parseVersionGlob() throws ParseException {
        @Nullable
        Version lowestVersion;
        @Nullable
        Version highestVersion;

        if (this.input.current().is(PLUS) || this.input.current().is(STAR)) {
            this.input.consume();
            highestVersion = null;
            lowestVersion = null;
        } else {
            BigInteger major = new BigInteger(consumeNumber());

            consumeCharacter(DOT);
            if (this.input.current().is(PLUS)) {
                this.input.consume();
                lowestVersion = Versions.getVersion(major, BigInteger.ZERO, BigInteger.ZERO);
                highestVersion = Versions.getVersion(major.add(BigInteger.ONE), BigInteger.ZERO, BigInteger.ZERO);
            } else {
                BigInteger minor = new BigInteger(consumeNumber());

                consumeCharacter(DOT);
                if (this.input.current().is(PLUS)) {
                    this.input.consume();
                    lowestVersion = Versions.getVersion(major, minor, BigInteger.ZERO);
                    highestVersion = Versions.getVersion(major, minor.add(BigInteger.ONE), BigInteger.ZERO);
                } else {
                    BigInteger patch = new BigInteger(consumeNumber());

                    lowestVersion = Versions.getVersion(major, minor, patch);
                    highestVersion = Versions.getVersion(major, minor, patch.add(BigInteger.ONE));
                }
            }
        }

        consumeEndOfInput();
        return Versions.getVersionRange(lowestVersion, true, highestVersion, false);
    }

    @NotNull
    private Version consumeVersionUntil(char... test) {
        StringBuilder sb = new StringBuilder();

        Char positionBefore = this.input.current();
        do {
            sb.append(consumeNotEndOfInput().getValue());
        } while (test.length != 0 ? !this.input.current().is(test) : !this.input.current().isEndOfInput());

        try {
            return Versions.parseVersion(sb.toString());
        } catch (ParseException e) {
            throw new ParseException(e, this.versionRangeString, e.getPosition().increment(positionBefore.getPos()));
        }
    }

    @NotNull
    private String consumeNumber() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!this.input.current().isDigit())
            throw new ParseException("Numeric identifier expected.", this.versionRangeString, this.input.current());

        if (this.input.current().is('0') && this.input.next().isDigit())
            throw new ParseException("Numeric identifier must not contain leading zeros.", this.versionRangeString, this.input.current());

        do {
            sb.append(consumeNotEndOfInput().getValue());
        } while (this.input.current().isDigit());

        return sb.toString();
    }

    private void consumeCharacter(char expected) throws ParseException {
        if (this.input.current().is(expected))
            this.input.consume();
        else
            throw new ParseException(
                    String.format("Illegal character. Character '%s' expected.", expected),
                    this.versionRangeString,
                    this.input.current()
            );
    }

    @NotNull
    private Char consumeNotEndOfInput() {
        if (this.input.current().isEndOfInput())
            throw new ParseException(
                    "Found end of input while parsing version range string.",
                    this.versionRangeString,
                    this.input.current()
            );
        else
            return this.input.consume();
    }

    private void consumeEndOfInput() {
        if (this.input.current().isEndOfInput())
            this.input.consume();
        else
            throw new ParseException("Illegal character. End of input expected.", this.versionRangeString, this.input.current());
    }
}
