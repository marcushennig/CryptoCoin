package crypto.shared;

import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with all kind of cryptographic functions
 */
public class Crypto {

    private final static Logger logger = Logger.getLogger(Crypto.class);

    private static final String algorithm = "SHA1withDSA";
    private static final int keySize = 1024;

    /**
     * Generate a given number of addresses (which consists of public/private key pairs)
     * @param numberOfAddresses Number of addresses to be generated
     */
    public static List<KeyPair> generateRandomKeyPairs(int numberOfAddresses) {

        List<KeyPair> keyPairs = new ArrayList<>();
        try {

            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");

            // Set key length and the source of randomness
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGenerator.initialize(keySize, secureRandom);

            for (int n = 0; n < numberOfAddresses; n++) {

                KeyPair keyPair = keyGenerator.generateKeyPair();

                logger.info(String.format("Generated new Address: (%s, %s)",
                        DatatypeConverter.printHexBinary(keyPair.getPublic().getEncoded()),
                        DatatypeConverter.printHexBinary(keyPair.getPrivate().getEncoded())));

                keyPairs.add(keyPair);
            }
            return keyPairs;

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {

            logger.error(String.format("Could not create addresses as the following error occurred: %s",
                    e.getMessage()));

            e.printStackTrace();
            return keyPairs;
        }
    }

    /**
     * Sign a message with given private key
     * @param privateKey Private key
     * @param message Message
     * @return Signature
     */
    public static byte[] sign(PrivateKey privateKey, byte[] message) {

        try {
            // Get signature object for creating and verifying signatures
            // DSA = digital signature algorithm
            Signature dsa = Signature.getInstance(algorithm);
            dsa.initSign(privateKey);
            dsa.update(message);

            return dsa.sign();

        } catch (InvalidKeyException |
                 SignatureException |
                 NoSuchAlgorithmException e) {

            logger.error(String.format("Could not create signature: %s", e.getMessage()));
            e.printStackTrace();
            return null;
        }


    }

    /**
     * This method takes a public key, a message and a signature, and returns true if and only if
     * the signature correctly verifies over message with the public key publicKey.
     * Internally, this uses RSA signature
     * @param publicKey Public key
     * @param message Message that was signed
     * @param signature Signature
     * @return True if signature is valid signature of the message under the key, otherwise false
     */
    public static boolean verifySignature(PublicKey publicKey, byte[] message, byte[] signature) {

        Signature dsa;

        try {

            dsa = Signature.getInstance(algorithm);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            return false;
        }

        try {

            dsa.initVerify(publicKey);

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        }

        try {

            dsa.update(message);

            return dsa.verify(signature);

        } catch (SignatureException e) {

            e.printStackTrace();
        }
        return false;
    }
}
