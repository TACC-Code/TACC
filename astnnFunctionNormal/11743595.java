class BackupThread extends Thread {
        private String determineID() {
            try {
                MessageDigest algorithm = MessageDigest.getInstance("SHA1");
                byte[] hash = algorithm.digest(getContent());
                return ByteToHex.convert(hash);
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
}
