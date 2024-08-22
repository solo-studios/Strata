/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file KotlinIdentityTests.kt is part of Strata.
 * Last modified on 22-08-2024 07:13 p.m.
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

import ca.solostudios.strata.Versions
import ca.solostudios.strata.kotlin.toVersion
import ca.solostudios.strata.kotlin.toVersionRange
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file KotlinIdentityTests.kt is part of Strata
 * Last modified on 23-02-2022 12:26 p.m.
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

class KotlinIdentityTests {
    @Test
    fun testToVersionIdentity() {
        val versionStrings = listOf("3.0.0", "0.2.5", "0.3.0", "0.1.3", "0.0.3-abcd", "0.0.1-8aw3-21312", "0.0.0-abcde+0yu9awe3")

        val correctVersions = versionStrings.map { Versions.parseVersion(it) }

        val kotlinVersions = versionStrings.map { it.toVersion() }

        assertContentEquals(correctVersions, kotlinVersions, "Kotlin .toVersion strings do not match")
    }

    @Test
    fun testToVersionRangeIdentity() {
        val versionRangeStrings = listOf("(,)", "(1.2.3,)", "[1.2.3,]", "(,4.5.6)", "(,4.5.6]", "1.2.3", "1.2.+", "1.+", "+")

        val correctVersionRanges = versionRangeStrings.map { Versions.parseVersionRange(it) }

        val kotlinVersionRanges = versionRangeStrings.map { it.toVersionRange() }

        assertContentEquals(correctVersionRanges, kotlinVersionRanges, "Kotlin .toVersionRange strings do not match")
    }
}
