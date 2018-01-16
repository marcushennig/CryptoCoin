package crypto.transaction;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Input of an transaction
 * The input points to a transaction (prevTxHash = unique ID) and the index of the output
 * (value --> address).
 */
public class Input {

    /** Hash of the Transaction whose output is being used */
    public byte[] prevTxHash;

    /** Used output's index in the previous transaction */
    public int outputIndex;

    /** The signature produced to check validity */
    public byte[] signature;

    /**
     * Constructor
     * @param prevHash Hash of previous transaction
     * @param index Index
     */
    public Input(byte[] prevHash, int index) {

        if (prevHash == null) {

            this.prevTxHash = null;

        } else {

            this.prevTxHash = Arrays.copyOf(prevHash, prevHash.length);
        }
        outputIndex = index;
    }

    /**
     * Add signature to the input
     * @param signature The signature
     */
    public void addSignature(byte[] signature) {
        if (signature == null) {
            this.signature = null;
        } else {
            this.signature = Arrays.copyOf(signature, signature.length);
        }
    }

    /**
     * Get raw data of input without signature that can be used for signature
     * @return List of bytes
     */
    public ArrayList<Byte> getRawDataWithoutSignature() {

        ArrayList<Byte> rawData = new ArrayList<>();

        // Add previous hash to signature data
        if (this.prevTxHash != null) {
            for (byte b : this.prevTxHash) {
                rawData.add(b);
            }
        }
        // Add output index to signature data
        ByteBuffer outputIndexBytes = ByteBuffer.allocate(Integer.SIZE / 8);
        outputIndexBytes.putInt(this.outputIndex);

        for (byte b : outputIndexBytes.array()) {
            rawData.add(b);
        }
        return rawData;
    }

    /**
     * Get raw data with signature
     * @return List of bytes
     */
    public ArrayList<Byte> getRawDataWithSignature() {

        ArrayList<Byte> rawData = new ArrayList<>(this.getRawDataWithoutSignature());
        if (signature != null) {

            for (byte aSignature : this.signature) {
                rawData.add(aSignature);
            }
        }
        return rawData;
    }
}
