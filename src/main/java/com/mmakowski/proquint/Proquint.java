package com.mmakowski.proquint;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <a href="https://arxiv.org/html/0901.4016">A Proposal for Proquints: Identifiers that are Readable, Spellable, and Pronounceable</a>.
 *
 * <p>Example encodings:
 * <ul>
 * <li>0x12345678: damuh-jinum</li>
 * <li>0x1a01337e5b61: domad-gatuv-jotod</li>
 * </ul>
 * </p>
 */
public final class Proquint {
    private static final char[] consonants = {'b', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'z'};
    private static final char[] vowels = {'a', 'i', 'o', 'u'};
    private static final Map<Character, Byte> consonantBits = new HashMap<>();
    private static final Map<Character, Byte> vowelBits = new HashMap<>();

    static {
        for (byte b = 0; b < consonants.length; b++) {
            consonantBits.put(consonants[b], b);
        }
        for (byte b = 0; b < vowels.length; b++) {
            vowelBits.put(vowels[b], b);
        }
    }

    /**
     * Encodes bytes as a proquint.
     *
     * @param bytes the byte array to encode as a proquint; must be of even length
     * @return the proquint string for the bytes provided
     */
    public static String fromBytes(final byte[] bytes) {
        if (bytes.length % 2 != 0) {
            throw new IllegalArgumentException("the number of bytes must be even, was " + bytes.length);
        }
        final int proquintLength = (bytes.length / 2) * 6 - 1;
        final StringBuilder builder = new StringBuilder(proquintLength);
        for (int i = 0; i < bytes.length - 1; i += 2) {
            builder.append(consonants[(bytes[i] & 0b11110000) >> 4]);
            builder.append(vowels[(bytes[i] & 0b00001100) >> 2]);
            builder.append(consonants[((bytes[i] & 0b00000011) << 2) | ((bytes[i + 1] & 0b11000000) >> 6)]);
            builder.append(vowels[(bytes[i + 1] & 0b00110000) >> 4]);
            builder.append(consonants[bytes[i + 1] & 0b00001111]);
            if (i < bytes.length - 2) {
                builder.append('-');
            }
        }
        return builder.toString();
    }

    /**
     * Decodes bytes from a proquint.
     * @param proquint the proquint string to decode
     * @return a byte array containing the decoded bytes
     */
    public static byte[] toBytes(final String proquint) {
        final int bytesLength = (proquint.length() + 1) / 3;
        final byte[] bytes = new byte[bytesLength];
        for (int i = 0; i < bytes.length - 1; i += 2) {
            bytes[i] = (byte) ((consonantBits.get(proquint.charAt(i * 3)) << 4) |
                    (vowelBits.get(proquint.charAt(i * 3 + 1)) << 2) |
                    ((consonantBits.get(proquint.charAt(i * 3 + 2)) & 0b00001100) >> 2));
            bytes[i+1] = (byte) (((consonantBits.get(proquint.charAt(i * 3 + 2)) & 0b00000011) << 6) |
                    (vowelBits.get(proquint.charAt(i * 3 + 3)) << 4) |
                    (consonantBits.get(proquint.charAt(i * 3 + 4))));
        }
        return bytes;
    }
}
