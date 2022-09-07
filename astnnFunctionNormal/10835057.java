class BackupThread extends Thread {
    public static void testChannelFile(String file, int buffer_len) throws IOException, NoSuchAlgorithmException {
        FileInputStream from = null;
        long currMS = System.currentTimeMillis();
        System.out.println("Current time = " + ElapsedTime.getDateTime(new Date(currMS)));
        File f = new File(file);
        System.out.println("file size = " + f.length());
        try {
            from = new FileInputStream(file);
            ReadableByteChannel channel = from.getChannel();
            byte[] buffer = new byte[buffer_len];
            ByteBuffer buf = ByteBuffer.wrap(buffer);
            int numRead = 0;
            while (numRead >= 0) {
                numRead = channel.read(buf);
                if (buf.position() == 0) break;
                String hash = StringUtils.getHexString(getMD5ByteHash(buf.array()));
                String newHash = StringUtils.getHexString(StringUtils.getHexBytes(hash));
                if (hash.equalsIgnoreCase(newHash)) System.out.println(hash); else System.out.println(hash + " " + newHash);
                buf.clear();
            }
            System.out.println("Elapsed time = " + (System.currentTimeMillis() - currMS) / 1000 + " to read ");
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
                ;
            }
        }
    }
}
