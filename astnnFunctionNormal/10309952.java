class BackupThread extends Thread {
    public static Command readFrom(ByteBuffer buffer, char[] secretKey) {
        CommandType type = CommandType.valueOf(buffer.get());
        Command c = new Command(type, null);
        MessageDigest md = getDigest(type, secretKey);
        int paramCount = buffer.getInt();
        if (paramCount != -1) {
            int p = 0;
            c.parameters = new String[paramCount];
            while (paramCount-- > 0) {
                byte[] buf = new byte[buffer.getInt()];
                buffer.get(buf);
                md.update(buf);
                c.parameters[p++] = new String(buf, utf8);
            }
        }
        byte[] digest = md.digest(), referenceDigest = new byte[digest.length];
        buffer.get(referenceDigest);
        if (!Arrays.equals(digest, referenceDigest)) throw new IllegalArgumentException("The secret that was used for signing, differs.");
        return c;
    }
}
