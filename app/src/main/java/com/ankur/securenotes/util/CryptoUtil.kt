package com.ankur.securenotes.util

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object CryptoUtil {

  enum class IVSize {
    SIZE_12_BYTES, SIZE_16_BYTES;

    fun getSize(): Int {
      return when (this) {
        SIZE_12_BYTES -> 12
        SIZE_16_BYTES -> 16
      }
    }
  }

  enum class KeySize {
    SIZE_128_BITS, SIZE_192_BITS, SIZE_256_BITS;

    fun getSize(): Int {
      return when (this) {
        SIZE_128_BITS -> 128
        SIZE_192_BITS -> 192
        SIZE_256_BITS -> 256
      }
    }
  }

  enum class SaltLength {
    LEN_8_BYTES, LEN_16_BYTES, LEN_32_BYTES;

    fun getLength(): Int {
      return when (this) {
        LEN_8_BYTES -> 8
        LEN_16_BYTES -> 16
        LEN_32_BYTES -> 32
      }
    }
  }

  /*
  Gets an Initialization Vector of specified size*/
  fun getRandomNonce(numBytes: IVSize): ByteArray {
    val nonce = ByteArray(numBytes.getSize())
    SecureRandom().nextBytes(nonce)
    return nonce
  }

  fun getRandomNonce(numBytes: SaltLength): ByteArray {
    val nonce = ByteArray(numBytes.getLength())
    SecureRandom().nextBytes(nonce)
    return nonce
  }

  // AES secret key
  @Throws(NoSuchAlgorithmException::class)
  fun getAESKey(keySize: KeySize): SecretKey {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(keySize.getSize(), SecureRandom.getInstanceStrong())
    return keyGen.generateKey()
  }

  // Password derived AES 256 bits secret key
  @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
  fun getAESKeyFromPassword(password: CharArray, salt: ByteArray, keySize: KeySize): SecretKey {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")

    // iterationCount = 65536
    val spec: KeySpec = PBEKeySpec(password, salt, 65536, keySize.getSize())
    return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
  }

  // hex representation
  fun hex(bytes: ByteArray): String {
    val result = StringBuilder()
    for (b in bytes) {
      result.append(String.format("%02x", b))
    }
    return result.toString()
  }

  // print hex with block size split
  fun hexWithBlockSize(bytes: ByteArray, blockSize: Int): String {
    var blockSize = blockSize
    val hex = hex(bytes)

    // one hex = 2 chars
    blockSize *= 2

    // better idea how to print this?
    val result: MutableList<String> = ArrayList()
    var index = 0
    while (index < hex.length) {
      result.add(hex.substring(index, Math.min(index + blockSize, hex.length)))
      index += blockSize
    }
    return result.toString()
  }
}