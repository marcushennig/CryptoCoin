package crypto.transaction;

import crypto.shared.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Represents a coin transaction
 * A transaction consists of a list of inputs, a list of outputs and a unique ID (see the getRawTx() method).
 * The class also contains methods to add and remove an input, add an output, compute digests to sign/hash,
 * add a signature to an input, and compute and store the hash of the transaction once all inputs/outputs/signatures
 * have been added.
 */
public class Transaction {

    /** Hash of the transaction, its unique id */
    private byte[] hash;

    private ArrayList<Input> inputs;

    private ArrayList<Output> outputs;

    /**
     * Constructor
     */
    public Transaction() {

        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    /**
     * Copy constructor
     * @param transaction Other transaction
     */
    public Transaction(Transaction transaction) {

        this.hash = transaction.hash.clone();
        this.inputs = new ArrayList<>(transaction.inputs);
        this.outputs = new ArrayList<>(transaction.outputs);
    }

    /**
     * Add input to transaction
     * @param prevTxHash Hash of the previous transaction
     * @param outputIndex  Index
     */
    public void addInput(byte[] prevTxHash, int outputIndex) {

        Input input = new Input(prevTxHash, outputIndex);
        this.inputs.add(input);
    }

    /**
     * Add output to transaction
     * @param value value
     * @param address Public key to which value is paid
     */
    public void addOutput(double value, PublicKey address) {

        Output output = new Output(value, address);
        this.outputs.add(output);
    }

    /**
     * Remove input with specific index
     * @param index The index of the output to be removed
     */
    public void removeInput(int index) {

        inputs.remove(index);
    }

    /**
     * Remove Input
     * @param ut
     */
    public void removeInput(UTXO ut) {

        for (int i = 0; i < this.inputs.size(); i++) {

            Input input = this.inputs.get(i);

            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

            if (utxo.equals(ut)) {

                this.inputs.remove(i);
                return;
            }
        }
    }

    /**
     * Retrieve the raw data that is to be signed
     * @param index Index of of the ith input
     * @return Raw data to be signed
     */
    public byte[] getRawDataToSign(int index) {

        if (index > this.inputs.size()) {
            return null;
        }
        Input input = this.inputs.get(index);

        // Get bytes of ith input
        final ArrayList<Byte> signatureData = new ArrayList<>(input.getRawDataWithoutSignature());

        // Get byte data of a all outputs
        this.outputs.forEach(output -> signatureData.addAll(output.getRawData()));

        return Helper.convertToByteArray(signatureData);
    }

    /**
     * Add signature to ith input
     * @param signature Signature
     * @param index Index of the a input
     */
    public void addSignature(byte[] signature, int index) {

        this.inputs.get(index).addSignature(signature);
    }

    /**
     * Get raw data of transaction
     * @return Raw data in form of bytes<
     */
    public byte[] getRawData() {

        ArrayList<Byte> rawData = new ArrayList<>();

        this.inputs.forEach(input -> rawData.addAll(input.getRawDataWithSignature()));
        this.outputs.forEach(output -> rawData.addAll(output.getRawData()));

        return Helper.convertToByteArray(rawData);
    }

    /**
     * Finish transaction by computing the hash
     */
    public void finish() {

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update(this.getRawData());

            this.hash = messageDigest.digest();

        } catch (NoSuchAlgorithmException exception) {

            exception.printStackTrace(System.err);
        }
    }


    public void setHash(byte[] hash) {

        this.hash = hash;
    }

    public byte[] getHash() {
        return hash;
    }

    public ArrayList<Input> getInputs() {
        return inputs;
    }

    public ArrayList<Output> getOutputs() {
        return outputs;
    }

    public Input getInput(int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return null;
    }

    public Output getOutput(int index) {
        if (index < outputs.size()) {
            return outputs.get(index);
        }
        return null;
    }

    public int numberOfInputs() {
        return inputs.size();
    }

    public int numberOfOutputs() {
        return outputs.size();
    }
}
