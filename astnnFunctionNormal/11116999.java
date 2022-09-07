class BackupThread extends Thread {
    private void checkDatabaseInUse() throws DException {
        StringTokenizer tokenizer = new StringTokenizer(System.getProperty("java.version"), ".");
        String javaVersion = null;
        if (tokenizer.countTokens() > 1) {
            javaVersion = tokenizer.nextToken();
            javaVersion += tokenizer.nextToken();
        }
        if (Integer.parseInt(javaVersion) < 14) {
            checkDatabaseInUseBelowJavaVersion1_4();
            return;
        }
        String logFile = daffodilHome + File.separator + "log.lg";
        File ff = new File(logFile);
        try {
            raf = new RandomAccessFile(ff, "rw");
            Method mt = null;
            try {
                mt = raf.getClass().getDeclaredMethod("getChannel", null);
            } catch (SecurityException ex1) {
                System.err.println(ex1);
            } catch (NoSuchMethodException ex1) {
                ex1.printStackTrace();
                System.err.println(ex1);
            }
            if (mt == null) {
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            }
            Object channel = null;
            Object lockObject = null;
            try {
                channel = mt.invoke(raf, null);
                if (channel == null) {
                    ff = null;
                    raf = null;
                    checkDatabaseInUseBelowJavaVersion1_4();
                    return;
                }
                mt = null;
                mt = channel.getClass().getMethod("tryLock", null);
                if (mt == null) {
                    ff = null;
                    raf = null;
                    checkDatabaseInUseBelowJavaVersion1_4();
                    return;
                }
                lockObject = mt.invoke(channel, null);
            } catch (InvocationTargetException ex2) {
                ff = null;
                raf = null;
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            } catch (IllegalArgumentException ex2) {
                ff = null;
                raf = null;
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            } catch (IllegalAccessException ex2) {
                ff = null;
                raf = null;
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            } catch (SecurityException ex1) {
                ff = null;
                raf = null;
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            } catch (NoSuchMethodException ex1) {
                ff = null;
                raf = null;
                checkDatabaseInUseBelowJavaVersion1_4();
                return;
            }
            if (lockObject == null) throw new DException("DSE5522", new Object[] {});
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
}
