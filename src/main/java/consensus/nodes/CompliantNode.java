package consensus.nodes;

import consensus.transaction.Candidate;
import consensus.transaction.Transaction;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * CompliantNode refers to a node that follows the rules (not malicious)
 */
public class CompliantNode implements Node {

    private final static Logger logger = Logger.getLogger(CompliantNode.class);

    /**  The Probability that a node will be set to be malicious*/
    private double pMalicious;

    /** The probability that each of the initial valid transactions will be communicated */
    private double pTxDistribution;

    private boolean[] followees;
    private int numberOfFollowees;
    private HashMap<Transaction, Set<Integer>> pendingTransactions;

    /**
     * Build compliant node
     * @param pGraph Simulation parameter
     * @param pMalicious Simulation parameter
     * @param pTxDistribution Simulation parameter
     * @param numRounds Simulation parameter
     */
    public CompliantNode(double pGraph, double pMalicious, double pTxDistribution, int numRounds) {

        this.pMalicious = pMalicious;
        this.pTxDistribution = pTxDistribution;
        this.pendingTransactions = new HashMap<>();
    }

    /** {@code followees[i]} is true if and only if this node follows node {@code i} */
    @Override
    public void setFollowees(boolean[] followees) {

        this.followees = followees;

        this.numberOfFollowees = 0;
        for (boolean followee : followees) {

            if (followee) this.numberOfFollowees++;
        }
    }

    /**
     * Check is node can be trusted
     * @param node Node index
     * @return True if node is trusted, otherwise false
     */
    private boolean isTrustedNode(int node) {

        return node <= this.followees.length &&
                this.followees[node];
    }

    /** Initialize proposal list of transactions */
    @Override
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {

        Set<Integer> nodes = new HashSet<>();
        for (int node = 0; node < followees.length; node++) {

            if(followees[node]) {

                nodes.add(node);
            }
        }
        this.pendingTransactions.clear();
        for (Transaction tx: pendingTransactions) {

            this.pendingTransactions.put(tx, nodes);
        }
    }

    /**
     * Send valid transactions to followers
     * @return Set of transaction that was send to followers
     */
    @Override
    public Set<Transaction> sendToFollowers() {

        Set<Transaction> consensus = new HashSet<>();
        // Push all transactions into the pending transactions that this node believes consensus on
        for (Transaction tx: this.pendingTransactions.keySet()) {

            int numberOfNodesProposingTransaction = pendingTransactions.get(tx).size();
            if(numberOfNodesProposingTransaction >= pTxDistribution * (1 - pMalicious) * numberOfFollowees) {

                consensus.add(tx);
            }
        }

        for (Transaction tx: consensus) {
            pendingTransactions.remove(tx);
        }
        return consensus;
    }

    /**
     * Each node receives candidates from its followees
     * @param candidates Set of candidate transactions
     */
    @Override
    public void receiveFromFollowees(Set<Candidate> candidates) {

        // For each candidate save the index of the node that sent it
        for (Candidate candidate: candidates) {

            // Node proposing the transaction
            int sender = candidate.getSender();
            // Transaction being proposed
            Transaction tx = candidate.getTx();

            if(!this.isTrustedNode(sender)){

                logger.warn(String.format("Node %d is no trusted as it is not in the list of followees", sender));
                continue;
            }

            if(this.pendingTransactions.containsKey(tx)) {

                this.pendingTransactions.get(tx).add(sender);

            } else {

                this.pendingTransactions.put(tx, new HashSet<>(Collections.singletonList(sender)));
            }
        }
    }
}
