package node.transaction;

public class Candidate {

    private Transaction tx;
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