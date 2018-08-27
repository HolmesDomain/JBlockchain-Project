//source: https://freestartupkits.com/articles/technology/cryptocurrency-news-and-tips/ultimate-rust-blockchain-tutorial/ 
const HASH_BYTE_SIZE: usize = 32;

pub type Sha256Hash = [u8; HASH_BYTE_SIZE];

#[derive(Debug)]
pub struct Block {
	//Headers
	timestamp: i64,
	prev_block_hash: Sha256Hash,
	
	//Body
	//contain data
	data: Vec<u8>,
}

use chrono::prelude::*;

impl Block {
    pub fn new(data: &str, prev_hash: Sha256Hash) -> Result<Self, MiningError> {
        let mut s = Self {
            prev_block_hash: prev_hash,
            nonce: 0,
            data: data.to_owned().into(),
        };

        s.try_hash()
            .ok_or(MiningError::Iteration)
            .and_then(|nonce| {
                s.nonce = nonce;

                Ok(s)
            })
    }
}