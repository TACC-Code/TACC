class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printAccess(request, false);
        String url = request.getParameter("url");
        if (url == null || url.length() == 0) {
            reportJsonError(request, response, "No url given");
            return;
        }
        try {
            response.setContentType("application/json");
            InputStream input = new URL(url).openStream();
            OutputStream output = response.getOutputStream();
            byte[] boeuf = new byte[1000];
            int lg;
            while ((lg = input.read(boeuf)) > 0) {
                output.write(boeuf, 0, lg);
            }
            return;
        } catch (Exception e) {
            reportJsonError(request, response, e);
        }
    }
}
