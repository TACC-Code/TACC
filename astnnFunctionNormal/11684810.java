class BackupThread extends Thread {
    public static int digest___3B___3B(MJIEnv env, int objRef, int inputRef) {
        MessageDigest md = getDigest(env, objRef);
        byte[] input = env.getByteArrayObject(inputRef);
        byte[] res = md.digest(input);
        return env.newByteArray(res);
    }
}
