class BackupThread extends Thread {
    public void copy(InputStream _in, OutputStream _out, boolean _closeInput) throws IOException {
        byte[] buffer = getCopyBuffer();
        int read;
        while (true) {
            read = _in.read(buffer);
            if (read == -1) break;
            _out.write(buffer, 0, read);
        }
        _out.flush();
        _out.close();
        if (_closeInput) _in.close();
    }
}
