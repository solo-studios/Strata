/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Extensions.kt is part of Strata
 * Last modified on 23-02-2022 12:43 p.m.
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

package ca.solostudios.strata.kotlin

import ca.solostudios.strata.Versions
import ca.solostudios.strata.parser.VersionParser
import ca.solostudios.strata.parser.VersionRangeParser
import ca.solostudios.strata.parser.tokenizer.ParseException
import ca.solostudios.strata.version.Version
import ca.solostudios.strata.version.VersionRange
import java.math.BigInteger

/**
 * Whether this range is satisfied by the provided version.
 *
 * @param version The version to check against.
 *
 * @return `true` if this range is satisfied by the provided version, false otherwise.
 */
public operator fun VersionRange.contains(version: Version): Boolean = this.isSatisfiedBy(version)

/**
 * Parses a version from the provided string, according to the semver spec as listed in [VersionParser].
 *
 * @return The parsed version.
 *
 * @throws ParseException If an exception occurred during the parsing of the version.
 * If taking user input, the message from this exception is highly useful and should be returned to the user.
 * @see Versions.parseVersion
 * @see VersionParser
 */
@Throws(ParseException::class)
public fun String.toVersion(): Version = Versions.parseVersion(this)

/**
 * Parses a [VersionRange] from the provided string,
 * according to the specification described in [VersionRangeParser].
 *
 * @return The parsed version range.
 *
 * @throws ParseException If an exception occurred during the parsing of the version.
 * If taking user input, the message from this exception is highly useful and should be returned to the user.
 * @see Versions.parseVersion
 * @see VersionRangeParser
 */
@Throws(ParseException::class)
public fun String.toVersionRange(): VersionRange = Versions.parseVersionRange(this)

/**
 * Constructs a new version from the provided values.
 *
 * - The [first][Triple.first] value is the major version.
 * - The [second][Triple.second] value is the minor version.
 * - The [third][Triple.third] value is the patch version.
 *
 * @return The new version.
 */
@JvmName("toVersionInt")
public fun Triple<Int, Int, Int>.toVersion(): Version = Versions.getVersion(first, second, third)

/**
 * Constructs a new version from the provided values.
 *
 * - The [first][Triple.first] value is the major version.
 * - The [second][Triple.second] value is the minor version.
 * - The [third][Triple.third] value is the patch version.
 *
 * @return The new version.
 */
@JvmName("toVersionLong")
public fun Triple<Long, Long, Long>.toVersion(): Version = Versions.getVersion(
    BigInteger.valueOf(first),
    BigInteger.valueOf(second),
    BigInteger.valueOf(third),
)

/**
 * Constructs a new version from the provided values.
 *
 * - The [first][Triple.first] value is the major version.
 * - The [second][Triple.second] value is the minor version.
 * - The [third][Triple.third] value is the patch version.
 *
 * @return The new version.
 */
@JvmName("toVersionBigInteger")
public fun Triple<BigInteger, BigInteger, BigInteger>.toVersion(): Version = Versions.getVersion(first, second, third)
