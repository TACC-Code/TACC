class BackupThread extends Thread {
    public void writeContent(OutputStream oStream) throws GroomException {
        DataInputStream aDIS = null;
        try {
            aDIS = new DataInputStream(new FileInputStream(_resource));
            while (true) oStream.write(aDIS.readUnsignedByte());
        } catch (EOFException e) {
        } catch (FileNotFoundException e) {
            throw new GroomException(GroomException.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new GroomException(GroomException.IO_ERROR);
        } finally {
            try {
                if (aDIS != null) {
                    aDIS.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
