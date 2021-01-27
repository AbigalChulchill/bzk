package net.bzk.infrastructure;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

/**
 * Example showing the CryptoCipher API using a ByteBuffer
 */

public class AESUtils {
	
	private static final Logger log = LoggerFactory.getLogger(AESUtils.class.getSimpleName());
	public static final int bufferSize = 256;
	
    //32�???度�?��???，�?�就?��256�?
    //static final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("12345678901234561234567890123456"), "AES");
    //?���?16�???�?
    static final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
    //??��?�方�?/??��?�模�?/填�?�方式�?�CBC?��安全?�好于ECB,?��?????�?????????,?��SSL?�IPSec???????
    static final String transform = "AES/CBC/PKCS5Padding";

    public static SecretKeySpec key(String pass) {
    	return new SecretKeySpec(getUTF8Bytes(pass), "AES");
    }
    
    /**
     * Converts String to UTF8 bytes
     *
     * @param input the input string
     * @return UTF8 bytes
     */
    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Converts ByteBuffer to String
     *
     * @param buffer input byte buffer
     * @return the converted string
     */
    private static String asString(ByteBuffer buffer) {
        final ByteBuffer copy = buffer.duplicate();
        final byte[] bytes = new byte[copy.remaining()];
        copy.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * ??��??
     * @param text ??要�?��?��?��?��??
     * @return ??base64??��?��?��?��?��??
     * @throws IOException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String pass,String text) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        Properties properties = new Properties();
        final ByteBuffer outBuffer;
        final int updateBytes;
        final int finalBytes;
        //Creates a CryptoCipher instance with the transformation and properties.
        try (CryptoCipher encipher = Utils.getCipherInstance(transform, properties)) {

            ByteBuffer inBuffer = ByteBuffer.allocateDirect(bufferSize);
            outBuffer = ByteBuffer.allocateDirect(bufferSize);
            inBuffer.put(getUTF8Bytes(text));

            inBuffer.flip(); // ready for the cipher to read it
            // Show the data is there
            log.debug("inBuffer={}", asString(inBuffer));

            // Initializes the cipher with ENCRYPT_MODE,key and iv.
            encipher.init(Cipher.ENCRYPT_MODE, key(pass), iv);

            // Continues a multiple-part encryption/decryption operation for byte buffer.
            updateBytes = encipher.update(inBuffer, outBuffer);
            log.debug("updateBytes={}", updateBytes);

            // We should call do final at the end of encryption/decryption.
            finalBytes = encipher.doFinal(inBuffer, outBuffer);
            log.debug("finalBytes={}", finalBytes);
        }

        outBuffer.flip(); // ready for use as decrypt
        byte[] encoded = new byte[updateBytes + finalBytes];
        outBuffer.duplicate().get(encoded);
        String encodedString = Base64Utils.encodeToString(encoded);
        log.debug("encodedString={}", encodedString);
        return encodedString;
    }

    /**
     * �?�?
     * @param encodedString ??base64??��?��?��?��?��??
     * @return ??��??
     * @throws IOException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws ShortBufferException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String dencrypt(String pass,String encodedString) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        Properties properties = new Properties();
        final ByteBuffer outBuffer;
        ByteBuffer decoded = ByteBuffer.allocateDirect(bufferSize);
        //Creates a CryptoCipher instance with the transformation and properties.
        try (CryptoCipher decipher = Utils.getCipherInstance(transform, properties)) {
            decipher.init(Cipher.DECRYPT_MODE, key(pass), iv);
            outBuffer = ByteBuffer.allocateDirect(bufferSize);
            outBuffer.put(Base64Utils.decode(getUTF8Bytes(encodedString)));
            outBuffer.flip();
            decipher.update(outBuffer, decoded);
            decipher.doFinal(outBuffer, decoded);
            decoded.flip(); // ready for use
            log.debug("decoded={}", asString(decoded));
        }

        return asString(decoded);
    }

}