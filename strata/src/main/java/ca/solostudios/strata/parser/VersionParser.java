/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file VersionParser.java is part of Strata
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


import ca.solostudios.strata.parser.tokenizer.Char;
import ca.solostudios.strata.parser.tokenizer.LookaheadReader;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import ca.solostudios.strata.version.BuildMetadata;
import ca.solostudios.strata.version.CoreVersion;
import ca.solostudios.strata.version.PreRelease;
import ca.solostudios.strata.version.PreReleaseIdentifier;
import ca.solostudios.strata.version.Version;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * A parser for strings representing a valid {@link Version} according to the SemVer spec.
 * <p>
 * Constructed with the string to parse, {@link VersionParser#parse()} must be invoked to parse the version.
 * This method returns the parsed version.
 *
 * <p><br><br>
 * The Semantic Versioning spec is provided below:
 * <h2>Semantic Versioning Specification (SemVer)</h2>
 * The key words “MUST”, “MUST NOT”, “REQUIRED”, “SHALL”, “SHALL NOT”, “SHOULD”, “SHOULD NOT”, “RECOMMENDED”, “MAY”, and “OPTIONAL” in this
 * document are to be interpreted as described in RFC 2119.
 *
 * <ol>
 *     <li>Software using Semantic Versioning MUST declare a public API. This API could be declared in the code itself or exist strictly in
 *     documentation. However it is done, it SHOULD be precise and comprehensive.</li>
 *     <li>A normal version number MUST take the form X.Y.Z where X, Y, and Z are non-negative integers, and MUST NOT contain leading
 *     zeroes. X is the major version, Y is the minor version, and Z is the patch version. Each element MUST increase numerically.
 *     For instance: 1.9.0 -&gt; 1.10.0 -&gt; 1.11.0.</li>
 *     <li>Once a versioned package has been released, the contents of that version MUST NOT be modified. Any modifications MUST be
 *     released as a new version.</li>
 *     <li>Major version zero (0.y.z) is for initial development. Anything MAY change at any time. The public API SHOULD NOT be considered
 *     stable.</li>
 *     <li>Version 1.0.0 defines the public API. The way in which the version number is incremented after this release is dependent on this
 *     public API and how it changes.</li>
 *     <li>Patch version Z (x.y.Z | x &gt; 0) MUST be incremented if only backwards compatible bug fixes are introduced. A bug fix is defined
 *     as an internal change that fixes incorrect behavior.</li>
 *     <li>Minor version Y (x.Y.z | x &gt; 0) MUST be incremented if new, backwards compatible functionality is introduced to the public API.
 *     It MUST be incremented if any public API functionality is marked as deprecated. It MAY be incremented if substantial new
 *     functionality or improvements are introduced within the private code. It MAY include patch level changes. Patch version MUST be
 *     reset to 0 when minor version is incremented.</li>
 *     <li>Major version X (X.y.z | X &gt; 0) MUST be incremented if any backwards incompatible changes are introduced to the public API. It
 *     MAY also include minor and patch level changes. Patch and minor versions MUST be reset to 0 when major version is incremented.</li>
 *     <li>A pre-release version MAY be denoted by appending a hyphen and a series of dot separated identifiers immediately following the
 *     patch version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty.
 *     Numeric identifiers MUST NOT include leading zeroes. Pre-release versions have a lower precedence than the associated normal
 *     version. A pre-release version indicates that the version is unstable and might not satisfy the intended compatibility requirements
 *     as denoted by its associated normal version. Examples: 1.0.0-alpha, 1.0.0-alpha.1, 1.0.0-0.3.7, 1.0.0-x.7.z.92, 1.0.0-x-y-z.–.</li>
 *     <li>Build metadata MAY be denoted by appending a plus sign and a series of dot separated identifiers immediately following the patch
 *     or pre-release version. Identifiers MUST comprise only ASCII alphanumerics and hyphens [0-9A-Za-z-]. Identifiers MUST NOT be empty.
 *     Build metadata MUST be ignored when determining version precedence. Thus two versions that differ only in the build metadata, have
 *     the same precedence. Examples: 1.0.0-alpha+001, 1.0.0+20130313144700, 1.0.0-beta+exp.sha.5114f85, 1.0.0+21AF26D3—-117B344092BD.</li>
 *     <li>
 *         Precedence refers to how versions are compared to each other when ordered.
 *         <ol>
 *             <li>Precedence MUST be calculated by separating the version into major, minor, patch and pre-release identifiers in that
 *             order (Build metadata does not figure into precedence).</li>
 *             <li>
 *                 Precedence is determined by the first difference when comparing each of these identifiers from left to right as follows:
 *                 Major, minor, and patch versions are always compared numerically.
 *                 <br>
 *                 Example: 1.0.0 &lt; 2.0.0 &lt; 2.1.0 &lt; 2.1.1.
 *             </li>
 *             <li>
 *                 When major, minor, and patch are equal, a pre-release version has lower precedence than a normal version:
 *                 <br>
 *                 Example: 1.0.0-alpha &lt; 1.0.0.
 *             </li>
 *             <li>
 *                 Precedence for two pre-release versions with the same major, minor, and patch version MUST be determined by comparing
 *                 each dot separated identifier from left to right until a difference is found as follows:
 *
 *                 <ol>
 *                     <li>Identifiers consisting of only digits are compared numerically.</li>
 *                     <li>Identifiers with letters or hyphens are compared lexically in ASCII sort order.</li>
 *                     <li>Numeric identifiers always have lower precedence than non-numeric identifiers.</li>
 *                     <li>A larger set of pre-release fields has a higher precedence than a smaller set, if all of the preceding
 *                     identifiers are equal.</li>
 *                 </ol>
 *                 Example: 1.0.0-alpha &lt; 1.0.0-alpha.1 &lt; 1.0.0-alpha.beta &lt; 1.0.0-beta &lt; 1.0.0-beta.2 &lt; 1.0.0-beta.11 &lt; 1.0.0-rc.1 &lt; 1.0.0.
 *             </li>
 *         </ol>
 *     </li>
 * </ol>
 *
 * @author solonovamax
 * @see #parse()
 * @see <a href="https://semver.org/">https://semver.org/</a>
 * @see VersionRangeParser
 */
public final class VersionParser {
    private static final char PLUS = '+';

    private static final char DOT = '.';

    private static final char DASH = '-';

    @NotNull
    private final LookaheadReader input;

    @NotNull
    private final String versionString;

    /**
     * Constructs a new version parser with the provided string to parse.
     *
     * @param versionString The version string to parse.
     */
  @Contract(pure = true)
  public VersionParser(@NotNull String versionString) {
        this.input = new LookaheadReader(new StringReader(versionString));
        this.versionString = versionString;
    }

    /**
     * Parses the provided version string to a {@link Version}.
     *
     * @return The {@link Version} parsed from the string this object was instantiated with.
     * @throws ParseException If an exception occurred during the parsing of the version. If taking user input, the message from this
     *                        exception is highly useful and should be returned to the user.
     */
    @NotNull
    @Contract(value = "-> new", pure = true)
    public Version parse() throws ParseException {
        CoreVersion coreVersion = parseCoreVersion();
        PreRelease preRelease = PreRelease.NULL;
        BuildMetadata buildMetadata = BuildMetadata.NULL;

        Char next = this.input.consume();

        if (next.is(DASH)) {
            preRelease = parsePreRelease();

            next = this.input.consume();
        }

        if (next.is(PLUS)) {
            buildMetadata = parseBuildMetadata();

            next = this.input.consume();
        }

        if (next.isEndOfInput())
            return new Version(coreVersion, preRelease, buildMetadata);
        else
            throw new ParseException("Expected end of version. Illegal character found.", this.versionString, next);
    }

    @NotNull
    private CoreVersion parseCoreVersion() throws ParseException {
        BigInteger major = new BigInteger(consumeNumber());
        consumeCharacter(DOT);
        BigInteger minor = new BigInteger(consumeNumber());
        consumeCharacter(DOT);
        BigInteger patch = new BigInteger(consumeNumber());
        return new CoreVersion(major, minor, patch);
    }

    @NotNull
    private PreRelease parsePreRelease() throws ParseException {
        List<PreReleaseIdentifier> identifiers = new ArrayList<>();

        identifiers.add(parsePreReleaseIdentifier());

        while (this.input.current().is('.')) {
            this.input.consume();

            identifiers.add(parsePreReleaseIdentifier());
        }

        return new PreRelease(identifiers);
    }

    @NotNull
    private PreReleaseIdentifier parsePreReleaseIdentifier() throws ParseException {
        if (lookaheadAlphaNumeric())
            return new PreReleaseIdentifier.AlphaNumericalPreReleaseIdentifier(consumeAlphaNumeric());
        else
            return new PreReleaseIdentifier.NumericalPreReleaseIdentifier(new BigInteger(consumeNumber()));
    }

    @NotNull
    private BuildMetadata parseBuildMetadata() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!(this.input.current().isAlphaNumeric()))
            throw new ParseException("Alpha-Numeric identifier expected.", this.versionString, this.input.current());

        do {
            Char consumed = this.input.consume();
            if (consumed.is(DOT)) {
                if (this.input.current().is(DOT))
                    throw new ParseException("Alpha-Numeric identifier expected, but found period.", this.versionString, this.input.current());
                if (this.input.current().isEndOfInput())
                    throw new ParseException("Alpha-Numeric identifier expected, but found end of input.", this.versionString, this.input.current());
            }

            sb.append(consumed.getValue());
        } while (this.input.current().isAlphaNumeric() || this.input.current().is(DOT));

        return new BuildMetadata(sb.toString());
    }

    @NotNull
    private String consumeNumber() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!this.input.current().isDigit())
            throw new ParseException("Numeric identifier expected.", this.versionString, this.input.current());

        if (this.input.current().is('0') && this.input.next().isDigit())
            throw new ParseException("Numeric identifier must not contain leading zeros.", this.versionString, this.input.current());

        do {
            sb.append(this.input.consume().getValue());
        } while (this.input.current().isDigit());

        return sb.toString();
    }

    private boolean lookaheadAlphaNumeric() throws ParseException {
        boolean foundNonDigit = false;

        for (int i = 0; !foundNonDigit; i++) {
            foundNonDigit = this.input.next(i).isLetter() || this.input.next(i).is(DASH);

            if (!this.input.next(i).isAlphaNumeric())
                return foundNonDigit;
        }

        return true;
    }

    @NotNull
    private String consumeAlphaNumeric() throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (!(this.input.current().isAlphaNumeric()))
            throw new ParseException("Alpha-Numeric identifier expected.", this.versionString, this.input.current());

        do {
            sb.append(this.input.consume().getValue());
        } while (this.input.current().isAlphaNumeric());

        return sb.toString();
    }

    private void consumeCharacter(char expected) throws ParseException {
        if (this.input.current().is(expected))
            this.input.consume();
        else
            throw new ParseException(String.format("Illegal character. Character '%s' expected.", expected),
                    this.versionString, this.input.current());
    }
}
