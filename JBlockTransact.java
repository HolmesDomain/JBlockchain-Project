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
}

