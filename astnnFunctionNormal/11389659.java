class BackupThread extends Thread {
    public static void writeToStream(File _file, OutputStream _stream) {
        BufferedInputStream _buf = null;
        try {
            FileInputStream input = new FileInputStream(_file);
            _buf = new BufferedInputStream(input);
            int readBytes = 0;
            while ((readBytes = _buf.read()) != -1) {
                _stream.write(readBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (_stream != null) _stream.close();
                if (_buf != null) _buf.close();
            } catch (IOException e) {
            }
        }
    }
}
