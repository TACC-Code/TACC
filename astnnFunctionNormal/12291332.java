class BackupThread extends Thread {
    public synchronized void flush() throws IOException {
        LOG.debug("flushing");
        if (!isOpen()) return;
        for (RandomAccessFile f : _fos) {
            if (f != null) f.getChannel().force(false); else {
                StringBuilder report = new StringBuilder();
                report.append("flush npe report:");
                report.append("  files:").append(_files).append("  ");
                report.append("fos length ").append(_fos.length).append("  ");
                for (RandomAccessFile f2 : _fos) report.append(String.valueOf(f2)).append("  ");
                throw new IllegalStateException(report.toString());
            }
        }
    }
}
