class BackupThread extends Thread {
    public void doGet(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        ServletOutputStream out = rsp.getOutputStream();
        String fileName = req.getParameter("file");
        String pathName = "/org/ezfusion/gui/www/" + fileName;
        InputStream resourceAsStream = getClass().getResourceAsStream(pathName);
        String contentType = getServletContext().getMimeType(pathName);
        if (contentType != null) rsp.setContentType(contentType); else if (pathName.endsWith(".js")) rsp.setContentType("text/javascript"); else if (pathName.endsWith(".css")) rsp.setContentType("text/css"); else if (pathName.endsWith(".html")) rsp.setContentType("text/html"); else rsp.setContentType("application/octet-stream");
        int lastSep = fileName.lastIndexOf("/");
        rsp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName.substring(lastSep + 1, fileName.length()) + "\"");
        try {
            byte[] buf = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = resourceAsStream.read(buf)) != -1) out.write(buf, 0, bytesRead);
        } catch (FileNotFoundException e) {
            out.println("File not found: " + fileName);
        } catch (IOException e) {
            out.println("Problem sending file " + pathName + ": " + e.getMessage());
        } finally {
            if (resourceAsStream != null) resourceAsStream.close();
        }
    }
}
