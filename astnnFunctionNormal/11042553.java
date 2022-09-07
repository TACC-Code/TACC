class BackupThread extends Thread {
    public byte[] hashIterator(String seed, String password, int seqnum) throws SaslException {
        String phrase = new String(seed + new String(password));
        byte[] response = null;
        byte[] temp = phrase.getBytes();
        for (int i = 0; i < seqnum; i++) {
            response = this.digest(temp);
            temp = response;
        }
        return response;
    }
}
