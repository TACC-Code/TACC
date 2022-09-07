class BackupThread extends Thread {
    public static String encryptar(String textoOriginal) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(textoOriginal.getBytes());
        StringBuffer cadenaEncryp = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            cadenaEncryp.append(Integer.toHexString((b[i] & 0xFF)));
        }
        String textoCifrado = cadenaEncryp.toString();
        System.out.println("Texto Original: " + textoOriginal + "\nTexto Cifrado: " + textoCifrado);
        return textoCifrado;
    }
}
