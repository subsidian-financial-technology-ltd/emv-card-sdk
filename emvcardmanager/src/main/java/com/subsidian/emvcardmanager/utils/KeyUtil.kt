package com.subsidian.emvcardmanager.utils

import android.content.Context
import com.subsidian.emvcardmanager.utils.ByteUtil.hexToBytes
import com.subsidian.emvcardmanager.utils.ByteUtil.bytes2HexString
import com.subsidian.emvcardmanager.utils.StringUtil.isEmpty
import com.subsidian.emvcardmanager.utils.ByteUtil.hexString2Bytes
import kotlin.Throws
import android.os.Build
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import com.subsidian.emvcardmanager.exceptions.KeyException
import com.subsidian.emvcardmanager.utils.ByteUtil
import com.subsidian.emvcardmanager.utils.StringUtil
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigInteger
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal
import kotlin.experimental.and

class KeyUtil {

    private val RSA_KEY_ALIAS = "httpsCert"
    private val SHA256 = "SHA-256"

    @Throws(KeyException::class)
    fun generateRSAKey(context: Context? = null) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val generator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
                generator.initialize(
                    KeyGenParameterSpec.Builder(
                        RSA_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                    )
                        .setDigests(
                            KeyProperties.DIGEST_SHA1,
                            KeyProperties.DIGEST_SHA224,
                            KeyProperties.DIGEST_SHA256,
                            KeyProperties.DIGEST_SHA384,
                            KeyProperties.DIGEST_SHA512
                        )
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
                )
                generator.generateKeyPair()
            } else {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 20)
                val spec = KeyPairGeneratorSpec.Builder(context!!)
                    .setAlias(RSA_KEY_ALIAS)
                    .setSubject(
                        X500Principal(
                            "CN=Arca Networks ," +
                                    " O=IT Dept" +
                                    " C=Nigeria"
                        )
                    )
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                val generator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
                generator.initialize(spec)
                generator.generateKeyPair()
            }
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException("Unable to generate RSA key in Key.generateRSAKey", e)
        } catch (e: NoSuchProviderException) {
            throw KeyException("Unable to generate RSA key in Key.generateRSAKey", e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw KeyException("Unable to generate RSA key in Key.generateRSAKey", e)
        }
    }

    @get:Throws(KeyException::class)
    val privateKey: PrivateKey?
        get() = try {
            val ks = KeyStore.getInstance("AndroidKeyStore")
            ks.load(null)
            val entry = ks.getEntry(RSA_KEY_ALIAS, null)
            if (entry !is KeyStore.PrivateKeyEntry) {
                null
            } else entry.privateKey
        } catch (e: KeyStoreException) {
            throw KeyException("Unable to get Private key in Key.getPrivateKey", e)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException("Unable to get Private key in Key.getPrivateKey", e)
        } catch (e: IOException) {
            throw KeyException("Unable to get Private key in Key.getPrivateKey", e)
        } catch (e: CertificateException) {
            throw KeyException("Unable to get Private key in Key.getPrivateKey", e)
        } catch (e: UnrecoverableEntryException) {
            throw KeyException("Unable to get Private key in Key.getPrivateKey", e)
        }

    // Get certificate of public key
    @get:Throws(KeyException::class)
    val publicKey: PublicKey
    // Get public key
    ?
        get() = try {
            val ks =
                KeyStore.getInstance("AndroidKeyStore")
            ks.load(null)
            val entry = ks.getEntry(RSA_KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry
            // Get certificate of public key
            val cert = ks.getCertificate(RSA_KEY_ALIAS)
            // Get public key
            cert.publicKey
        } catch (e: KeyStoreException) {
            throw KeyException(
                "Unable to get Public key in Key.getPublicKey.",
                e
            )
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException(
                "Unable to get Public key in Key.getPublicKey.",
                e
            )
        } catch (e: IOException) {
            throw KeyException(
                "Unable to get Public key in Key.getPublicKey.",
                e
            )
        } catch (e: CertificateException) {
            throw KeyException(
                "Unable to get Public key in Key.getPublicKey.",
                e
            )
        } catch (e: UnrecoverableEntryException) {
            throw KeyException(
                "Unable to get Public key in Key.getPublicKey.",
                e
            )
        }

    //                Log.w(TAG, "Not an instance of a PrivateKeyEntry");
    // Get certificate of public key
    @get:Throws(KeyException::class)
    val base64PublicKey: String
    // Get public key
    ?
        get() = try {
            val ks =
                KeyStore.getInstance("AndroidKeyStore")
            ks.load(null)
            val entry =
                ks.getEntry(RSA_KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry//                Log.w(TAG, "Not an instance of a PrivateKeyEntry");
            // Get certificate of public key
            val cert = ks.getCertificate(RSA_KEY_ALIAS)
            // Get public key
            val publicKey = cert.publicKey
            val encodedPublickey = publicKey.encoded
            Base64.encodeToString(encodedPublickey, Base64.NO_WRAP)
        } catch (e: KeyStoreException) {
            throw KeyException(
                "Unable to get Public key in Key.getBase64PublicKey.",
                e
            )
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException(
                "Unable to get Public key in Key.getBase64PublicKey.",
                e
            )
        } catch (e: IOException) {
            throw KeyException(
                "Unable to get Public key in Key.getBase64PublicKey.",
                e
            )
        } catch (e: CertificateException) {
            throw KeyException(
                "Unable to get Public key in Key.getBase64PublicKey.",
                e
            )
        } catch (e: UnrecoverableEntryException) {
            throw KeyException(
                "Unable to get Public key in Key.getBase64PublicKey.",
                e
            )
        }

    @Throws(KeyException::class)
    fun encryptWithDES(key: String?, cipherHex: String?): String {
        return try {
            // create a binary key from the argument key (seed)
            val tmp = hex2bin(key)
            val keyBytes = ByteArray(24)
            System.arraycopy(tmp, 0, keyBytes, 0, 16)
            System.arraycopy(tmp, 0, keyBytes, 16, 8)
            val sk: SecretKey = SecretKeySpec(keyBytes, "DESede")
            // create an instance of cipher
            val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, sk)
            // enctypt!
            val encrypted = cipher.doFinal(hex2bin(cipherHex))
            bin2hex(encrypted)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException("Unable to encrypt with DES key in Keyservice.encryptWithDES.", e)
        } catch (e: NoSuchPaddingException) {
            throw KeyException("Unable to encrypt with DES key in Keyservice.encryptWithDES.", e)
        } catch (e: InvalidKeyException) {
            throw KeyException("Unable to encrypt with DES key in Keyservice.encryptWithDES.", e)
        } catch (e: IllegalBlockSizeException) {
            throw KeyException("Unable to encrypt with DES key in Keyservice.encryptWithDES.", e)
        } catch (e: BadPaddingException) {
            throw KeyException("Unable to encrypt with DES key in Keyservice.encryptWithDES.", e)
        }
    }

    @Throws(KeyException::class)
    fun decryptWithDES(key: String?, encryptedHex: String?): String {
        return try {
            // create a binary key from the argument key (seed)
            val tmp = hex2bin(key)
            val keyBytes = ByteArray(24)
            System.arraycopy(tmp, 0, keyBytes, 0, 16)
            System.arraycopy(tmp, 0, keyBytes, 16, 8)
            val sk: SecretKey = SecretKeySpec(keyBytes, "DESede")

            // do the decryption with that key
            val cipher = Cipher.getInstance("DESede/ECB/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, sk)
            val decrypted = cipher.doFinal(hex2bin(encryptedHex))
            bin2hex(decrypted)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException("Unable to decrypt with DES key in Keyservice.decryptWithDES.", e)
        } catch (e: NoSuchPaddingException) {
            throw KeyException("Unable to decrypt with DES key in Keyservice.decryptWithDES.", e)
        } catch (e: InvalidKeyException) {
            throw KeyException("Unable to decrypt with DES key in Keyservice.decryptWithDES.", e)
        } catch (e: IllegalBlockSizeException) {
            throw KeyException("Unable to decrypt with DES key in Keyservice.decryptWithDES.", e)
        } catch (e: BadPaddingException) {
            throw KeyException("Unable to decrypt with DES key in Keyservice.decryptWithDES.", e)
        }
    }

    @Throws(KeyException::class)
    fun encryptWithRSAReturnBase64String(cipherText: String?): String {
        var encodedBytes: ByteArray? = null
        return try {
            val publicKey = publicKey
            val cipher =
                Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            encodedBytes = cipher.doFinal(hexToBytes(cipherText!!))
            String(Base64.encode(encodedBytes, Base64.NO_WRAP))
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.encryptWithRSAReturnBase64String.",
                e
            )
        } catch (e: NoSuchPaddingException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.encryptWithRSAReturnBase64String.",
                e
            )
        } catch (e: InvalidKeyException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.encryptWithRSAReturnBase64String.",
                e
            )
        } catch (e: IllegalBlockSizeException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.encryptWithRSAReturnBase64String.",
                e
            )
        } catch (e: BadPaddingException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.encryptWithRSAReturnBase64String.",
                e
            )
        }
    }

    fun decryptBase64StringWithRSA(base64CipherText: String?): String {
        return bytes2HexString(decryptBase64StringWithRSAByte(base64CipherText))
    }

    @Throws(KeyException::class)
    fun decryptBase64StringWithRSAByte(base64CipherText: String?): ByteArray? {
        var decodedBytes: ByteArray? = null
        return try {
            val privateKeyEntry = privateKey
            val c = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            c.init(Cipher.DECRYPT_MODE, privateKeyEntry)
            decodedBytes = c.doFinal(Base64.decode(base64CipherText, Base64.NO_WRAP))
            decodedBytes
            //return new String(decodedBytes);
        } catch (bpex: BadPaddingException) {
            try {
                val privateKeyEntry = privateKey
                val c = Cipher.getInstance("RSA/None/NoPadding")
                c.init(Cipher.DECRYPT_MODE, privateKeyEntry)
                decodedBytes = c.doFinal(Base64.decode(base64CipherText, Base64.NO_WRAP))
                var newClearKey = bytes2HexString(decodedBytes)
                if (!isEmpty(newClearKey) && newClearKey.length > 32) {
                    newClearKey = newClearKey.substring(0, 32)
                }
                hexString2Bytes(newClearKey)
            } catch (ex: NoSuchAlgorithmException) {
                throw KeyException(
                    "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                    ex
                )
            } catch (ex: NoSuchPaddingException) {
                throw KeyException(
                    "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                    ex
                )
            } catch (ex: InvalidKeyException) {
                throw KeyException(
                    "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                    ex
                )
            } catch (ex: IllegalBlockSizeException) {
                throw KeyException(
                    "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                    ex
                )
            } catch (ex: BadPaddingException) {
                throw KeyException(
                    "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                    ex
                )
            }
        } catch (ex: NoSuchAlgorithmException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                ex
            )
        } catch (ex: NoSuchPaddingException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                ex
            )
        } catch (ex: InvalidKeyException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                ex
            )
        } catch (ex: IllegalBlockSizeException) {
            throw KeyException(
                "Unable to decrypt with RSA key in Key.decryptBase64StringWithRSAByte.",
                ex
            )
        }
    }

    @Throws(KeyException::class)
    fun encryptWithRSA(publicKeyBase64: String?, cipherText: String): String {
        var encodedBytes: ByteArray? = null
        return try {
            val publicKey = publicKey
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encryptedBytes = cipher.doFinal(cipherText.toByteArray())
            encodedBytes = Base64.encode(encryptedBytes, Base64.NO_WRAP)
            String(encodedBytes)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyException("Unable to decrypt with RSA key in Key.encryptWithRSA.", e)
        } catch (e: NoSuchPaddingException) {
            throw KeyException("Unable to decrypt with RSA key in Key.encryptWithRSA.", e)
        } catch (e: InvalidKeyException) {
            throw KeyException("Unable to decrypt with RSA key in Key.encryptWithRSA.", e)
        } catch (e: IllegalBlockSizeException) {
            throw KeyException("Unable to decrypt with RSA key in Key.encryptWithRSA.", e)
        } catch (e: BadPaddingException) {
            throw KeyException("Unable to decrypt with RSA key in Key.encryptWithRSA.", e)
        }
    }

    @Throws(Exception::class)
    fun getMac(seed: String?, macDataBytes: ByteArray): String {
        val keyBytes = hex2bin(seed)
        val digest = MessageDigest.getInstance(SHA256)
        digest.update(keyBytes, 0, keyBytes.size)
        digest.update(macDataBytes, 0, macDataBytes.size)
        val hashedBytes = digest.digest()
        var hashText = bin2hex(hashedBytes)
        hashText = hashText.replace(" ", "")
        if (hashText.length < 64) {
            val numberOfZeroes = 64 - hashText.length
            var zeroes = ""
            var temp = hashText
            for (i in 0 until numberOfZeroes) zeroes = zeroes + "0"
            temp = zeroes + temp
            return temp
        }
        return hashText
    }

    private fun toHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        val formatter = Formatter(sb)
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return sb.toString()
    }

    private fun hex2bin(hex: String?): ByteArray {
        if (hex == null) return ByteArray(0)
        require(hex.length and 0x01 != 0x01)
        val bytes = ByteArray(hex.length / 2)
        for (idx in bytes.indices) {
            val hi = Character.digit(hex[idx * 2].toInt(), 16)
            val lo = Character.digit(hex[idx * 2 + 1].toInt(), 16)
            require(!(hi < 0 || lo < 0))
            bytes[idx] = (hi shl 4 or lo).toByte()
        }
        return bytes
    }

    private fun bin2hex(bytes: ByteArray): String {
        val hex = CharArray(bytes.size * 2)
        for (idx in bytes.indices) {
            val hi: Int = (bytes[idx] and (0xF0).toByte()).toInt() ushr 4
            val lo: Int = (bytes[idx] and (0x0F).toByte()).toInt()
            hex[idx * 2] = (if (hi < 10) '0'.toInt() + hi else 'A'.toInt() - 10 + hi).toChar()
            hex[idx * 2 + 1] = (if (lo < 10) '0'.toInt() + lo else 'A'.toInt() - 10 + lo).toChar()
        }
        return String(hex)
    }

    @Throws(KeyException::class)
    fun privateKeyExists(): Boolean {
        return privateKey != null
    }
}