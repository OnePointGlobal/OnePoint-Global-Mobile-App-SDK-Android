//
// Translated by CS2J (http://www.cs2j.com): 03/07/2013 12:06:59
//

package com.opg.sdk;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.opg.sdk.OPGSDKConstant.AES;
import static com.opg.sdk.OPGSDKConstant.CIPHER_KEY;
import static com.opg.sdk.OPGSDKConstant.EMPTY_STRING;
import static com.opg.sdk.OPGSDKConstant.HEIGHT_EQUAL;
import static com.opg.sdk.OPGSDKConstant.MD5;
import static com.opg.sdk.OPGSDKConstant.MEDIA_ID_EQUAL;
import static com.opg.sdk.OPGSDKConstant.MEDIA_TYPE_EQUAL;
import static com.opg.sdk.OPGSDKConstant.METHOD_GET_MEDIA_SESSION_ID;
import static com.opg.sdk.OPGSDKConstant.UTF_16LE;
import static com.opg.sdk.OPGSDKConstant.UTF_8;
import static com.opg.sdk.OPGSDKConstant.WIDTH_100_HEIGHT_100;
import static com.opg.sdk.OPGSDKConstant.WIDTH_EQUAL;
import static com.opg.sdk.OPGSDKConstant.ZERO;

///////////////////////////////////////////////////////////////////////////////
//
//Copyright (c) 2016 OnePoint Global Ltd. All rights reserved.
//
//This code is licensed under the OnePoint Global License.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
///////////////////////////////////////////////////////////////////////////////

/**
* AES256 class uses the AES algorithm with a provided 256 bit key and a random
* 128 bit IV to meet PCI standards The IV is randomly generated before each
* encryption and encoded with the final encrypted string
*/
public class Aes256 {
       // Symmetric algorithm interface is used to store the AES service provider

    /**
        * Decrypts a string with AES algorithm
        *
        * @param secureText
        *            Encrypted string with IV prefix
        *
        * @return Decrypted string
        */
       public static String decrypt(String secureText) throws Exception {
               return decrypt(Base64ConvertedKey(), Base64ConvertedArray(secureText));
       }

       // Return decrypted bytes as a string
       /**
        * Encrypts a string with AES algorithm
        *
        * @param plainText
        *            String to encrypt
        *
        * @return Encrypted string with IV prefix
        */
       public static String encrypt(String plainText) throws Exception {
               return encrypt(Base64ConvertedKey(), getByteInLittleIndian(plainText));
       }

       /** Encryption Operation done inside here */
       @SuppressLint("TrulyRandom")
       public static String encrypt(byte[] keybytes, byte[] data) throws Exception {
               SecretKeySpec skeySpec = new SecretKeySpec(keybytes, AES);
               Cipher cipher = Cipher.getInstance(CIPHER_KEY);

               /** To get the Random 16 Byte IV */
               byte[] iv = generateIv();
               IvParameterSpec ivspec = new IvParameterSpec(iv);
               cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
               byte[] encrypted = cipher.doFinal(data);

               /** Appending IV before the encrypted */
               byte[] finalData = concatenateByteArrays(ivspec.getIV(), encrypted);

               /** Converting the byte array into the MyBase 64 */
               return Base64.encodeToString(finalData, 0);
       }

       /** Decryption Operation done inside here */
       public static String decrypt(byte[] keybytes, byte[] encrypted)
       throws Exception {
               SecretKeySpec skeySpec = new SecretKeySpec(keybytes, AES);
               byte[] temp = encrypted;
               byte[] getIv = new byte[16];
               byte[] cipherByte = new byte[temp.length - 16];

               /** Getting the 16 Byte Iv from the encrypted string */
               System.arraycopy(temp, 0, getIv, 0, getIv.length);

               /** Getting the encrypted Byte is to decrypt */
               System.arraycopy(temp, 16, cipherByte, 0, temp.length - 16);

               IvParameterSpec ivspec = new IvParameterSpec(getIv);
               Cipher cipher = Cipher.getInstance(CIPHER_KEY);
               cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
               byte[] decrypted = cipher.doFinal(cipherByte);

               /** returning the Unicode String */
               return new String(decrypted, UTF_16LE);
       }

       /** Returning the Random IV of 16 Byte */
       protected static byte[] generateIv() throws NoSuchAlgorithmException {
               Random ranGen = new SecureRandom();
               byte[] aesKey = new byte[16]; // 16 bytes = 128 bits
               ranGen.nextBytes(aesKey);
               return aesKey;
       }

       /** Appending IV before the Encrypted Data */
       protected static byte[] concatenateByteArrays(byte[] IV, byte[] encrypted) {
               byte[] result = new byte[IV.length + encrypted.length];
               System.arraycopy(IV, 0, result, 0, IV.length);
               System.arraycopy(encrypted, 0, result, IV.length, encrypted.length);
               return result;
       }

       public static byte[] getByteInLittleIndian(String litInd)
       throws UnsupportedEncodingException {
               return litInd.getBytes(UTF_16LE);
       }

       public static byte[] getByteInUTF8(String litInd)
       throws UnsupportedEncodingException {
               return litInd.getBytes(UTF_8);
       }

       public static String getStringInLittleIndian(byte[] litInd)
       throws UnsupportedEncodingException {
               return new String(litInd, UTF_16LE);
       }

       public static byte[] Base64ConvertedKey() {
               return Base64.decode(AESKey.getKey(), 0);
       }

       public static byte[] Base64ConvertedArray(String toConvert) {
               return Base64.decode(toConvert, 0);
       }

       public static String getMediaBundle(String sessionId, String mediaid,
                       String mediatype) throws Exception {
               return encryptedString(METHOD_GET_MEDIA_SESSION_ID + sessionId + MEDIA_ID_EQUAL + mediaid + MEDIA_TYPE_EQUAL + mediatype
                               + WIDTH_100_HEIGHT_100);
       }

       public static String getMediaBundleForImage(String sessionId,
                       String mediaid, String mediatype, int width, int hieght)
       throws Exception {
               return encryptedString(METHOD_GET_MEDIA_SESSION_ID + sessionId
                               + MEDIA_ID_EQUAL + mediaid + MEDIA_TYPE_EQUAL + mediatype + WIDTH_EQUAL
                               + width + HEIGHT_EQUAL+ hieght + EMPTY_STRING);
       }

       public static String encryptedString(String jsonasString) {
               String encrypted = null;
               try {
                       encrypted = encrypt(Base64ConvertedKey(),
                                       getByteInLittleIndian(jsonasString));
               } catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
               } catch (Exception e) {
                       e.printStackTrace();
               }
               return encrypted;
       }

       public static String convertintoMD5(String value) {
               MessageDigest m = null;
               try {
                       m = MessageDigest.getInstance(MD5);
               } catch (NoSuchAlgorithmException e) {
               }
               m.reset();
               m.update(value.getBytes());
               byte[] digest = m.digest();
               BigInteger bigInt = new BigInteger(1, digest);
               String hashtext = bigInt.toString(16);
               while (hashtext.length() < 32) {
                       hashtext = ZERO + hashtext;
               }
               return hashtext;
       }

       private static class AESKey{

               private final static String getKey(){
                        String KEY = "HiYNZFOI1S1biFnoiFFWZcPwWBnhxqhkQ1Ipyh2yG7U=";
                        return KEY;
               }

       }
}
