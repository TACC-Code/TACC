class BackupThread extends Thread {
    public synchronized String getID() {
        if (m_count < 0) {
            m_count = 0;
        }
        String uid = Integer.toHexString(m_address) + Long.toHexString(m_startTime) + Long.toHexString(System.currentTimeMillis()) + Integer.toHexString(m_count++) + Integer.toHexString(m_randomGenerator.nextInt());
        try {
            uid = hexEncode(m_digest.digest(uid.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Invalid encoding specified for a byte array in id generator.");
        }
        return uid;
    }
}
