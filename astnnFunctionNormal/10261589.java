class BackupThread extends Thread {
    private void doDownload(HttpServletRequest request, HttpServletResponse response, String filename) throws IOException, ServletException {
        response.setContentType("image/" + imageExtension.substring(1, imageExtension.length()));
        response.setStatus(200);
        response.setHeader("Content-disposition", "inline; filename=" + filename);
        OutputStream outStream = response.getOutputStream();
        try {
            outStream.write(readFile(request, filename));
            outStream.flush();
        } catch (Exception e) {
            logger.error("Error reading file: ", e);
        } finally {
            outStream.close();
        }
    }
}
