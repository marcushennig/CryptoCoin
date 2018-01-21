package crypto.transaction;

import crypto.shared.Helper;
import crypto.transaction.Transaction;
import crypto.transaction.UTXOPool;
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class TxHandler {

    /**
     * Pool of all unspent transaction outputs (value --> address), which is essentially a list
     * of all value hold by the addresses the value was sent to.
     */
    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {

        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code transaction} are in the current UTXO pool,
     * (2) the signatures on each input of {@code transaction} are valid,
     * (3) no UTXO is claimed multiple times by {@code transaction},
     * (4) all of {@code transaction}s output values are non-negative, and
     * (5) the sum of {@code transaction}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTransaction(Transaction tx) {

        // (1) All outputs claimed by transaction are in the current UTXO pool:
        // All output claimed as inputs for transaction are
        // unspent transaction outputs (UTXOs). In other words,
        // I can only spend stuff, that wasn't spent before
        ArrayList<UTXO> claimedUtxos = new ArrayList<>();

        for (Input input: tx.getInputs()) {

            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if(this.utxoPool.contains(utxo)) {
                claimedUtxos.add(utxo);
            } else {
                return false;
            }
        }

        // (2) The signature on each input of {@code transaction} is valid,
        for (Input input: tx.getInputs()) {

            // UTXO that the user has control of and that was used as
            // input for the transaction
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

            // The corresponding output of a previous transaction
            // that generated the UTXO
            Output output = this.utxoPool.getTxOutput(utxo);

            byte[] signature = input.signature;
            PublicKey publicKey = output.address;
            ArrayList<Byte> message = input.getRawDataWithoutSignature();

            if(!Helper.verifySignature(publicKey, Helper.convertToByteArray(message), signature)) {
                return false;
            }
        }

        // (3) No UTXO is claimed multiple times by {@code transaction},
        if (claimedUtxos.stream().distinct().count() < claimedUtxos.size()) {
            return false;
        }

        // (4) All of {@code transaction}s output values are non-negative, and
        if(tx.getOutputs().stream().anyMatch(output -> output.value < 0)) {
            return false;
        }

        // (5) the sum of {@code transaction}s input values is greater than or equal to the sum of its output
        //*     values;
        double totalOutput = tx.getOutputs()
                .stream()
                .mapToDouble(p -> p.value)
                .sum();

        double totalInput = claimedUtxos.stream()
                .map(utxo -> utxoPool.getTxOutput(utxo))
                .mapToDouble(p -> p.value)
                .sum();

        return !(totalOutput > totalInput);
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        return null;
    }

}
