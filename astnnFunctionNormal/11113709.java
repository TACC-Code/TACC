class BackupThread extends Thread {
    public synchronized String generateId() {
        byte random[] = new byte[16];
        StringBuffer result = new StringBuffer();
        int resultLenBytes = 0;
        while (resultLenBytes < assertionIdLength) {
            getRandomBytes(random);
            random = getDigest().digest(random);
            for (int j = 0; j < random.length && resultLenBytes < assertionIdLength; j++) {
                byte b1 = (byte) ((random[j] & 0xf0) >> 4);
                byte b2 = (byte) (random[j] & 0x0f);
                if (b1 < 10) result.append((char) ('0' + b1)); else result.append((char) ('A' + (b1 - 10)));
                if (b2 < 10) result.append((char) ('0' + b2)); else result.append((char) ('A' + (b2 - 10)));
                resultLenBytes++;
            }
        }
        return (result.toString());
    }
}
