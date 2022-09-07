class BackupThread extends Thread {
    private void writeStream(InputStream inputStream, String filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            log.error("Cannot find target route for write resource: " + filePath);
        } catch (IOException e) {
            log.error("Reading resource", e);
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }
}
