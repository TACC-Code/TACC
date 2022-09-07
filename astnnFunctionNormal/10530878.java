class BackupThread extends Thread {
                public void run() {
                    final int opCount = getBlockSize();
                    for (int i = 0; i < NELEMENT; i++) {
                        String genKey = rndStrings[i + index * NELEMENT];
                        if (i % (readcount + writecount) < readcount) {
                            mapStr.get(genKey);
                        } else if (index % 2 == 0) {
                            mapStr.remove(genKey);
                        } else {
                            mapStr.put(genKey, genKey);
                        }
                    }
                }
}
