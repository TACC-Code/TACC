class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] buffer = new byte[8192];
        int readSize = 0;
        FileInputStream fis = new FileInputStream(args[0]);
        while ((readSize = fis.read(buffer)) != -1) {
            md.update(buffer, 0, readSize);
        }
        byte[] digest = md.digest();
        System.out.println(new String(Hex.encodeHex(digest)));
        fis.close();
    }
}
