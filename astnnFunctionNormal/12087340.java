class BackupThread extends Thread {
    protected Object[] loadDatabase(SignificanceReader reader, String datafile) {
        SortedMap<String, SignificanceUnit> words = new TreeMap<String, SignificanceUnit>();
        HashMap<String, SignificanceUnit> addresses = new HashMap<String, SignificanceUnit>();
        Object[] retVal = new Object[2];
        FileChannel channel = null;
        try {
            File file = new File(SIGNIFICANCE_PATH + File.separator + datafile);
            if (file == null || !file.exists()) {
                if (!file.createNewFile()) {
                    L.appendln("JANE16-ERROR: class=" + this.getClass().getName() + "; " + "                   method='protected Object[] loadDatabase(Reader reader)'; " + "                   timestamp=" + System.currentTimeMillis(), this);
                    L.appendln("Description: Critical Error-Can't create lexicon database.", this);
                    L.appendln("Details: Word: ( access priviledges for " + SIGNIFICANCE_PATH + File.separator + datafile + " ? )", this);
                    L.flush(this, Level.SEVERE, L.JANE16_ERROR_LOGGER);
                    System.err.println("Can't create lexicon database...critical error! " + "( access priviledges for " + SIGNIFICANCE_PATH + File.separator + datafile + " ? )");
                    System.exit(-1);
                    return null;
                }
            }
            channel = new FileInputStream(file).getChannel();
            byte[] data = reader.readSignificanceWords(channel, file.length());
            int j = 0;
            byte[] arr = new byte[4];
            boolean done = false;
            for (int i = 0; i < data.length; i++) {
                if (j < (4)) {
                    arr[j++] = data[i];
                    continue;
                }
                int dataLength = Utils.bytesToInt(arr, 0);
                byte[] wordData = new byte[dataLength];
                System.arraycopy(data, i, wordData, 0, dataLength);
                SignificanceUnit sunit = new SignificanceUnit(wordData);
                words.put(sunit.getWord(), sunit);
                addresses.put(sunit.getAddress().getAsKey(), sunit);
                i += dataLength - 1 + (Demarcation.SIZE);
                j = 0;
            }
        } catch (Exception e) {
            L.appendln(e.getMessage(), this);
            L.flush(this, Level.SEVERE, L.EXCEPTIONS_LOGGER);
            if (L.PRINT_STACK_TRACE) {
                e.printStackTrace(PrintStreamManager.getPs(this.getClass().getName()));
            }
        } finally {
            if (channel != null) try {
                channel.close();
            } catch (IOException ex) {
            }
        }
        retVal[0] = words;
        retVal[1] = addresses;
        return retVal;
    }
}
