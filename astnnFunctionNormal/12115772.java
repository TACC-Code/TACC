class BackupThread extends Thread {
    private synchronized String sha1sum(final String text) {
        final byte[] hash = this.messageDigest.digest(text.getBytes());
        this.messageDigest.reset();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            final String s = Integer.toHexString(hash[i] & 0xFF);
            if (s.length() == 1) sb.append("0");
            sb.append(s);
        }
        return sb.toString();
    }
}
