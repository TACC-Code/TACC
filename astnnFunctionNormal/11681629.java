class BackupThread extends Thread {
    public void open(File file) throws FileNotFoundDriverException {
        try {
            if (file.canWrite()) {
                try {
                    raf = new RandomAccessFile(file, "rw");
                    mode = FileChannel.MapMode.READ_WRITE;
                } catch (FileNotFoundException e) {
                    raf = new RandomAccessFile(file, "r");
                    mode = FileChannel.MapMode.READ_ONLY;
                }
            } else {
                raf = new RandomAccessFile(file, "r");
                mode = FileChannel.MapMode.READ_ONLY;
            }
            channel = raf.getChannel();
            buffer = new BigByteBuffer2(channel, mode);
            myHeader = new DbaseFileHeader();
            myHeader.readHeader(buffer);
            if (charSet == null) {
                Preferences prefs = Preferences.userRoot().node("gvSIG.encoding.dbf");
                String defaultCharSetName = prefs.get("dbf_encoding", DbaseFile.getDefaultCharset().toString());
                String charSetName = DbfEncodings.getInstance().getCharsetForDbfId(myHeader.getLanguageID());
                if ((charSetName == null) || (charSetName.equalsIgnoreCase("UNKNOWN"))) {
                    try {
                        charSet = Charset.forName(defaultCharSetName);
                    } catch (UnsupportedCharsetException e) {
                        charSet = Charset.defaultCharset();
                    }
                } else {
                    try {
                        charSet = Charset.forName(charSetName);
                    } catch (UnsupportedCharsetException e) {
                        charSet = Charset.defaultCharset();
                    }
                }
            }
            bytesCachedRecord = new byte[myHeader.getRecordLength()];
        } catch (IOException e) {
            throw new FileNotFoundDriverException("DBF", e, file.getAbsolutePath());
        }
    }
}
