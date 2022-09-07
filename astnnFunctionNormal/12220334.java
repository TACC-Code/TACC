class BackupThread extends Thread {
    private String generateToken() {
        return bytes2String(sha1.digest(UUID.randomUUID().toString().getBytes()));
    }
}
