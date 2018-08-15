package Jblockchain;
import java.security.*;

public class Wallet {
	
	public PrivateKey privatekey;
	public PublicKey publicKey;
	
	public Wallet() {
		generateKeyPair();
	}
	
	public void generateKeyPair() {
		
		try {
			
			keyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
			
		} catch(Exception e) {
			
			throw new RunTimeException(e);
			
		}
	}
}

