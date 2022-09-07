class BackupThread extends Thread {
    protected Long hashSearch(byte[] block, int off, int len) {
        Integer weakSum = new Integer(config.weakSum.getValue());
        if (hashtable.containsKey(weakSum.intValue())) {
            if (hashtable.containsKey(weakSum)) {
                config.strongSum.reset();
                config.strongSum.update(block, off, len);
                if (config.checksumSeed != null) {
                    config.strongSum.update(config.checksumSeed);
                }
                byte[] digest = new byte[config.strongSumLength];
                System.arraycopy(config.strongSum.digest(), 0, digest, 0, digest.length);
                return (Long) hashtable.get(new ChecksumPair(weakSum.intValue(), digest));
            }
        }
        return null;
    }
}
