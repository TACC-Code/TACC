class BackupThread extends Thread {
    private String getCodeSignature(Object targetObject) {
        try {
            Class<?> objectClass = targetObject.getClass();
            byte[] classBytes = FileFinder.findClassFile(objectClass);
            if (null == classBytes) {
                throw new IOException("Unable to find bytes for class " + objectClass.getName());
            }
            synchronized (messageDigest) {
                messageDigest.reset();
                messageDigest.update(classBytes);
                final byte[] digestBytes = messageDigest.digest();
                return new BigInteger(digestBytes).toString(32);
            }
        } catch (IOException exc) {
            Logger.getLogger().warn(exc, "Market place unable to load code (to digest).");
            return "";
        }
    }
}
