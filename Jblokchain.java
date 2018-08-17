// author: Andrew Holmes
// dev: Simplified blockchain in Java

import java.util.Date;
import java.security.MessageDigest;

/**
Test Implementation

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutputs> UTXOs = new HashMap<String,TransactionOutputs>(); //list of all unspent transactions. 
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	public static void main(String[] args) {

		blockchain.add(new Block("I'm the first block babyyy", "0"));
			
		blockchain.add(new Block("Yo im the second block",genesisBlock.hash));
		
		blockchain.add(new Block("I'm the third block babyyy", "secondBlock.hash"));
			
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		System.out.println(blockchainJson);	
	}

	public static Boolean isChainValid() {
	
	Block currentBlock; 
	Block previousBlock;
	
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
}
**/



public class Block {

	public String hash;
	public String previousHash;
	private String data; //meta data
	private long timeStamp; //as number of milliseconds since 1/1/1970
	
	//Block constructor
	public Block(String data, String previousHash) {
		
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash(); // do this last
		
	}
	
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + data );
		return calculatedhash;
	}
	
	public void mineBlock(int difficulty) {	
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
}

public class StringUtil {
	
	public static String applySha256(String input) {
		try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(input.getBytes("UTF-8"));
				StringBuffer hexString = new StringBuffer(); // contain hash
				
				for(int i = 0; i < hash.length; i++) {
					String hex = Integer.toHexString(0xff & hash[i]);
					
					if(hex.length() == 1) hexString.append('0');
					hexString.append(hex);
				}
				
				return hexString.toString();
				
		}
		
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
