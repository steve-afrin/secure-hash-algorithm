package home.safrin.checksum;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public enum HashAlgorithm {
    MD5("MD5"), SHA_256("SHA-256"), SHA_512("SHA-512"), SHA3_256("SHA3-256"), SHA3_512("SHA3-512");

    private final String value;

    HashAlgorithm(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HashAlgorithm findValueOf(final String value) throws NoSuchAlgorithmException {
        return Arrays.stream(values())
                .filter(val -> value.equalsIgnoreCase(val.getValue()))
                .findFirst()
                .orElseThrow(() -> new NoSuchAlgorithmException(String.format("No algorithm matching '%s' in '%s'",
                                                                              value, HashAlgorithm.class.getName())));

    }
}
