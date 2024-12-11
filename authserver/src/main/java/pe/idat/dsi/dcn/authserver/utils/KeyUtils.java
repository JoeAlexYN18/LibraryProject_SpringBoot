package pe.idat.dsi.dcn.authserver.utils;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyUtils {
      public static SecretKey stringToSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public static String secretKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
