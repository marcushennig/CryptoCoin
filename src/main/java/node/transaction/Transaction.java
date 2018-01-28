package node.transaction;

import lombok.Data;

/**
 * The Transaction class, a transaction being merely a wrapper around a unique identifier
 * (i.e., the validity and semantics of transactions are irrelevant to this assignment)
 */
@Data
final public class Transaction {

    private final int id;

    /**
     * Build transaction
     * @param id Unique ID of the transaction
     */
    public Transaction(int id) {

        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;

        return this.id == other.id;
    }

    @Override
    public int hashCode() {

        return id;
    }
}