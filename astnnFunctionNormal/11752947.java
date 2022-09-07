class BackupThread extends Thread {
    String basicAddResourceFromStream(InputStream inputStream, String extension, String prefix, String requestedResourceReference) {
        if (prefix != null) {
            if (!this.isSupportedPrefix(prefix)) {
                System.out.println("Unsupported resource prefix: " + prefix);
                return null;
            }
        }
        String tempFileName = "TEMP_" + Standards.newUUID() + extension;
        File resourceFile = new File(this.basePathForTemporary(), tempFileName);
        try {
            FileOutputStream resourceFileOutputStream = new FileOutputStream(resourceFile);
            try {
                ResourceFileSupport.copyInputStreamToOutputStream(inputStream, resourceFileOutputStream);
            } finally {
                resourceFileOutputStream.close();
            }
            String resourceReference = Standards.getResourceReferenceWithSHA256HashAsHexEncodedString(resourceFile);
            if ((requestedResourceReference != null) && (!resourceReference.equals(requestedResourceReference))) {
                System.out.println("Requested resource reference does not match generated one: " + requestedResourceReference + " != " + resourceReference);
                if (!resourceFile.delete()) {
                    System.out.println("Could not delete temp file: " + resourceFile.getAbsolutePath());
                }
                return null;
            }
            File destinationFile = this.fileForResourceReference(resourceReference);
            if (destinationFile.exists()) {
                System.out.println("Resource file already exists");
                if (!resourceFile.delete()) {
                    System.out.println("Could not delete temp file: " + resourceFile.getAbsolutePath());
                }
                return resourceReference;
            }
            destinationFile.getParentFile().mkdirs();
            if (!resourceFile.renameTo(destinationFile)) {
                System.out.println("Target does not exist, but could not rename file to: " + destinationFile.getName());
            } else {
            }
            RandomAccessFile changesRandomAccessFile = null;
            String changeLogStateToken = null;
            try {
                String information = "{\"action\":\"PUT\", \"resource\":\"" + resourceReference + "\"}\n";
                byte[] utf8 = information.getBytes("UTF-8");
                File changesFile = this.currentChangesFile();
                changesRandomAccessFile = new RandomAccessFile(changesFile, "rws");
                FileLock lock = changesRandomAccessFile.getChannel().lock(Long.MAX_VALUE - 1, 1, true);
                try {
                    changesRandomAccessFile.seek(changesRandomAccessFile.length());
                    changesRandomAccessFile.write(utf8);
                } finally {
                    lock.release();
                }
                changeLogStateToken = changesFile.getName() + ":" + changesRandomAccessFile.getFilePointer();
            } catch (Exception error) {
                System.out.println("Some kind of exception occurred");
                System.out.println(error);
            } finally {
                if (changesRandomAccessFile != null) {
                    changesRandomAccessFile.close();
                }
            }
            RandomAccessFile logRandomAccessFile = null;
            try {
                String currentTimestampString = Standards.getCurrentTimestamp();
                String currentDateString = currentTimestampString.substring(0, PointrelLogFileDateSubstringLength);
                File logFile = new File(this.basePathForLogging() + PointrelLogFilePrefix + currentDateString + PointrelLogFileSuffix);
                String logLine = currentTimestampString + " PUT " + resourceReference + "\n";
                byte[] utf8 = logLine.getBytes("UTF-8");
                logRandomAccessFile = new RandomAccessFile(logFile, "rws");
                FileLock lock = logRandomAccessFile.getChannel().lock(Long.MAX_VALUE - 1, 1, true);
                try {
                    logRandomAccessFile.seek(logRandomAccessFile.length());
                    logRandomAccessFile.write(utf8);
                } finally {
                    lock.release();
                }
            } catch (Exception error) {
                System.out.println("Some kind of exception occurred");
                System.out.println(error);
            } finally {
                if (logRandomAccessFile != null) {
                    logRandomAccessFile.close();
                }
            }
            if (ResourceFileSupport.VariableResourceFileSuffix.equals(extension)) {
                if (this.cachedVariables == null) this.updateCachedVariablesIfNeeded();
                this.updateVariableValueIfNeeded(resourceReference, "ADD", true);
                if (changeLogStateToken != null) this.stateTokenForVariableCache = changeLogStateToken;
            }
            return resourceReference;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
