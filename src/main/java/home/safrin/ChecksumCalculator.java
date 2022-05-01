package home.safrin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides methods to perform CRC/checksum on various types of input streams.
 */
public final class ChecksumCalculator {

  /* Private constructor to hide default constructor implementation forcing this class to be used only in a static
   * context. Since this class has no attributes or state, it's useless to have instances of this class. */
  private ChecksumCalculator() {
  }

  /**
   * Generic method to provide checksum of the contents of any arbitrary {@link InputStream} instance.
   *
   * @param messageDigest a {@link MessageDigest} instance configure for a specific hash algorithm
   * @param inputStream   an arbitrary {@link InputStream} instance on some content for which the hash needs to be
   *                      computed
   * @return a String value that represents the {@code byte[]} in hexadecimal format of the computed hash of the
   * content from the specified {@code inputStream} parameter value
   * @throws IOException when there's any difficulty reading the provided {@code inputStream} parameter value for
   * content
   */
  public static String calculateChecksum(final MessageDigest messageDigest, final InputStream inputStream)
    throws IOException {
    // Create byte array to read data in 1K chunks
    byte[] byteArray = new byte[1024];
    int bytesCount = 0;

    // Update message digest with chunks of data from the file
    while ((bytesCount = inputStream.read(byteArray)) != -1) {
      messageDigest.update(byteArray, 0, bytesCount);
    }

    // Convert the hash from decimal format to hexadecimal format
    final var byteBuffer = ByteBuffer.wrap(messageDigest.digest());
    final var sb = new StringBuilder();
    Stream.generate(byteBuffer::get)
          .limit(byteBuffer.capacity())
          .forEach(b -> sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1)));

    // Return complete hash in hexadecimal format
    return sb.toString();
  }

  /**
   * Convenience method to compute the hash of a specified {@link File} object.
   *
   * @param messageDigest a {@link MessageDigest} instance configure for a specific hash algorithm
   * @param file          a {@link File} object for which this class has read access via the local filesystem
   * @return a String value that represents the {@code byte[]} in hexadecimal format of the computed hash of the
   * specified {@code file} parameter value
   * @throws IOException when there's any difficulty reading the provided {@code file} parameter value for content
   */
  public static String calculateChecksum(final MessageDigest messageDigest, final File file) throws IOException {
    try (var fis = new FileInputStream(file)) {
      return calculateChecksum(messageDigest, fis);
    }
  }

  /**
   * Gets the set of all available algorithms for the specified {@link Provider} and {@link Class} in the JRE.
   *
   * @param provider  a {@link Provider} for the Java Security API that specifies a number of algorithms and services
   * @param typeClass the {@link Class} that specifies for which specific Java Security service the caller is most
   *                  interested in finding available algorithms
   * @return a {@link Set} of algorithms available in the JRE for the specified {@code provider} and {@code typeClass}
   * parameter values
   */
  public static Set<String> getAvailableAlgorithms(final Provider provider, final Class<?> typeClass) {
    String type = typeClass.getSimpleName();

    final var availableAlgorithms = new HashSet<String>();

    provider.getServices().stream()
        .filter(service -> service.getType().equalsIgnoreCase(type))
        .forEach(service -> availableAlgorithms.add(service.getAlgorithm()));

    return Set.copyOf(availableAlgorithms);
  }
}
