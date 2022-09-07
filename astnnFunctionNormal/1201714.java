class BackupThread extends Thread {
    public String download(long id) {
        String dir = new StringBuilder(downloadDir).append(Constants.FILE_SEPARATOR).append("JH-").append(getRandomString(10)).toString();
        String app = new StringBuilder(dir).append(Constants.FILE_SEPARATOR).append(id).append(".jhp").toString();
        File d = new File(dir);
        if (d.exists()) download(id);
        if (!d.mkdirs()) {
            log.error("Error creating temp download directory: #0", dir);
            throw new RuntimeException("Could not create temp download directory: " + dir);
        }
        try {
            Download download = new AppstoreAPIStub.Download();
            download.setId(id);
            AppstoreAPIStub.DownloadE downloadImpl = new AppstoreAPIStub.DownloadE();
            downloadImpl.setDownload(download);
            DataHandler data = stub.download(downloadImpl).getDownloadResponse().get_return();
            InputStream inputStream = data.getInputStream();
            File f = new File(app);
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
            log.info("Done downloading application to #0", d.getAbsolutePath());
            return app;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            FacesMessages.instance().add(ioe.getMessage());
        }
        return null;
    }
}
