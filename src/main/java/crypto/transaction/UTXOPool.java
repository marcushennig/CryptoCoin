package crypto.transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class UTXOPool {

    /**
     * The current collection of UTXOs, with each one mapped to its corresponding transaction output
     */
    private HashMap<UTXO, TransactionOutput> H;

    /**
     * Creates a new empty UTXOPool
     */
    public UTXOPool() {

        H = new HashMap<>();
    }

    /**
     * Creates a new UTXOPool that is a copy of {@code other}
     * @param other The pool to be copied
     */
    public UTXOPool(UTXOPool other) {

        H = new HashMap<>(other.H);
    }

    /**
     * Adds a mapping from UTXO {@code utxo} to transaction output @code{txOut} to the pool
     * @param utxo unspent transaction output
     * @param txOut transaction output
     */
    public void addUTXO(UTXO utxo, TransactionOutput txOut) {

        H.put(utxo, txOut);
    }

    /**
     * Removes the UTXO {@code utxo} from the pool
     * @param utxo UTXO
     */
    public void removeUTXO(UTXO utxo) {

        H.remove(utxo);
    }

    /**
     * @return the transaction output corresponding to UTXO {@code utxo}, or null if {@code utxo} is
     *         not in the pool.
     */
    public TransactionOutput getTxOutput(UTXO utxo) {

        return H.get(utxo);
    }

    /**
     * @return true if UTXO {@code utxo} is in the pool and false otherwise
     */
    public boolean contains(UTXO utxo) {

        return H.containsKey(utxo);
    }

    /**
     * Returns an {@code ArrayList} of all UTXOs in the pool
     */
    public ArrayList<UTXO> getAllUTXO() {

        return new ArrayList<>(H.keySet());
    }
}
