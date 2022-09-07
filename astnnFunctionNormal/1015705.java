class BackupThread extends Thread {
    private void setClobValue(Writer src, java.sql.Clob clob) throws Exception {
        Reader stream = clob.getCharacterStream();
        char[] b = new char[4096];
        for (int len = -1; (len = stream.read(b, 0, 4096)) != -1; ) src.write(b, 0, len);
        src.flush();
        src.close();
        stream.close();
    }
}
