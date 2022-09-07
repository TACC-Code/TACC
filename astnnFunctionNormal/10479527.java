class BackupThread extends Thread {
    public static String getSignature(Digest.Algorithm algo, InputStream ins, long offset, long length) throws IOException {
        if (ins == null) {
            throw new IllegalArgumentException("ins should not be null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset should not be negative");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algo.getName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ins.skip(offset);
        int bytesRead = 0;
        if (length > 0) {
            byte[] bytes = new byte[(int) length];
            bytesRead = ins.read(bytes);
            if (bytesRead < length) {
                byte[] lastBytes = new byte[bytesRead];
                System.arraycopy(bytes, 0, lastBytes, 0, lastBytes.length);
                md.update(lastBytes);
                ins.close();
                return toHexString(md.digest());
            }
            md.update(bytes);
        } else {
            byte[] bytes = new byte[BUFFER_SIZE];
            while (bytesRead > -1) {
                bytesRead = ins.read(bytes);
                if (bytesRead < BUFFER_SIZE) {
                    byte[] lastBytes = new byte[bytesRead];
                    System.arraycopy(bytes, 0, lastBytes, 0, lastBytes.length);
                    md.update(lastBytes);
                    ins.close();
                    return toHexString(md.digest());
                } else {
                    md.update(bytes);
                }
            }
        }
        ins.close();
        return toHexString(md.digest());
    }
}
