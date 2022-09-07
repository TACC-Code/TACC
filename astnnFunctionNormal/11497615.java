class BackupThread extends Thread {
    public String hashData(String data, String hashTemplate) {
        try {
            md.reset();
            md.update(data.getBytes(encoding));
            return bytesToHex(md.digest());
        } catch (IOException ex) {
            l.log(Level.SEVERE, "Could not hash data", ex);
            return "<error>";
        }
    }
}
