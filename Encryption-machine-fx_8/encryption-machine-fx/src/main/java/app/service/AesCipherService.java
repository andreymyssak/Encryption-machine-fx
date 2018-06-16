package app.service;

import app.objects.AesCipher;
import com.jfoenix.controls.JFXTextField;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AesCipherService {
    private static AesCipher aesCipher = new AesCipher();

    public static void initAesCipher(List<File> files, JFXTextField txtFieldUrlDirectory, String mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, DecoderException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesCipher.getInitVector());
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesCipher.getSecretKey().getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        if (mode.equals("DECRYPT_MODE")) {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            for (File file : files) {
                String baseName = FilenameUtils.getBaseName(file.getAbsolutePath()) + "." + FilenameUtils.getExtension(file.getAbsolutePath());
                String fileOut = txtFieldUrlDirectory.getText() + "/" + baseName;

                String encryptedTxt = readFile(file.getPath());
                System.out.println(encryptedTxt);

                writeFile(new String(cipher.doFinal(Base64.decodeBase64(encryptedTxt)), "UTF-8"), fileOut);
            }

        } else if (mode.equals("ENCRYPT_MODE")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            for (File file : files) {
                String baseName = FilenameUtils.getBaseName(file.getAbsolutePath()) + "." + FilenameUtils.getExtension(file.getAbsolutePath());
                String fileOut = txtFieldUrlDirectory.getText() + "/" + baseName;

                String text = readAndParseHtmlFile(file.getPath());
                writeFile(new String (Base64.encodeBase64(cipher.doFinal(text.getBytes("UTF-8")), false)), fileOut);
            }
        }
    }

    private static void writeFile(String text, String url) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(url), "UTF-8"));

        bufferedWriter.write(text);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static String readAndParseHtmlFile(String url) throws IOException {
        File file = new File(url);
        Document document = Jsoup.parse(file, "UTF-8");

        return String.valueOf(document);
    }

    private static String readFile(String url) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));

        String s;
        StringBuilder sb = new StringBuilder("");
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }

        bufferedReader.close();

        return sb.toString();
    }


//    private static void processFile(Cipher ci, String inFile, String outFile) throws IOException, BadPaddingException, IllegalBlockSizeException {
//        try (FileInputStream in = new FileInputStream(inFile);
//             FileOutputStream out = new FileOutputStream(outFile)) {
//            byte[] ibuf = new byte[1024];
//            int len;
//
//            while ((len = in.read(ibuf)) != -1) {
//                byte[] obuf = ci.update(ibuf, 0, len);
//                if (obuf != null) out.write(obuf);
//            }
//
//            byte[] obuf = ci.doFinal();
//            if (obuf != null) out.write(obuf);
//        }
//    }

    public AesCipher getAesCipher() {
        return aesCipher;
    }

    public void setAesCipher(AesCipher aesCipher) {
        AesCipherService.aesCipher = aesCipher;
    }
}
