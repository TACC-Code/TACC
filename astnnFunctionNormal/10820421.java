class BackupThread extends Thread {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filePath = ServletRequestUtils.getRequiredStringParameter(request, "filepath");
        String realPath = this.getServletContext().getRealPath(filePath);
        File file = new File(realPath);
        if (file != null) {
            FileDataSource fileDataSource = new FileDataSource(file);
            response.setContentType(fileDataSource.getContentType());
            FileInputStream inputStream = (FileInputStream) fileDataSource.getInputStream();
            FileChannel channel = inputStream.getChannel();
            String mimetype = this.getServletContext().getMimeType(filePath);
            response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            OutputStream outputStream = response.getOutputStream();
            long bytesTransfered = channel.transferTo(0, channel.size(), Channels.newChannel(outputStream));
            response.flushBuffer();
        }
    }
}
