package node;

import node.transaction.Candidate;
import node.transaction.Transaction;

import java.util.Set;
import java.util.HashSet;

/**
 * Malicious nodes may have arbitrary behavior. For instance, among other
 * things, a malicious node might:
 *  [.] be functionally dead and never actually broadcast any transactions.
 *  [.] constantly broadcasts its own set of transactions and never accept transactions given to it.
 *  [.] change behavior between rounds to avoid detection.
 */
public class MaliciousNode implements Node {

    public MaliciousNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
    }

    public void setFollowees(boolean[] followees) {
        return;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        return;
    }

    public Set<Transaction> sendToFollowers() {
        return new HashSet<Transaction>();
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {

        return;
    }
}
