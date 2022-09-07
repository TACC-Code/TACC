class BackupThread extends Thread {
    private byte[] loadPictureData(String url) {
        if (url != null) {
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(new URL(url).openStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int count = bis.read(buf);
                while (count != -1) {
                    bos.write(buf, 0, count);
                    count = bis.read(buf);
                }
                return bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
