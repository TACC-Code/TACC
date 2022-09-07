class BackupThread extends Thread {
    public static char[] cryptPassword(char[] pwd) throws NoSuchAlgorithmException {
        char[] newPwd = null;
        if (salt != null) {
            newPwd = saltPassword(pwd);
        } else {
            newPwd = pwd;
        }
        if (digestAlgorithm.equals(NONE_ALGORITHM)) {
            return newPwd;
        }
        MessageDigest messageDigest = null;
        char crypt[] = null;
        try {
            messageDigest = messageDigestPool.take();
            messageDigest.reset();
            byte pwdb[] = new byte[newPwd.length];
            for (int b = 0; b < newPwd.length; b++) {
                pwdb[b] = (byte) newPwd[b];
            }
            crypt = hexDump(messageDigest.digest(pwdb));
            smudge(pwdb);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        } finally {
            messageDigestPool.offer(messageDigest);
        }
        return crypt;
    }
}
