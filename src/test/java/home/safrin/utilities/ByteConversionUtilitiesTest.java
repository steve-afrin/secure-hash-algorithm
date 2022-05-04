package home.safrin.utilities;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ByteConversionUtilitiesTest {

    @ParameterizedTest(name = "Test convertBytesToHexValue for {0}")
    @MethodSource
    void convertBytesToHexValue(final byte[] bytes, final String expectedValue) {
        Assertions.assertEquals(expectedValue, ByteConversionUtilities.convertBytesToHexValue(bytes));
    }

    @ParameterizedTest(name = "Test convertBytesToBinaryValue for {0}")
    @MethodSource
    void convertBytesToBinaryValue(final byte[] bytes, final String expectedValue) {
        Assertions.assertEquals(expectedValue, ByteConversionUtilities.convertBytesToBinaryValue(bytes));
    }

    @ParameterizedTest(name = "Test convertBytesToOctalValue for {0}")
    @MethodSource
    void convertBytesToOctalValue(final byte[] bytes, final String expectedValue) {
        Assertions.assertEquals(expectedValue, ByteConversionUtilities.convertBytesToOctalValue(bytes));
    }

    private static Stream<Arguments> convertBytesToHexValue() {
        return Stream.of(
                Arguments.of(new byte[]{0}, "00"),
                Arguments.of(new byte[]{1}, "01"),
                Arguments.of(new byte[]{(byte) 255}, "ff"),
                Arguments.of(new byte[]{(byte) 250, (byte) 206}, "face")
                        );
    }

    private static Stream<Arguments> convertBytesToBinaryValue() {
        return Stream.of(
                Arguments.of(new byte[]{0}, "00000000"),
                Arguments.of(new byte[]{1}, "00000001"),
                Arguments.of(new byte[]{(byte) 255}, "11111111"),
                Arguments.of(new byte[]{(byte) 250, (byte) 206}, "1111101011001110")
                        );
    }

    private static Stream<Arguments> convertBytesToOctalValue() {
        return Stream.of(
                Arguments.of(new byte[]{0}, "000"),
                Arguments.of(new byte[]{1}, "001"),
                Arguments.of(new byte[]{(byte) 255}, "377"),
                Arguments.of(new byte[]{(byte) 250, (byte) 206}, "372316")
                        );
    }
}