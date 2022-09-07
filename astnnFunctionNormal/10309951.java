class BackupThread extends Thread {
    public ByteBuffer writeTo(ByteBuffer buffer, char[] secretKey) {
        MessageDigest md = getDigest(type, secretKey);
        buffer.put(type.getId());
        buffer.putInt(parameters == null ? -1 : parameters.length);
        if (parameters != null) {
            for (String p : parameters) {
                byte[] param = p.getBytes(utf8);
                buffer.putInt(param.length);
                buffer.put(param);
                md.update(param);
            }
        }
        buffer.put(md.digest());
        return buffer;
    }
}
