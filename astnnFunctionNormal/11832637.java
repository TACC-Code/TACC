class BackupThread extends Thread {
    public void hash() {
        int read;
        byte[] data;
        MessageDigest digest;
        FileInputStream stream;
        if (this.isFolder()) return;
        try {
            data = new byte[1024 * 1024];
            digest = MessageDigest.getInstance("SHA-256");
            stream = new FileInputStream(this.getLocalPath());
            while ((read = stream.read(data)) != -1) {
                digest.update(data, 0, read);
            }
            this.setHash(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "FileListNode.HashNode: Failed to find hash instance");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "FileListNode.HashNode: File not found (" + this.getLocalPath() + ")");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FileListNode.HashNode: Failed to hash file");
        }
    }
}
