package crypto.transaction;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
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

    /** Address size */
    private final int keySize = 1024;



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
     * Generate a given number of addresses (which consists of public/private key pairs)
      * @param numberOfAddresses Number of addresses to be generated
     */
    private List<KeyPair> generateRandomKeyPairs(int numberOfAddresses) {

        List<KeyPair> keyPairs = new ArrayList<>();
        try {

            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");

            // Set key length and the source of randomness
            // TODO: Create seed in byte[]
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.initialize(keySize, secureRandom);

            for (int n = 0; n < numberOfAddresses; n++) {

                KeyPair keyPair = keyGenerator.generateKeyPair();

                logger.info(String.format("Generated new Address: (%s, %s)",
                        DatatypeConverter.printHexBinary(keyPair.getPublic().getEncoded()),
                        DatatypeConverter.printHexBinary(keyPair.getPrivate().getEncoded())));

                keyPairs.add(keyPair);
            }
            return keyPairs;

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {

            logger.error(String.format("Could not create addresses as the following error occurred: %s",
                    e.getMessage()));

            e.printStackTrace();
            return keyPairs;
        }
    }

    /**
     * Set up unit test
     * @throws Exception If anything goes wrong
     */
    protected void setUp() throws Exception {

        super.setUp();

        UTXOPool utxoPool = new UTXOPool();

        this.addresses = this.generateRandomKeyPairs(100);

        this.txHandler = new TxHandler(utxoPool);
    }

    public void testIsValidTx() {

        assertTrue(true);
    }


    public void testHandleTxs() {

        assertTrue(true);
    }
}