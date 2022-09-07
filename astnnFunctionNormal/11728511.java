class BackupThread extends Thread {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File f = makeFile(request.getPathInfo());
        if (!f.exists() || !f.canRead()) {
            if (logger.isDebugEnabled()) logger.debug("Won't serve this file: " + f);
            response.sendError(404);
        } else {
            if (f.isDirectory()) {
                if (logger.isDebugEnabled()) logger.debug("Listing Directory: " + f);
                response.setContentType("text/plain");
                response.setCharacterEncoding("ISO-8859-1");
                PrintWriter w = response.getWriter();
                for (File file : f.listFiles()) {
                    w.write(file.isDirectory() ? "D " : "F ");
                    w.write(file.getName());
                    w.write("\n");
                }
            } else {
                if (logger.isDebugEnabled()) logger.debug("Getting file: " + f);
                response.setContentType("application/octet-stream");
                ServletOutputStream os = response.getOutputStream();
                FileInputStream is = new FileInputStream(f);
                byte buffer[] = new byte[1024];
                int read;
                while ((read = is.read(buffer)) > 0) os.write(buffer, 0, read);
                is.close();
                os.flush();
            }
        }
    }
}
