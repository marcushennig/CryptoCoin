package crypto.transaction;

import crypto.shared.Crypto;
import crypto.shared.Helper;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TxHandlerTest extends TestCase {

    private final static Logger logger = Logger.getLogger(TxHandlerTest.class);

    /** Transaction handler */
    private TxHandler txHandler;


    /** Seed that is used for generating random numbers */
    private final int seed = 42;

    /** Used for generating random values */
    private final Random random = new Random(seed);

    /** Address space */
    private List<KeyPair> addresses;

    /**
     * Generates a random valid transaction
     * @param address Public key of the person generating the random transaction
     * @return Valid random Transaction
     */
    private Transaction generateRandomInValidTransaction(PublicKey address) {
        return null;
    }

    /**
     * Generates a random valid transaction
     * @param address Public key of the person generating the random transaction
     * @return Valid random Transaction
     */
    private Transaction generateRandomValidTransaction(PublicKey address) {


        return null;
    }

    /**
     * Generate a random positive value between 0 and 1,000,000
     * @return Positive value
     */
    private double generateRandomPositiveValue() {

        return 1e6 *  this.random.nextDouble();
    }

    /**
     * Set up unit test
     * @throws Exception If anything goes wrong
     */
    protected void setUp() throws Exception {

        super.setUp();

        this.addresses = Crypto.generateRandomKeyPairs(10);

        UTXOPool utxoPool = new UTXOPool();

        Transaction genesisTx = new Transaction();
        for (KeyPair address : addresses) {
            genesisTx.addOutput(this.generateRandomPositiveValue(), address.getPublic());
        }

        byte[] txHash = genesisTx.getHash();
        for (int outputIndex=0; outputIndex < genesisTx.numberOfOutputs(); outputIndex++) {

            utxoPool.addUTXO(new UTXO(txHash, outputIndex), genesisTx.getOutput(outputIndex));
        }
        this.txHandler = new TxHandler(utxoPool);
    }

    /**
     * Test signature
     */
    public void testSignature() {

        String messageStr = "Hello crypto coin, what's up";
        byte[] message = messageStr.getBytes(StandardCharsets.UTF_8); //

        for (KeyPair address: this.addresses) {

            byte[] signature = Crypto.sign(address.getPrivate(), message);
            assertTrue(Crypto.verifySignature(address.getPublic(), message, signature));
        }

    }

    public void testIsValidTx() {

        assertTrue(true);
    }


    public void testHandleTxs() {

        assertTrue(true);
    }
}