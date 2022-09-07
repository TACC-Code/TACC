class BackupThread extends Thread {
    static void extractAndLoadLibrary(LibraryImplementation libImpl, File libraryFile) {
        InputStream is = null;
        RandomAccessFile readRaf = null;
        RandomAccessFile writeRaf = null;
        FileLock writeLock = null;
        FileLock readLock = null;
        try {
            byte[] nativeLibraryBuffer = null;
            {
                String resourceName = libImpl.libraryResource;
                ClassLoader classLoader = NativeLibLoader.class.getClassLoader();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    is = classLoader.getResourceAsStream(resourceName);
                    byte[] buffer = new byte[8192];
                    int nread = 0;
                    while (nread != -1) {
                        bos.write(buffer, 0, nread);
                        nread = is.read(buffer);
                    }
                } finally {
                    if (is != null) is.close();
                }
                nativeLibraryBuffer = bos.toByteArray();
            }
            try {
                readRaf = new RandomAccessFile(libraryFile, "r");
                readLock = readRaf.getChannel().lock(0, Long.MAX_VALUE, true);
            } catch (FileNotFoundException fnfx) {
            }
            if (readLock != null) {
                boolean hasIntegrity = true;
                int libraryOnDiskSize = 0;
                byte[] libraryOnDiskBuffer = new byte[(int) readRaf.length()];
                while (libraryOnDiskSize < libraryOnDiskBuffer.length) {
                    int n = readRaf.read(libraryOnDiskBuffer, libraryOnDiskSize, libraryOnDiskBuffer.length - libraryOnDiskSize);
                    if (n == -1) {
                        hasIntegrity = false;
                        break;
                    }
                    libraryOnDiskSize += n;
                }
                if (hasIntegrity && libraryOnDiskSize != nativeLibraryBuffer.length) {
                    hasIntegrity = false;
                }
                if (hasIntegrity && !Arrays.equals(nativeLibraryBuffer, libraryOnDiskBuffer)) {
                    hasIntegrity = false;
                }
                if (!hasIntegrity) {
                    readLock.release();
                    readLock = null;
                    readRaf.close();
                    readRaf = null;
                }
            }
            if (readLock == null) {
                writeRaf = new RandomAccessFile(libraryFile, "rw");
                writeLock = writeRaf.getChannel().lock(0, Long.MAX_VALUE, false);
                writeRaf.write(nativeLibraryBuffer);
                writeRaf.setLength(nativeLibraryBuffer.length);
                if (libSet.command != null) {
                    String[] commandArray = new String[libSet.command.length];
                    for (int n = 0; n < libSet.command.length; ++n) {
                        String actualCommand = libSet.command[n].replaceAll("\\{library-file\\}", libraryFile.getAbsolutePath());
                        commandArray[n] = actualCommand;
                    }
                    Process p = Runtime.getRuntime().exec(commandArray, null, libraryFile.getAbsoluteFile().getParentFile());
                    for (; ; ) {
                        try {
                            p.waitFor();
                        } catch (InterruptedException ex) {
                            continue;
                        }
                        break;
                    }
                }
                writeLock.release();
                writeLock = null;
                writeRaf.close();
                writeRaf = null;
                libraryFile.deleteOnExit();
            }
            if (readLock == null) {
                readRaf = new RandomAccessFile(libraryFile, "r");
                readLock = readRaf.getChannel().lock(0, Long.MAX_VALUE, true);
            }
            String libpath = libraryFile.getAbsolutePath();
            Logger.getLogger(NativeLibLoader.class.getName()).log(Level.FINE, "loading library from: " + libpath);
            System.load(libpath);
            readLock.release();
            readLock = null;
            readRaf.close();
            readRaf = null;
        } catch (IOException iox) {
            iox.printStackTrace();
        } finally {
            try {
                if (writeLock != null) writeLock.release();
                if (writeRaf != null) writeRaf.close();
                if (readRaf != null) readRaf.close();
                if (readLock != null) readLock.release();
            } catch (IOException iox) {
            }
            try {
                if (is != null) is.close();
            } catch (IOException iox) {
            }
        }
    }
}
