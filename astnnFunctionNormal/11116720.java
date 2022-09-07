class BackupThread extends Thread {
    public String processRequest(ServerInterface serverInterface) {
        String filename = serverInterface.getRequestData().getParameter("filename");
        String directory = serverInterface.getRequestData().getParameter("directory");
        if (filename == null) filename = serverInterface.getParameter("filename");
        if (directory != null) filename = directory + File.separator + filename;
        try {
            InputStream in = serverInterface.getServletContext().getResourceAsStream(filename);
            if (in == null) {
                if (logger.isLoggable(Level.FINER)) logger.finer("filename resource '" + filename + "' was not located");
                serverInterface.getServletResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "filename resource '" + filename + "' was not located");
            } else {
                serverInterface.getServletResponse().setContentType(serverInterface.getServletContext().getMimeType(filename));
                serverInterface.getServletResponse().addHeader("content-disposition", "attachment; filename=" + new File(filename).getName());
                OutputStream out = serverInterface.getServletResponse().getOutputStream();
                byte buf[] = new byte[2048];
                int numberRead = 0;
                while ((numberRead = in.read(buf)) != -1) out.write(buf, 0, numberRead);
                in.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            serverInterface.setErrorMessage(ServerInterface.UNDEFINED_ERROR, e.toString(), false);
        }
        return NO_FORWARDING;
    }
}
