package crypto.transaction;

import crypto.shared.Base58;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Transaction output
 */
public class TransactionOutput {

    /** The value of the output */
    public double value;

    /** The address or public key of the recipient */
    public PublicKey address;

    /**
     * Constructor
     * @param value Value of the output
     * @param address Public key to which the value is paid
     */
    public TransactionOutput(double value, PublicKey address) {

        this.value = value;
        this.address = address;
    }

    /**
     * Get raw data of output that can be used for signature
     * @return List of bytes
     */
    public ArrayList<Byte> getRawData() {

        ArrayList<Byte> rawData = new ArrayList<>();

        ByteBuffer valueBytes = ByteBuffer.allocate(Double.SIZE / 8);
        valueBytes.putDouble(this.value);

        for (byte b : valueBytes.array()) {
            rawData.add(b);
        }

        byte[] addressBytes = this.address.getEncoded();
        for (byte b : addressBytes) {
            rawData.add(b);
        }

        return rawData;
    }

    @Override
    public String toString() {

        return String.format("$ %f --> %s", this.value, Base58.encode(this.address.getEncoded()));
    }
}
