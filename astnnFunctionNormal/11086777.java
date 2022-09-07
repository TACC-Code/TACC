class BackupThread extends Thread {
            public void run() {
                InputStream is = null;
                try {
                    iconLabel.setIcon(downloadIcon);
                    String path = filePath;
                    String inputFileName;
                    File file = new File(filePath);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                        inputFileName = file.getName();
                    } else {
                        URL url = new URL(path);
                        is = url.openConnection().getInputStream();
                        inputFileName = url.getFile();
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[0xFF];
                    int i = is.read(buffer);
                    int done = 0;
                    while (i != -1) {
                        bos.write(buffer, 0, i);
                        done += i;
                        i = is.read(buffer);
                    }
                    data = bos.toByteArray();
                    fileName = simpleFileName(inputFileName);
                    setStatus((data.length / 1024) + "k " + fileName);
                } catch (Throwable e) {
                    Logger.error(e);
                    setStatus("Download error " + e.getMessage());
                } finally {
                    IOUtils.closeQuietly(is);
                    iconLabel.setIcon(btIcon);
                }
            }
}
