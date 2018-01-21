package crypto.shared;

import java.security.*;
import java.util.ArrayList;

public class Helper {



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

        Signature sig;

        try {

            sig = Signature.getInstance("SHA256withRSA");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            return false;
        }

        try {

            sig.initVerify(publicKey);

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        }
        try {

            sig.update(message);

            return sig.verify(signature);

        } catch (SignatureException e) {

            e.printStackTrace();
        }

        return false;

    }

    /**
     * Convert list of bytes into array
     * @param bytes List of bytes
     * @return Array of bytes
     */
    public static byte[] convertToByteArray(ArrayList<Byte> bytes) {

        byte[] rawBytes = new byte[bytes.size()];
        int i = 0;
        for (Byte b : bytes) {
            rawBytes[i++] = b;
        }
        return rawBytes;
    }
}
