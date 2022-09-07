class BackupThread extends Thread {
    private void writeFile() throws IOException {
        if (!this.dir.exists()) {
            Files.mkdirs(this.dir);
        }
        Logger logger = this.factory.getLogger();
        if ((logger != null) && logger.isLoggable(Level.CONFIG)) logger.config("writing preferences: " + this);
        long oldLastModified = this.file.lastModified();
        FileOutputStream out = Files.createLockedOutputStream(this.file, this.factory.setup.fileLockTimeout, TimeUnit.MILLISECONDS);
        try {
            this.properties.write(out, this.factory.setup.fileFormat, true);
            FileChannel outChannel = out.getChannel();
            outChannel.truncate(outChannel.size());
            out.close();
            long newLastModified = this.file.lastModified();
            if (newLastModified == oldLastModified) {
                newLastModified++;
                this.file.setLastModified(newLastModified);
            }
            out = null;
            this.modTime = milliToNanoTime(newLastModified);
        } finally {
            IO.tryClose(out);
        }
    }
}
