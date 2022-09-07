class BackupThread extends Thread {
    public synchronized String generateCallIdentifier(String address) {
        String date = new Long(System.currentTimeMillis() + callIDCounter++ + rand.nextLong()).toString();
        byte cid[] = digester.digest(date.getBytes());
        String cidString = Utils.toHexString(cid);
        return cidString + "@" + address;
    }
}
