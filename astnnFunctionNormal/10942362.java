class BackupThread extends Thread {
    private String digest(IMessageDigest hash, byte[] encoded) {
        hash.update(encoded);
        byte[] b = hash.digest();
        StringBuilder sb = new StringBuilder().append(Util.toString(b, 0, 1));
        for (int i = 1; i < b.length; i++) sb.append(":").append(Util.toString(b, i, 1));
        String result = sb.toString();
        return result;
    }
}
