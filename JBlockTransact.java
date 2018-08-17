//tutorial: https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce

package Jblockchain;

import java.security.*;
import java.util.ArrayList;

public class JBlockTransact {
	
	public String transactionId;
	public PublicKey sender;
	public PublicKey reciepient;
	public float value;
	public byte[] signature;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionInput> outputs = new ArrayList<TransactionInput>();
	
	private static int sequence = 0; // a rough count of how many transactions have been gen
	
	//Constructor
	public JBlockTransact(PublicKey from, Public to, float value, ArrayList<TransactionInput> inputs) {
		
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
		
	}
	
	//This calculates the transaction hash
	private String calculateHash() {
		
		sequence++;
		return StringUtil.applySha256(
			StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) +
			Float.toString(value) + sequence);
			
	}
	
	public static byte[] applyECDSASig(PrivateKey privateKey, PublicKey publicKey, String input) {
		
		Signature dsa;
		byte[] output = new byte[0];
		
		try {
			
			dsa = Signature.getInstance("ECDSA","BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);			
		}
		return output;
	}
	
	//Verifies a String signature
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		
		try {
			
			Signature ecdsaVerify = Signature.getInstance("ECDSA","BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
			
		} catch(Exception e) {
			
			throw new RuntimeException(e);
			
		}
		
	}
	
	public static String getStringFromKey(Key key) {
		
		return Base64.getEncoder().encodeToString(key.getEncoded());
		
	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		
	String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
	signature = StringUtil.applyECDSASig(privateKey,data);	
	
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		
	String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
	return StringUtil.verifyECDSASig(sender, data, signature);
	
	}
	
}

