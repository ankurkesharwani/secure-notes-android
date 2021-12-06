package com.ankur.securenotes.util

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


object EncryptionAESGCM {
  private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"
  private const val TAG_LENGTH_BIT = 128
  private val IV_LENGTH_BYTE = CryptoUtil.IVSize.SIZE_12_BYTES
  private val AES_KEY_BIT = CryptoUtil.KeySize.SIZE_256_BITS
  private val UTF_8 = StandardCharsets.UTF_8

  @Throws(Exception::class)
  fun encrypt(text: ByteArray, secret: SecretKey, iv: ByteArray): ByteArray {
    val cipher = Cipher.getInstance(ENCRYPT_ALGO)
    cipher.init(Cipher.ENCRYPT_MODE, secret, GCMParameterSpec(TAG_LENGTH_BIT, iv))
    return cipher.doFinal(text)
  }

  @Throws(Exception::class)
  fun encryptWithPrefixIV(text: ByteArray, secret: SecretKey, iv: ByteArray): ByteArray {
    val cipherText = encrypt(text, secret, iv)
    return ByteBuffer.allocate(iv.size + cipherText.size).put(iv).put(cipherText).array()
  }

  @Throws(Exception::class)
  fun decrypt(text: ByteArray, secret: SecretKey, iv: ByteArray): String {
    val cipher = Cipher.getInstance(ENCRYPT_ALGO)
    cipher.init(Cipher.DECRYPT_MODE, secret, GCMParameterSpec(TAG_LENGTH_BIT, iv))
    val plainText = cipher.doFinal(text)
    return String(plainText, UTF_8)
  }

  @Throws(Exception::class)
  fun decryptWithPrefixIV(text: ByteArray, secret: SecretKey): String {
    val bb = ByteBuffer.wrap(text)
    val iv = ByteArray(IV_LENGTH_BYTE.getSize())
    bb[iv] //bb.get(iv, 0, iv.length);
    val cipherText = ByteArray(bb.remaining())
    bb[cipherText]
    return decrypt(cipherText, secret, iv)
  }

  @Throws(Exception::class)
  @JvmStatic
  fun test() {
    val OUTPUT_FORMAT = "%-30s:%s"
    val pText = "Hello World AES-GCM, Welcome to Cryptography!"

    // encrypt and decrypt need the same key.
    // get AES 256 bits (32 bytes) key
    val secretKey: SecretKey = CryptoUtil.getAESKey(AES_KEY_BIT)

    // encrypt and decrypt need the same IV.
    // AES-GCM needs IV 96-bit (12 bytes)
    val iv: ByteArray = CryptoUtil.getRandomNonce(IV_LENGTH_BYTE)
    val encryptedText = encryptWithPrefixIV(pText.toByteArray(UTF_8), secretKey, iv)
    println("\n------ AES GCM Encryption ------")
    println(String.format(OUTPUT_FORMAT, "Input (plain text)", pText))
    println(java.lang.String.format(OUTPUT_FORMAT, "Key (hex)", CryptoUtil.hex(secretKey.encoded)))
    println(java.lang.String.format(OUTPUT_FORMAT, "IV  (hex)", CryptoUtil.hex(iv)))
    println(java.lang.String.format(OUTPUT_FORMAT, "Encrypted (hex) ", CryptoUtil.hex(encryptedText)))
    println(
      java.lang.String.format(
        OUTPUT_FORMAT, "Encrypted (hex) (block = 16)", CryptoUtil.hexWithBlockSize(encryptedText, 16)
      )
    )
    println("\n------ AES GCM Decryption ------")
    println(java.lang.String.format(OUTPUT_FORMAT, "Input (hex)", CryptoUtil.hex(encryptedText)))
    println(
      java.lang.String.format(
        OUTPUT_FORMAT, "Input (hex) (block = 16)", CryptoUtil.hexWithBlockSize(encryptedText, 16)
      )
    )
    println(java.lang.String.format(OUTPUT_FORMAT, "Key (hex)", CryptoUtil.hex(secretKey.encoded)))
    val decryptedText = decryptWithPrefixIV(encryptedText, secretKey)
    println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText))
  }
}