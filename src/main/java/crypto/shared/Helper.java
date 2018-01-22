package crypto.shared;

import java.util.ArrayList;

public class Helper {

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
