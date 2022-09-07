class BackupThread extends Thread {
    public static byte[] getFileContent(URL url) throws IOException {
        System.out.println("Opening url: " + url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        byte[] buf = new byte[4096];
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            baos.write(buf, 0, numRead);
        }
        in.close();
        return baos.toByteArray();
    }
}
