extern crate crypto;
use crypto::digest::Digest;
use crypto::sha2::Sha256;
//source: https://freestartupkits.com/articles/technology/cryptocurrency-news-and-tips/ultimate-rust-blockchain-tutorial/
use num_bigint::BigUint;
use num_traits::One;

const DIFFICULTY usize = 5;
const MAX_NONCE: u64 = 1_000_000;

impl Block {
  fn try_hash(&self) -> Option<u64> {
      // The target is a number we compare the hash to. It is a 256bit binary with DIFFICULTY
      // leading zeroes.
      let target = BigUint::one() << (256 - 4 * DIFFICULTY);

      for nonce in 0..MAX_NONCE {
          let hash = calculate_hash(&block, nonce);
          let hash_int = BigUint::from_bytes_be(&hash);

          if hash_int < target {
              return Some(nonce);
          }
      }

      None
  }

  pub fn calculate_hash(block: &Block, nonce: u64) -> Sha256Hash {
      let mut headers = block.headers();
      headers.extend_from_slice(convert_u64_to_u8_array(nonce));

      let mut hasher = Sha256::new();
      hasher.input(&headers);
      let mut hash = Sha256Hash::default();

      hasher.result(&mut hash);

      hash
  }

  pub fn headers(&self) -> Vec<u8> {
      let mut vec = Vec::new();

      vec.extend(&util::convert_u64_to_u8_array(self.timestamp as u64));
      vec.extend_from_slice(&self.prev_block_hash);

      vec
  }

}

// This transforms a u64 into a little endian array of u8
pub fn convert_u64_to_u8_array(val: u64) -> [u8; 8] {
    return [
        val as u8,
        (val >> 8) as u8,
        (val >> 16) as u8,
        (val >> 24) as u8,
        (val >> 32) as u8,
        (val >> 40) as u8,
        (val >> 48) as u8,
        (val >> 56) as u8,
    ]
}