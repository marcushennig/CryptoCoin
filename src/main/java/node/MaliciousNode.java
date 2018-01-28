package node;

import node.transaction.Candidate;
import node.transaction.Transaction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    private enum MaliciousType {

        DEAD_NODE,
        SEND_INVALID_TRANSACTIONS
    }
    private Random random = new Random(42);
    private MaliciousType maliciousType;


    public MaliciousNode(double pGraph, double pMalicious, double pTxDistribution, int numRounds) {
        this.maliciousType =  (random.nextDouble() > 0.5)? MaliciousType.DEAD_NODE
                                                         : MaliciousType.SEND_INVALID_TRANSACTIONS;
    }

    @Override
    public void setFollowees(boolean[] followees) {

    }

    @Override
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {

    }

    @Override
    public Set<Transaction> sendToFollowers() {

        if(this.maliciousType == MaliciousType.DEAD_NODE) {

            return new HashSet<>();

        } else if(this.maliciousType == MaliciousType.SEND_INVALID_TRANSACTIONS) {

            HashSet<Transaction> transactions = new HashSet<>();

            for (int i = 0; i < Math.max(0, random.nextInt(50)); i++) {

                transactions.add(new Transaction(random.nextInt()));
            }

            return transactions;

        } else {

            throw new NotImplementedException();
        }
    }

    @Override
    public void receiveFromFollowees(Set<Candidate> candidates) {
    }
}
