class BackupThread extends Thread {
    @Override
    protected String createDigest(String message) {
        byte[] messageBytes = message.getBytes();
        String digest;
        synchronized (this.messageDigest) {
            if (this.previousDigest != null) this.messageDigest.update(this.previousDigest.getBytes());
            byte[] digestBytes = this.messageDigest.digest(messageBytes);
            digest = this.encode(digestBytes);
            this.previousDigest = digest;
        }
        return digest;
    }
}
