package home.safrin.utilities;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

/**
 * Utility methods to assist in converting byte arrays to various base number systems (e.g., binary, hexadecimal,
 * etc.)
 */
public final class ByteConversionUtilities {
    private static final int MAX_BITS_FOR_UNSIGNED_BYTE = 0xff;
    private static final int EIGHT_BIT_FILTER = 0X100;
    private static final int OCTAL_FILTER = 01000;

    private ByteConversionUtilities() {
    }

    public static String convertBytesToHexValue(final byte[] bytes) {
        return convertBytes(bytes, 16);
    }

    public static String convertBytesToBinaryValue(final byte[] bytes) {
        return convertBytes(bytes, 2);
    }

    public static String convertBytesToOctalValue(final byte[] bytes) {
        return convertBytes(bytes, 8);
    }

    private static String convertBytes(final byte[] bytes, final int radix) {
        // Convert the hash from decimal format to hexadecimal format
        final var byteBuffer = ByteBuffer.wrap(bytes);
        final var digitsFilter = radix == 8 ? OCTAL_FILTER : EIGHT_BIT_FILTER;
        final var sb = new StringBuilder();
        Stream.generate(byteBuffer::get)
              .limit(byteBuffer.capacity())
              .forEach(b -> sb.append(Integer.toString((b & MAX_BITS_FOR_UNSIGNED_BYTE) + digitsFilter, radix)
                                             .substring(1)));

        // Return complete hash in hexadecimal format
        return sb.toString();
    }
}
