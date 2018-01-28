package node.transaction;

import lombok.Data;

/**
 * A simple class to describe candidate transactions
 * a node receives
 */
@Data
public class Candidate {

    /** Transaction that is a possible candidate for consensus */
    private Transaction tx;

	/** Index of the node that send the transaction */
    private int sender;

    /**
     * Build a new candidate for transaction
     * @param tx Transaction
     * @param sender Index of the sender
     */
	public Candidate(Transaction tx, int sender) {

	    this.tx = tx;
		this.sender = sender;
	}
}