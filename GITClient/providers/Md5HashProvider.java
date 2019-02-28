package providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5HashProvider{
    public static String hash(File file) throws ProviderException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ProviderException(e.getMessage(), e.getCause());
        }
        String result;
        byte[] buffer = new byte[102400];
        int read = 0;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new ProviderException(e.getMessage(), e.getCause());
        }

        byte[] digest = md.digest();
        result = convertByteArrayToHexString(digest);
        return result;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}


