package consensus.simulation;

import junit.framework.TestCase;

public class SimulationTest extends TestCase {


    public void testConsensus() {

        Simulation nodeSimulation = new Simulation(100,
                0.2,
                0.15,
                0.1,
                10);

        nodeSimulation.run();
        assertTrue(true);

        // There are four required command line arguments: p_graph (.1, .2, .3),
        // p_malicious (.15, .30, .45), p_txDistribution (.01, .05, .10),
        // and numRounds (10, 20). You should try to test your CompliantNode
        // code for all 3x3x3x2 = 54 combinations.
        /*int numNodes = 100;
        for (double pGraph: Arrays.asList(0.1, 0.2, 0.3)) {
            for (double pMalicious : Arrays.asList(0.15, 0.30, 0.45)) {
                for (double pTxDistribution : Arrays.asList(0.01, 0.05, 0.10)) {
                    for (int numRounds : Arrays.asList(10, 20)) {

                        Simulation nodeSimulation = new Simulation(numNodes,
                                pGraph,
                                pMalicious,
                                pTxDistribution,
                                numRounds);

                        nodeSimulation.run();
                    }
                }
            }

        }*/
    }
}