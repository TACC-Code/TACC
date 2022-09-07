class BackupThread extends Thread {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        VPUser user = UserAuthentication.getUser(req);
        req.setAttribute("VPUser", user);
        StudentSession sess = (StudentSession) req.getSession().getAttribute("StudentSession");
        String placement = sess.getPlacement().getName();
        String page = sess.getPhase();
        String image = (String) req.getParameter("file");
        if (page == null) {
            page = "";
        }
        if (page != "") {
            page += "/";
        }
        File file = new File(settings.getString("vp.datadir") + "/placements/" + placement + "/" + page + image);
        if (!file.exists()) {
            file = new File(settings.getString("vp.datadir") + "/placements/" + placement + "/" + image);
            if (!file.exists()) {
                VPHelpers.error(req, res, "Placement image '" + placement + "/" + page + image + "' does not exist");
                return;
            }
        }
        if (!file.isFile() || !file.canRead()) {
            VPHelpers.error(req, res, "Placement file '" + placement + "/" + page + image + "' cannot be read");
            return;
        }
        FileInputStream dataSource = new FileInputStream(file);
        OutputStream out = res.getOutputStream();
        try {
            int read = dataSource.read();
            while (read != -1) {
                out.write(read);
                read = dataSource.read();
            }
        } catch (IOException ioe) {
            VPHelpers.error(req, res, "Error reading file '" + placement + page + file + "'");
            return;
        }
        out.flush();
    }
}
