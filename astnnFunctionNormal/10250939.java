class BackupThread extends Thread {
    public synchronized String newIdentifier() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(++counter);
        buffer.append(System.currentTimeMillis());
        buffer.append(seed);
        buffer.append(ipAddress);
        buffer.append(Math.random());
        byte[] digest = md5.digest(buffer.toString().getBytes());
        byte[] encodedDigest = Base64.encodeForURL(digest);
        return new String(encodedDigest);
    }
}
