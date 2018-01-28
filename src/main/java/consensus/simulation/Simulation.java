package consensus.simulation;

import crypto.transaction.TxHandler;
import consensus.nodes.CompliantNode;
import consensus.nodes.Node;
import consensus.nodes.MaliciousNode;
import consensus.transaction.Candidate;
import consensus.transaction.Transaction;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Example of a Simulation. This test runs the nodes on a random graph.
 * At the end, it will print out the Transaction ids which each node
 * believes consensus has been reached upon. You can use this simulation to
 * test your nodes. You will want to try creating some deviant nodes and
 * mixing them in the network to fully test.
 */
public class Simulation {

    final static Logger logger = Logger.getLogger(TxHandler.class);

    /** The number of nodes in the simulation */
    public int numNodes;

    /** The pairwise connectivity probability of the random graph */
    public double pGraph;

    /**  The Probability that a node will be set to be malicious*/
    public double pMalicious;

    /** The probability that each of the initial valid transactions will be communicated */
    public double pTxDistribution;

    /** Number of simulation rounds your nodes will run for*/
    public int numRounds;

    /**
     * Build the network simulation
     * @param numNodes Number of nodes in the simulated network (100)
     * @param pGraph Parameter for random graph: Probability that an edge will exist (0.1, 0.2, 0.3)
     * @param pMalicious Probability that a node will be set to be malicious (0.15, 0.30, 0.45)
     * @param pTxDistribution Probability of assigning an initial transaction to each node (10, 20)
     * @param numRounds Number of simulation rounds your nodes will run for
     */
    public Simulation(int numNodes,
                      double pGraph,
                      double pMalicious,
                      double pTxDistribution,
                      int numRounds) {

        this.numNodes = numNodes;
        this.pGraph = pGraph;
        this.pMalicious = pMalicious;
        this.pTxDistribution = pTxDistribution;
        this.numRounds = numRounds;
    }

    /**
     * pick which nodes are malicious and which are compliant
     * @return Array of nodes
     */
    private Node[] generateRandomNodes() {

        // pick which nodes are malicious and which are compliant
        Node[] nodes = new Node[numNodes];
        for (int i = 0; i < numNodes; i++) {

            if(Math.random() < pMalicious) {

                // TODO: When you are ready to try testing with malicious nodes, replace the
                // instantiation below with an instantiation of a MaliciousNode
                nodes[i] = new MaliciousNode(pGraph, pMalicious, pTxDistribution, numRounds);

            } else {

                nodes[i] = new CompliantNode(pGraph, pMalicious, pTxDistribution, numRounds);
            }
        }
        return nodes;
    }

    /**
     * Generate a random matrix of follows
     * @return Boolean matrix: followees[i][j] is true iff i follows j
     */
    private boolean[][] generateRandomFollowees() {

        boolean[][] followees = new boolean[numNodes][numNodes]; // followees[i][j] is true iff i follows j
        for (int i = 0; i < numNodes; i++) {

            for (int j = 0; j < numNodes; j++) {

                if (i == j) continue;

                if(Math.random() < pGraph) {

                    followees[i][j] = true;
                }
            }
        }
        return followees;
    }

    /**
     * Run the network simulation
     */
    public void run() {

        // Pick which nodes are malicious and which are compliant
        Node[] nodes = this.generateRandomNodes();

        // Initialize random follow graph
        boolean[][] followees = this.generateRandomFollowees();

        // Notify all nodes of their followees
        for (int i = 0; i < numNodes; i++) {

            nodes[i].setFollowees(followees[i]);
        }

        // Initialize a set of 500 valid Transactions with random ids
        int numTx = 500;
        HashSet<Integer> validTxIds = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < numTx; i++) {

            int r = random.nextInt();
            validTxIds.add(r);
        }

        // Distribute the 500 Transactions throughout the nodes, to initialize
        // the starting state of Transactions each node has heard. The distribution
        // is random with probability pTxDistribution for each Transaction-Node pair.
        for (int i = 0; i < numNodes; i++) {

            HashSet<Transaction> pendingTransactions = new HashSet<>();

            for(Integer txID : validTxIds) {

                if (Math.random() < pTxDistribution) {

                    pendingTransactions.add(new Transaction(txID));
                }
            }
            nodes[i].setPendingTransaction(pendingTransactions);
        }

        // Simulate for numRounds times
        for (int round = 0; round < numRounds; round++) { // numRounds is either 10 or 20

            // Gather all the proposals into a map. The key is the index of the node receiving
            // proposals. The value is an ArrayList containing 1x2 Integer arrays. The first
            // element of each array is the id of the transaction being proposed and the second
            // element is the index # of the node proposing the transaction.
            HashMap<Integer, Set<Candidate>> allProposals = new HashMap<>();

            for (int i = 0; i < numNodes; i++) {

                Set<Transaction> proposals = nodes[i].sendToFollowers();

                for (Transaction tx : proposals) {

                    if (!validTxIds.contains(tx.getId())) {
                        continue; // ensure that each tx is actually valid
                    }

                    for (int j = 0; j < numNodes; j++) {

                        if(!followees[j][i]) continue; // tx only matters if j follows i

                        if (!allProposals.containsKey(j)) {
                          Set<Candidate> candidates = new HashSet<>();
                          allProposals.put(j, candidates);
                        }

                        Candidate candidate = new Candidate(tx, i);
                        allProposals.get(j).add(candidate);
                    }
                }
            }

            // Distribute the Proposals to their intended recipients as Candidates
            for (int i = 0; i < numNodes; i++) {

                if (allProposals.containsKey(i)) {
                    nodes[i].receiveFromFollowees(allProposals.get(i));
                }
            }
        }

        // print results
        for (int i = 0; i < numNodes; i++) {

            Set<Transaction> transactions = nodes[i].sendToFollowers();
            System.out.println("Transaction ids that Node " + i + " believes consensus on:");

            for (Transaction tx : transactions) {
                System.out.println(tx.getId());
            }
            System.out.println();
            System.out.println();
        }
    }
}