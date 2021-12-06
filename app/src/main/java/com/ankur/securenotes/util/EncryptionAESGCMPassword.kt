package com.ankur.securenotes.util

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


object EncryptionAESGCMPassword {
  private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"
  private const val TAG_LENGTH_BIT = 128 // must be one of {128, 120, 112, 104, 96}
  private val IV_LENGTH_BYTE = CryptoUtil.IVSize.SIZE_12_BYTES
  private val SALT_LENGTH_BYTE = CryptoUtil.SaltLength.LEN_16_BYTES
  private val UTF_8 = StandardCharsets.UTF_8

  // return a base64 encoded AES encrypted text
  @Throws(Exception::class)
  fun encrypt(text: ByteArray?, password: String): String {

    // 16 bytes salt
    val salt: ByteArray = CryptoUtil.getRandomNonce(SALT_LENGTH_BYTE)

    // GCM recommended 12 bytes iv?
    val iv: ByteArray = CryptoUtil.getRandomNonce(IV_LENGTH_BYTE)

    // secret key from password
    val aesKeyFromPassword: SecretKey =
      CryptoUtil.getAESKeyFromPassword(password.toCharArray(), salt, CryptoUtil.KeySize.SIZE_256_BITS)
    val cipher = Cipher.getInstance(ENCRYPT_ALGO)

    // ASE-GCM needs GCMParameterSpec
    cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
    val cipherText = cipher.doFinal(text)

    // prefix IV and Salt to cipher text
    val cipherTextWithIvSalt =
      ByteBuffer.allocate(iv.size + salt.size + cipherText.size).put(iv).put(salt).put(cipherText).array()

    // string representation, base64, send this string to other for decryption.
    return Base64.getEncoder().encodeToString(cipherTextWithIvSalt)
  }

  // we need the same password, salt and iv to decrypt it
  @Throws(Exception::class)
  private fun decrypt(text: String, password: String): String {
    val decode = Base64.getDecoder().decode(text.toByteArray(UTF_8))

    // get back the iv and salt from the cipher text
    val bb = ByteBuffer.wrap(decode)
    val iv = ByteArray(IV_LENGTH_BYTE.getSize())
    bb[iv]
    val salt = ByteArray(SALT_LENGTH_BYTE.getLength())
    bb[salt]
    val cipherText = ByteArray(bb.remaining())
    bb[cipherText]

    // get back the aes key from the same password and salt
    val aesKeyFromPassword: SecretKey =
      CryptoUtil.getAESKeyFromPassword(password.toCharArray(), salt, CryptoUtil.KeySize.SIZE_256_BITS)
    val cipher = Cipher.getInstance(ENCRYPT_ALGO)
    cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
    val plainText = cipher.doFinal(cipherText)
    return String(plainText, UTF_8)
  }

  @Throws(Exception::class)
  @JvmStatic
  fun test() {
    val OUTPUT_FORMAT = "%-30s:%s"
    val PASSWORD = "this is a password"
    val pText = "AES-GSM Password-Bases encryption!"
    val encryptedTextBase64 = encrypt(pText.toByteArray(UTF_8), PASSWORD)
    println("\n------ AES GCM Password-based Encryption ------")
    println(String.format(OUTPUT_FORMAT, "Input (plain text)", pText))
    println(String.format(OUTPUT_FORMAT, "Encrypted (base64) ", encryptedTextBase64))
    println("\n------ AES GCM Password-based Decryption ------")
    println(String.format(OUTPUT_FORMAT, "Input (base64)", encryptedTextBase64))
    val decryptedText = decrypt(encryptedTextBase64, PASSWORD)
    println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText))
  }
}