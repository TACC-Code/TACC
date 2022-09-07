class BackupThread extends Thread {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String relativePath = req.getRequestURI().replace(req.getSession().getServletContext().getContextPath(), "");
        try {
            relativePath = URLDecoder.decode(relativePath, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        String realPath = req.getSession().getServletContext().getRealPath(relativePath);
        resp.setContentType("application/octet-stream");
        File file = new File(realPath);
        String filename = file.getName();
        String ua = req.getHeader("User-Agent");
        if (ua != null && ua.indexOf("MSIE") != -1) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            filename = new String(filename.getBytes(), "ISO8859-1");
        }
        resp.setHeader("Content-Disposition", "filename=\"" + filename + "\"");
        if (file.exists()) {
            ServletOutputStream os = resp.getOutputStream();
            FileInputStream is = new FileInputStream(file);
            long streamLen = file.length();
            long startPos = 0;
            if (streamLen != 0 && req.getHeader("Range") != null) {
                resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                startPos = Long.parseLong(req.getHeader("Range").toLowerCase().replaceAll("bytes=", "").replaceAll("-", ""));
                if (startPos != 0) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(startPos).toString()).append("-").append(new Long(streamLen - 1).toString()).append("/").append(new Long(streamLen).toString()).toString();
                    resp.setHeader("Content-Range", contentRange);
                    is.skip(startPos);
                }
            }
            if (streamLen != 0) {
                resp.setHeader("Accept-Ranges", "bytes");
                resp.setHeader("Content-Length", new Long(streamLen - startPos).toString());
            }
            byte[] buf = new byte[1024];
            int read;
            while ((read = is.read(buf)) != -1) {
                os.write(buf, 0, read);
            }
            os.flush();
            os.close();
            is.close();
        }
    }
}
