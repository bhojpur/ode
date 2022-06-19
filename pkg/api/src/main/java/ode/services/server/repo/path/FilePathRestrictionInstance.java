package ode.services.server.repo.path;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.util.HashSet;
import java.util.Set;

import ode.services.server.util.CurrentPlatform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

public enum FilePathRestrictionInstance {

    /** proscribe naming that probably causes system problems on Microsoft Windows */
    WINDOWS_REQUIRED("Windows required"),
    /** proscribe naming that probably causes sysadmin inconvenience on Microsoft Windows */
    WINDOWS_OPTIONAL("Windows optional"),
    /** proscribe naming that probably causes system problems on UNIX-like platforms */
    UNIX_REQUIRED("UNIX required"),
    /** proscribe naming that probably causes sysadmin inconvenience on UNIX-like platforms */
    UNIX_OPTIONAL("UNIX optional"),
    /** proscribe naming that probably causes system problems on the current platform */
    LOCAL_REQUIRED("local required"),
    /** proscribe naming that probably causes sysadmin inconvenience on the current platform */
    LOCAL_OPTIONAL("local optional");

    private static ImmutableMap<String, FilePathRestrictionInstance> nameLookup;

    /**
     * Convert the given character to the corresponding Unicode code point.
     * @param character a character
     * @return the character's code point
     */
    static int getCodePoint(char character) {
        final char[] singletonCharacterArray = new char[] {character};
        return Character.codePointAt(singletonCharacterArray, 0);
    }

    static {
        /* Some information on Windows naming strategies is to be found at
         * http://msdn.microsoft.com/en-us/library/aa365247(VS.85).aspx */

        final ImmutableMap.Builder<String, FilePathRestrictionInstance> nameLookupBuilder = ImmutableMap.builder();
        nameLookupBuilder.put(WINDOWS_REQUIRED.name, WINDOWS_REQUIRED);
        nameLookupBuilder.put(WINDOWS_OPTIONAL.name, WINDOWS_OPTIONAL);
        nameLookupBuilder.put(UNIX_REQUIRED.name, UNIX_REQUIRED);
        nameLookupBuilder.put(UNIX_OPTIONAL.name, UNIX_OPTIONAL);
        nameLookupBuilder.put(LOCAL_REQUIRED.name, LOCAL_REQUIRED);
        nameLookupBuilder.put(LOCAL_OPTIONAL.name, LOCAL_OPTIONAL);
        nameLookup = nameLookupBuilder.build();

        final ImmutableSet<Character> safeCharacters = ImmutableSet.of('_');
        final int safeCodePoint = getCodePoint(safeCharacters.iterator().next());

        final SetMultimap<Integer, Integer> transformationMatrix = HashMultimap.create();
        final Set<String> unsafePrefixes = new HashSet<String>();
        final Set<String> unsafeSuffixes = new HashSet<String>();
        final Set<String> unsafeNames = new HashSet<String>();

        for (int codePoint = 0; codePoint < 0x20; codePoint++) {
            transformationMatrix.put(codePoint, safeCodePoint);
        }
        transformationMatrix.put(0x22, getCodePoint('\'')); // "
        transformationMatrix.put(0x2A, getCodePoint('x'));  // *
        transformationMatrix.put(0x2F, getCodePoint('!'));  // /
        transformationMatrix.put(0x3A, getCodePoint(';'));  // :
        transformationMatrix.put(0x3C, getCodePoint('['));  // <
        transformationMatrix.put(0x3E, getCodePoint(']'));  // >
        transformationMatrix.put(0x3F, getCodePoint('%'));  // ?
        transformationMatrix.put(0x5C, getCodePoint('!'));  // \
        transformationMatrix.put(0x7C, getCodePoint('!'));  // |

        unsafeNames.add("AUX");
        unsafeNames.add("CLOCK$");
        unsafeNames.add("CON");
        unsafeNames.add("NUL");
        unsafeNames.add("PRN");
        for (char number = '1'; number <= '9'; number++) {
            unsafeNames.add("COM" + number);
            unsafeNames.add("LPT" + number);
        }

        for (final String unsafeName : unsafeNames) {
            unsafePrefixes.add(unsafeName + ".");
        }
        /* NTFS metadata files */
        unsafePrefixes.add("$");

        WINDOWS_REQUIRED.rules = new FilePathRestrictions(transformationMatrix, unsafePrefixes, null, unsafeNames, safeCharacters);

        unsafeSuffixes.clear();
        unsafeSuffixes.add(".");
        unsafeSuffixes.add(" ");

        WINDOWS_OPTIONAL.rules = new FilePathRestrictions(null, null, unsafeSuffixes, null, safeCharacters);

        transformationMatrix.clear();
        transformationMatrix.put(0x00, safeCodePoint);      // NUL
        transformationMatrix.put(0x2F, getCodePoint('!'));  // /

        UNIX_REQUIRED.rules = new FilePathRestrictions(transformationMatrix, null, null, null, safeCharacters);

        unsafePrefixes.clear();
        unsafePrefixes.add(".");
        unsafePrefixes.add("-");

        UNIX_OPTIONAL.rules = new FilePathRestrictions(null, unsafePrefixes, null, null, safeCharacters);

        if (CurrentPlatform.isWindows()) {
            LOCAL_REQUIRED.rules = WINDOWS_REQUIRED.rules;
            LOCAL_OPTIONAL.rules = WINDOWS_OPTIONAL.rules;
        } else if (CurrentPlatform.isLinux() || CurrentPlatform.isMacOSX()) {
            LOCAL_REQUIRED.rules = UNIX_REQUIRED.rules;
            LOCAL_OPTIONAL.rules = UNIX_OPTIONAL.rules;
        } else {
            /* take a conservative approach */
            LOCAL_REQUIRED.rules = getFilePathRestrictions(WINDOWS_REQUIRED, UNIX_REQUIRED);
            LOCAL_OPTIONAL.rules = getFilePathRestrictions(WINDOWS_OPTIONAL, UNIX_OPTIONAL);
        }
    }

    public final String name;
    private FilePathRestrictions rules;

    /**
     * Get a set of rules by which local files may not be named on the file-system,
     * formed by combining the rules of the given names with rules against control characters.
     * @param names the names of the desired rules
     * @return a rule set
     */
    public static FilePathRestrictions getFilePathRestrictions(String... names) {
        final FilePathRestrictionInstance[] enums = new FilePathRestrictionInstance[names.length];
        for (int index = 0; index < names.length; index++) {
            enums[index] = nameLookup.get(names[index]);
        }
        return getFilePathRestrictions(enums);
    }

    /**
     * Get a set of rules by which local files may not be named on the file-system,
     * formed by combining the given rules identifiers with rules against control characters.
     * @param enums the names of the desired rules
     * @return a rule set
     */
    public static FilePathRestrictions getFilePathRestrictions(FilePathRestrictionInstance... enums) {
        final FilePathRestrictions[] rules = new FilePathRestrictions[enums.length];
        for (int index = 0; index < enums.length; index++) {
            rules[index] = enums[index].rules;
        }
        return FilePathRestrictions.combineFilePathRestrictions(rules);
    }

    /**
     * Construct an enumeration instance noting its corresponding set of rules and their name.
     * @param name the rules' name
     */
    private FilePathRestrictionInstance(String name) {
        this.name = name;
    }
}