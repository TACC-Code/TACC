class BackupThread extends Thread {
    public void load(String strFileName) {
        InputStream isCurrent = null;
        DataInputStream disCurrent = null;
        try {
            if (getSaveMethod() == SAVE_METHOD_FILE) isCurrent = new FileInputStream(strFileName); else isCurrent = getBinaryStream(strFileName);
            disCurrent = new DataInputStream(isCurrent);
            while (true) dos.writeByte(disCurrent.readByte());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
                if (disCurrent != null) disCurrent.close();
                if (isCurrent != null) isCurrent.close();
            } catch (IOException ioe) {
            }
        }
    }
}
