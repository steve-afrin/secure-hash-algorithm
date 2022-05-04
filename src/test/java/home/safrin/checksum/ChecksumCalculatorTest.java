package home.safrin.checksum;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChecksumCalculatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ChecksumCalculatorTest.class);

    private static final File TEST_FILE = new File("src/test/resources/checksum-test.txt");
    private static final String TEST_STRING = "some.user@whatever.domain.makes.sense.com";

    @Test
    @DisplayName("Display Available Hash Algorithms for MessageDigest")
    void displayAvailableHashAlgorithms() {
        final var availableAlgorithms = new TreeSet<String>();

        for (final Provider provider : Security.getProviders()) {
            availableAlgorithms.addAll(ChecksumCalculator.getAvailableAlgorithms(provider, MessageDigest.class));
        }

        Assertions.assertNotEquals(0, availableAlgorithms.size(), "No available MessageDigest hash algorithms");

        LOG.info("Available MessageDigest hash algorithms are: {}", availableAlgorithms);
    }

    @ParameterizedTest(name = "Test calculateChecksum for {0} hash algorithm")
    @MethodSource("hashAlgorithmValuesForFile")
    void testCalculateChecksumForFile(final HashAlgorithm hashAlgorithm, final String expectedChecksum)
            throws IOException, NoSuchAlgorithmException {

        // Assert the correct computation of the CRC/checksum
        Assertions.assertEquals(
                expectedChecksum,
                ChecksumCalculator.calculateChecksum(hashAlgorithm.getValue(), TEST_FILE),
                String.format("The %s checksum values are not identical", hashAlgorithm));
    }

    @ParameterizedTest(name = "Test calculateChecksum for {0} hash algorithm")
    @MethodSource("hashAlgorithmValuesForString")
    void testCalculateChecksumForString(final HashAlgorithm hashAlgorithm, final String expectedChecksum)
            throws IOException, NoSuchAlgorithmException {

        // Assert the correct computation of the CRC/checksum
        Assertions.assertEquals(
                expectedChecksum,
                ChecksumCalculator.calculateChecksum(hashAlgorithm.getValue(), TEST_STRING),
                String.format("The %s checksum values are not identical", hashAlgorithm));
    }

    private Optional<Map.Entry<String, String>> generateHashValue(final String hashAlgorithm, final File file) {
        final Map.Entry<String, String> result;

        try {
            result = Map.entry(hashAlgorithm, ChecksumCalculator.calculateChecksum(hashAlgorithm, file));
        } catch (IOException | NoSuchAlgorithmException ex) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    // Has to be static for JUnit 5 to pick up as a valid method source for a parameterized test
    private static Stream<Arguments> hashAlgorithmValuesForFile() {
        // MD5 - 128 bits (16 bytes) - 32 digit (hexadecimal) hash
        // SHA-256 - 256 bits (32 bytes) - 64 digit (hexadecimal) hash
        // SHA-512 - 512 bits (64 bytes) - 128 digit (hexadecimal) hash
        return Stream.of(
                Arguments.of(HashAlgorithm.MD5, "57d0d1705455e79ee84acc54b9505a13"),
                Arguments.of(HashAlgorithm.SHA_256, "f4208f01a1333271843e402a4740f4a6c32452d3483eedf56e4d081"
                        + "cdd9931b4"),
                Arguments.of(HashAlgorithm.SHA_512, "3215d58cf8395526a88e96a214060452d3ef85c85553d7d792b4a4a"
                        + "acdadf2243281de701f6bc372199a6b2f8654fb11edeaeb90e5c4108ebfedb2fe35f3cb0a"),
                Arguments.of(HashAlgorithm.SHA3_256, "df657a69ef7862a7c623cd01ef54d5b1117bada3b10eeb1e81d070"
                        + "d6cfc2cea8"),
                Arguments.of(HashAlgorithm.SHA3_512, "df603f7f9e1078e7087c0ffa08724a7ff72560f5985e657d64cb26"
                        + "c9d835d90caca85b1fb406190dcc07e6d2d7913f4a50c4783d7185ccc2dadd0ea47c0995c3")
                        );
    }

    private static Stream<Arguments> hashAlgorithmValuesForString() {
        // MD5 - 128 bits (16 bytes) - 32 digit (hexadecimal) hash
        // SHA-256 - 256 bits (32 bytes) - 64 digit (hexadecimal) hash
        // SHA-512 - 512 bits (64 bytes) - 128 digit (hexadecimal) hash
        return Stream.of(
                Arguments.of(HashAlgorithm.MD5, "16ff51a5b1cee26106c33e0da3e9be35"),
                Arguments.of(HashAlgorithm.SHA_256, "8c87eca90374ba4248befe0c1a1e7ae5502f3e525be62f332a93929"
                        + "76967a7c2"),
                Arguments.of(HashAlgorithm.SHA_512, "85b7d43d08fc85089c0964906a2bcc9c142f807fbb94e1e903a884a"
                        + "e07c7bf17b437cfd330099bc53ef29634c5d0c688d845764085707d0c9dfbdc5e582ef4d9"),
                Arguments.of(HashAlgorithm.SHA3_256, "a95ea1266c154be025aa66053d2293ce763a92894c71ed5dd17892"
                        + "4e951b34d4"),
                Arguments.of(HashAlgorithm.SHA3_512, "6d209c1a08bca005c4b8ee97a81b5f9cd20aefe2123821b36397d1"
                        + "09b095cb38c78cc383ec34941dc6f72afea14b4bd8315031193e6dbe0112744c74bad246f2")
                        );
    }
}