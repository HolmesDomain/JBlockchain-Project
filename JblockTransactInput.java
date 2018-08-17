import java.security.PublicKey;

public class JblockTransactInput {

	public String transactionOutputId;
	public TransactionOutput UTXO;
	
	public TransactionInput(String transactionOutputId) {
		
		this.transactionOutputId = transactionOutputId;
		
	}

}