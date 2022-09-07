class BackupThread extends Thread {
    protected boolean copy(InputStream is, OutputStream os) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is, bufferSize);
            BufferedOutputStream bos = new BufferedOutputStream(os, bufferSize);
            byte[] buffer = new byte[bufferSize];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) bos.write(buffer, 0, read);
            bos.close();
            bis.close();
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
