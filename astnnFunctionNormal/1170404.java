class BackupThread extends Thread {
    public static Object copy(Object aSource, Object aDestination) {
        try {
            writePropertiesForObject(readPropertiesForObject(aSource), aDestination);
        } catch (RuntimeException exc) {
            throw new WotonomyException(exc);
        }
        return aDestination;
    }
}
