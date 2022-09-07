class BackupThread extends Thread {
    @Override
    public boolean downloadFile(String downloadDir, String fileName, String sourceContainer) throws Exception {
        boolean success = false;
        S3Object object = cloudClient.getObject(sourceContainer, fileName);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = object.getDataInputStream();
            int data;
            fos = new FileOutputStream(new File(downloadDir + CommonWS.fs + fileName));
            while ((data = is.read()) != -1) fos.write(data);
            fos.close();
            success = true;
        } finally {
            if (is != null) is.close();
            if (fos != null) fos.close();
        }
        return success;
    }
}
