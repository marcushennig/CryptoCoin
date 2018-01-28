package node.transaction;

/**
 * Simple class for transaction use more complicated own later
 */
final public class Transaction {

    public final int id;

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