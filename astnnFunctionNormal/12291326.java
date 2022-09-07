class BackupThread extends Thread {
    public synchronized List<F> open(List<F> files, boolean complete, boolean isVerifying) throws IOException {
        _files = files;
        if (_fos != null) throw new IOException("Files already open(ing)!");
        RandomAccessFile[] fos = new RandomAccessFile[_files.size()];
        long pos = 0;
        List<F> filesToVerify = null;
        for (int i = 0; i < _files.size(); i++) {
            F file = _files.get(i);
            IOUtils.close(fos[i]);
            if (complete) {
                LOG.info("opening torrent in read-only mode");
                fos[i] = new RandomAccessFile(file, "r");
            } else {
                LOG.info("opening torrent in read-write");
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    if (parentFile != null) {
                        parentFile.mkdirs();
                        FileUtils.setWriteable(parentFile);
                    }
                    file.createNewFile();
                    if (!isVerifying) {
                        isVerifying = true;
                        i = -1;
                        continue;
                    }
                }
                FileUtils.setWriteable(file);
                fos[i] = new RandomAccessFile(file, "rw");
                if (isVerifying && fos[i].length() > 0) {
                    if (filesToVerify == null) filesToVerify = new ArrayList<F>(_files.size());
                    filesToVerify.add(file);
                }
            }
            pos += file.length();
        }
        for (RandomAccessFile raf : fos) {
            if (!raf.getFD().valid()) throw new IOException("file was invalid: " + raf);
        }
        _fos = fos;
        return filesToVerify;
    }
}
