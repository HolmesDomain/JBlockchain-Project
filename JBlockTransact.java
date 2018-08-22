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
	
	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
	
	if(verifiySignature() == false) {
		System.out.println("#Transaction Signature failed to verify");
		return false;
	}
			
	//gather transaction inputs (Make sure they are unspent):
	for(TransactionInput i : inputs) {
		i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
	}

	//check if transaction is valid:
	if(getInputsValue() < NoobChain.minimumTransaction) {
		System.out.println("#Transaction Inputs to small: " + getInputsValue());
		return false;
	}
	
	//generate transaction outputs:
	float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
	transactionId = calulateHash();
	outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
	outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
			
	//add outputs to Unspent list
	for(TransactionOutput o : outputs) {
		NoobChain.UTXOs.put(o.id , o);
	}
	
	//remove transaction inputs from UTXO lists as spent:
	for(TransactionInput i : inputs) {
		if(i.UTXO == null) continue; //if Transaction can't be found skip it
		NoobChain.UTXOs.remove(i.UTXO.id);
	}
	
	return true;
	}
	
	//returns sum of inputs(UTXOs) values
		public float getInputsValue() {
			float total = 0;
			for(TransactionInput i : inputs) {
				if(i.UTXO == null) continue; //if Transaction can't be found skip it 
				total += i.UTXO.value;
			}
			return total;
		}

	//returns sum of outputs:
		public float getOutputsValue() {
			float total = 0;
			for(TransactionOutput o : outputs) {
				total += o.value;
			}
			return total;
		}
	
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.transactionId);
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i = 1; i < previousTreeLayer.size();i++) {
				treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
	
}

