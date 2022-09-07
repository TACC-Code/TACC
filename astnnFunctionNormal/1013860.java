class BackupThread extends Thread {
        public void startDownload() throws IOException {
            Flags f = Flags.valueOf(flags);
            FileCache.Group cache = f.isSet(DOWNLOAD_NO_CACHE_UPDATE | DOWNLOAD_USE_CACHE) ? FileCache.getDownloadGroup() : null;
            try {
                if (f.isSet(DOWNLOAD_NO_CACHE_UPDATE)) {
                    file = cache.getFile(url, FileCache.IGNORE_DATE);
                    if (file != null) {
                        length = file.length();
                        if (destinationFile != null) FS.copyFile(file, destinationFile);
                        return;
                    }
                }
                connection = url.openConnection();
                setupConnection(connection, f.intValue());
                connection = checkTemporaryRedirect(connection, f.intValue());
                lastModified = connection.getLastModified();
                if (f.isSet(DOWNLOAD_USE_CACHE)) {
                    file = cache.getFile(url, lastModified);
                    if (file != null) {
                        log.debugFormat("Using file from cache: %s", file);
                        length = file.length();
                        if (destinationFile != null) FS.copyFile(file, destinationFile);
                        return;
                    }
                    if (lastModified == FileCache.NO_DATE) file = cache.newPath(url, FileCache.IGNORE_DATE, cacheSuffix).toFile(); else file = cache.newPath(url, lastModified, cacheSuffix).toFile();
                    log.debugFormat("Creating new cache entry: %s", file);
                } else {
                    file = File.createTempFile("download", cacheSuffix);
                }
                log.debugFormat("Downloading \"%s\"...", url);
                input = getInputStream(connection);
                inputType = input.getClass();
                input = new BufferedInputStream(input);
                output = new FS.BufferedFileOutput(file);
                length = connection.getContentLengthLong();
                FS.copyStream(input, output, FS.COPY_BUF_LENGTH, length, progressListener);
                FS.close(output);
                if (destinationFile != null) FS.copyFile(file, destinationFile);
            } catch (IOException exception) {
                if (cache != null) cache.remove(url);
                throw exception;
            } catch (Exception exception) {
                if (cache != null) cache.remove(url);
                throw new IOException(exception);
            } finally {
                shutDownConnection();
            }
        }
}
