package ode.services.server.util;

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

import static ode.rtypes.rstring;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import ode.util.checksum.ChecksumType;
import ode.model.ChecksumAlgorithm;
import ode.model.ChecksumAlgorithmI;
import ode.model.enums.ChecksumAlgorithmAdler32;
import ode.model.enums.ChecksumAlgorithmCRC32;
import ode.model.enums.ChecksumAlgorithmFileSize64;
import ode.model.enums.ChecksumAlgorithmMD5128;
import ode.model.enums.ChecksumAlgorithmMurmur3128;
import ode.model.enums.ChecksumAlgorithmMurmur332;
import ode.model.enums.ChecksumAlgorithmSHA1160;

/**
 * Work with {@link ChecksumAlgorithm} enumeration instances,
 * including mapping their values back to {@link ChecksumType}s.
 */
public class ChecksumAlgorithmMapper {
    public static Function<ChecksumAlgorithm, String> CHECKSUM_ALGORITHM_NAMER = new Function<ChecksumAlgorithm, String>() {
        /** {@inheritDoc} */
        public String apply(ChecksumAlgorithm checksumAlgorithm) {
            return checksumAlgorithm.getValue().getValue();
        }
    };

    private static final ImmutableMap<String, ChecksumType> checksumAlgorithms =
            ImmutableMap.<String, ChecksumType> builder().
            put(ChecksumAlgorithmAdler32.value, ChecksumType.ADLER32).
            put(ChecksumAlgorithmCRC32.value, ChecksumType.CRC32).
            put(ChecksumAlgorithmMD5128.value, ChecksumType.MD5).
            put(ChecksumAlgorithmMurmur332.value, ChecksumType.MURMUR32).
            put(ChecksumAlgorithmMurmur3128.value, ChecksumType.MURMUR128).
            put(ChecksumAlgorithmSHA1160.value, ChecksumType.SHA1).
            put(ChecksumAlgorithmFileSize64.value, ChecksumType.FILE_SIZE).
            build();

    private static ChecksumAlgorithm getChecksumAlgorithmWithValue(String name) {
        final ChecksumAlgorithm algorithm = new ChecksumAlgorithmI();
        algorithm.setValue(rstring(name));
        return algorithm;
    }

    public static ChecksumType getChecksumType(ode.model.enums.ChecksumAlgorithm algorithm) {
        return checksumAlgorithms.get(algorithm.getValue());
    }

    public static ChecksumType getChecksumType(ChecksumAlgorithm algorithm) {
        return checksumAlgorithms.get(algorithm.getValue().getValue());
    }

    public static ChecksumAlgorithm getChecksumAlgorithm(String name) {
        if (!checksumAlgorithms.containsKey(name)) {
            throw new IllegalArgumentException(name + " is not recognized as a value of the enumeration " +
                    ode.model.enums.ChecksumAlgorithm.class.getCanonicalName());
        }
        return getChecksumAlgorithmWithValue(name);
    }

    public static List<ChecksumAlgorithm> getAllChecksumAlgorithms() {
        final List<ChecksumAlgorithm> algorithms = new ArrayList<ChecksumAlgorithm>(checksumAlgorithms.size());
        for (final String name : checksumAlgorithms.keySet()) {
            algorithms.add(getChecksumAlgorithmWithValue(name));
        }
        return algorithms;
    }
}