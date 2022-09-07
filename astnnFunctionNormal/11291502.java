class BackupThread extends Thread {
    public String decodeAES(String input) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(input.getBytes("ISO8859_1"));
        } catch (UnsupportedEncodingException exn1) {
            exn1.printStackTrace();
        }
        Cipher c;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, skeySpec);
            CipherInputStream cis = new CipherInputStream(is, c);
            for (int b; (b = cis.read()) != -1; ) bos.write(b);
            cis.close();
        } catch (NoSuchAlgorithmException exn) {
            exn.printStackTrace();
        } catch (NoSuchPaddingException exn) {
            exn.printStackTrace();
        } catch (InvalidKeyException exn) {
            exn.printStackTrace();
        } catch (IOException exn) {
            exn.printStackTrace();
        }
        String result = null;
        try {
            result = new String(((ByteArrayOutputStream) bos).toByteArray(), "ISO8859_1");
        } catch (UnsupportedEncodingException exn) {
            exn.printStackTrace();
        }
        return result;
    }
}
