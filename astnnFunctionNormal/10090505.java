class BackupThread extends Thread {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String imageFilePath = System.getProperty("java.io.tmpdir");
        String imageFileName = req.getParameter("file");
        if (imageFileName == null) {
            LOG.info("ImageFileName not supplied to request ");
            return;
        }
        imageFileName = URLDecoder.decode(imageFileName, "UTF-8");
        File imageFile = new File(imageFilePath, imageFileName);
        if (!imageFile.exists()) {
            LOG.info("ImageFile does not exist");
            return;
        }
        String contentType = URLConnection.guessContentTypeFromName(imageFileName);
        if (contentType == null || !contentType.startsWith("image")) {
            LOG.info("ContentType not an image");
            return;
        }
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            input = new BufferedInputStream(new FileInputStream(imageFile));
            int contentLength = input.available();
            res.reset();
            res.setContentLength(contentLength);
            res.setContentType(contentType);
            res.setHeader("Content-disposition", "inline; filename=\"" + imageFileName + "\"");
            output = new BufferedOutputStream(res.getOutputStream());
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            output.flush();
        } catch (IOException e) {
            throw new KwantuFaultException("Problem writing to response ", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error("Closing InputStream ", e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOG.error("Closing OutputStream ", e);
                }
            }
        }
    }
}
