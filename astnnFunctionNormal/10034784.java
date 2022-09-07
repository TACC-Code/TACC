class BackupThread extends Thread {
    private byte[] grabData(URL url) throws Exception {
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[1000];
        int readed = -1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((readed = in.read(buffer)) != -1) {
            out.write(buffer, 0, readed);
        }
        return out.toByteArray();
    }
}
