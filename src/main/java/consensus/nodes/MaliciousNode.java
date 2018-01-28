package consensus.nodes;

import consensus.transaction.Candidate;
import consensus.transaction.Transaction;

import java.util.Random;
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

    private Random random = new Random(42);

    public MaliciousNode(double pGraph, double pMalicious, double pTxDistribution, int numRounds) {
    }

    @Override
    public void setFollowees(boolean[] followees) {

    }

    @Override
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {

    }

    @Override
    public Set<Transaction> sendToFollowers() {

        if(random.nextDouble() > 0.5) {

            return new HashSet<>();

        } else {

            HashSet<Transaction> transactions = new HashSet<>();

            for (int i = 0; i < Math.max(1, random.nextInt(50)); i++) {

                transactions.add(new Transaction(random.nextInt()));
            }
            return transactions;
        }
    }

    @Override
    public void receiveFromFollowees(Set<Candidate> candidates) {
    }
}
