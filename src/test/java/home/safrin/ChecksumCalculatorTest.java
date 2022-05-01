package home.safrin;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.TreeSet;
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
  @MethodSource("hashAlgorithmValues")
  void testCalculateChecksum(final String hashAlgorithm, final String expectedChecksum)
    throws IOException, NoSuchAlgorithmException {

    final File file = new File("src/test/resources/checksum-test.txt");

    // Use specified hash algorithm as provided by parameter value
    final MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
    LOG.debug("MessageDigest provider and available services are {}", messageDigest.getProvider().getInfo());

    // Assert the correct computation of the CRC/checksum
    Assertions.assertEquals(
      expectedChecksum,
      ChecksumCalculator.calculateChecksum(messageDigest, file),
      String.format("The %s checksum values are not identical", hashAlgorithm));
  }

  // Has to be static for JUnit 5 to pick up as a valid method source for a parameterized test
  private static Stream<Arguments> hashAlgorithmValues() {
    // MD5 - 128 bits (16 bytes) - 32 digit (hexadecimal) hash
    // SHA-256 - 256 bits (32 bytes) - 64 digit (hexadecimal) hash
    // SHA-512 - 512 bits (64 bytes) - 128 digit (hexadecimal) hash
    return Stream.of(
      Arguments.of("MD5", "57d0d1705455e79ee84acc54b9505a13"),
      Arguments.of("SHA-256", "f4208f01a1333271843e402a4740f4a6c32452d3483eedf56e4d081cdd9931b4"),
      Arguments.of("SHA-512", "3215d58cf8395526a88e96a214060452d3ef85c85553d7d792b4a4aacdadf2243281de701"
        + "f6bc372199a6b2f8654fb11edeaeb90e5c4108ebfedb2fe35f3cb0a"),
      Arguments.of("SHA3-256", "df657a69ef7862a7c623cd01ef54d5b1117bada3b10eeb1e81d070d6cfc2cea8"),
      Arguments.of("SHA3-512", "df603f7f9e1078e7087c0ffa08724a7ff72560f5985e657d64cb26c9d835d90caca85b1f"
        + "b406190dcc07e6d2d7913f4a50c4783d7185ccc2dadd0ea47c0995c3")
    );
  }
}