package com.mmakowski.proquint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public final class ProquintTest {
    @Test
    public void ipExamples() {
        // the examples from the proposal
        final Iterable<IpExample> examples = ImmutableList.of(
                new IpExample("127.0.0.1", "lusab-babad"),
                new IpExample("63.84.220.193", "gutih-tugad"),
                new IpExample("63.118.7.35", "gutuk-bisog"),
                new IpExample("140.98.193.141", "mudof-sakat"),
                new IpExample("64.255.6.200", "haguz-biram"),
                new IpExample("128.30.52.45", "mabiv-gibot"),
                new IpExample("147.67.119.2", "natag-lisaf"),
                new IpExample("212.58.253.68", "tibup-zujah"),
                new IpExample("216.35.68.215", "tobog-higil"),
                new IpExample("216.68.232.21", "todah-vobij"),
                new IpExample("198.81.129.136", "sinid-makam"),
                new IpExample("12.110.110.204", "budov-kuras"));

        for (final IpExample example : examples) {
            final byte[] ipBytes = ipToByteArray(example.ip);
            assertEquals(example.proquint, Proquint.fromBytes(ipBytes));
            assertArrayEquals(ipBytes, Proquint.toBytes(example.proquint));
        }
    }

    @Test
    public void randomBytesRoundTrip() {
        final int trialsPerSize = 100;
        final Iterable<Integer> sizes = ImmutableList.of(2, 4, 8, 10, 20);
        final Random rng = new Random();
        for (int size : sizes) {
            final byte[] bytes = new byte[size];
            for (int trial = 1; trial <= trialsPerSize; trial++) {
                rng.nextBytes(bytes);
                assertArrayEquals("round-trip failed for: " + Arrays.toString(bytes), bytes, Proquint.toBytes(Proquint.fromBytes(bytes)));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void requiresEvenNumberOfBytes() {
        Proquint.fromBytes(new byte[3]);
    }

    private byte[] ipToByteArray(final String ip) {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("error decoding IP address", e);
        }
    }

    private static final class IpExample {
        public final String ip;
        public final String proquint;

        private IpExample(final String ip, final String proquint) {
            this.ip = ip;
            this.proquint = proquint;
        }
    }
}
