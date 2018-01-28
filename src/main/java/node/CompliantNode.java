package node;

import crypto.transaction.TxHandler;
import node.transaction.Candidate;
import node.transaction.Transaction;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CompliantNode refers to a node that follows the rules (not malicious)
 */
public class CompliantNode implements Node {

    private final static Logger logger = Logger.getLogger(CompliantNode.class);

    /** The number of nodes in the simulation */
    private int numNodes;

    /** The pairwise connectivity probability of the random graph */
    private double pGraph;

    /**  The Probability that a node will be set to be malicious*/
    private double pMalicious;

    /** The probability that each of the initial valid transactions will be communicated */
    private double pTxDistribution;

    /** Number of simulation rounds your nodes will run for*/
    private int numRounds;

    private boolean[] followees;
    private int numberOfFollowees;

    /** Proposals from each node */
    private HashMap<Transaction, Set<Integer>> proposals;

    /**
     * Build compliant node
     * @param pGraph Simulation parameter
     * @param pMalicious Simulation parameter
     * @param pTxDistribution Simulation parameter
     * @param numRounds Simulation parameter
     */
    public CompliantNode(double pGraph, double pMalicious, double pTxDistribution, int numRounds) {

        this.pGraph = pGraph;
        this.pMalicious = pMalicious;
        this.pTxDistribution = pTxDistribution;
        this.numNodes = numRounds;
        this.proposals = new HashMap<>();
    }

    /** {@code followees[i]} is true if and only if this node follows node {@code i} */
    public void setFollowees(boolean[] followees) {

        this.followees = followees;

        this.numberOfFollowees = 0;
        for (boolean followee : followees) {

            if (followee) this.numberOfFollowees++;
        }
    }

    /** Initialize proposal list of transactions */
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {

        Set<Integer> nodes = new HashSet<>();
        for (int node = 0; node < followees.length; node++) {

            if(followees[node]) {

                nodes.add(node);
            }
        }

        this.proposals.clear();

        for (Transaction tx: pendingTransactions) {

            this.proposals.put(tx, nodes);
        }
    }

    /**
     * Send valid transactions to followers
     * @return Set of transaction that was send to followers
     */
    public Set<Transaction> sendToFollowers() {

        Set<Transaction> consensus = new HashSet<>();
        // Push all transactions into the pending transactions that this node believes consensus on
        for (Transaction tx: this.proposals.keySet()) {


            Set<Integer> nodes = proposals.get(tx);
            double vote = ((double) nodes.size()) / this.numberOfFollowees;
            if(vote > 0.5) {

                consensus.add(tx);

            } else {

                logger.warn(String.format("Transaction %d has less then 50%% of the votes", tx.id));
            }
        }
        this.proposals.clear();

        return consensus;
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

    /**
     * Each node receives candidates from its followees
     * @param candidates Set of candidate transactions
     */
    public void receiveFromFollowees(Set<Candidate> candidates) {

        // For each candidate save the index of the node that sent it
        for (Candidate candidate: candidates) {

            // Node that sent the candidate transaction
            int sender = candidate.sender;
            Transaction tx = candidate.tx;

            if(!this.isTrustedNode(sender)){

                logger.warn(String.format("Node %d is no trusted as it is not in the list of followees", sender));
                continue;
            }

            if(this.proposals.containsKey(tx)) {

                this.proposals.get(tx).add(sender);

            } else {

                this.proposals.put(tx, new HashSet<>(Collections.singletonList(sender)));
            }
        }
    }
}
