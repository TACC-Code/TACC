class BackupThread extends Thread {
    protected java.util.Vector processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        java.util.Vector v1 = new java.util.Vector();
        java.util.Vector url = new java.util.Vector();
        java.util.Vector atc = new java.util.Vector();
        java.util.Enumeration enum1 = request.getAttributeNames();
        while (enum1.hasMoreElements()) {
            String atr = enum1.nextElement().toString();
            String val = request.getAttribute(atr).toString();
        }
        java.util.Enumeration enum2 = request.getParameterNames();
        while (enum2.hasMoreElements()) {
            String atr = enum2.nextElement().toString();
            String val = request.getParameter(atr);
        }
        String type = "";
        String catrecid = "";
        String ownlibid = "";
        if (request.getParameter("HERE") == null) {
            System.out.println("calling1 in attachments page");
            type = request.getAttribute("type1").toString().trim();
            catrecid = request.getAttribute("catrecid1").toString().trim();
            ownlibid = request.getAttribute("ownlibid1").toString().trim();
        } else {
            System.out.println("calling2  in attachments page");
            type = request.getParameter("type1").trim();
            catrecid = request.getParameter("catrecid1").trim();
            ownlibid = request.getParameter("ownlibid1").trim();
        }
        String filename1 = "";
        try {
            if ((type.equals("URL")) || (type.equals("ATT"))) {
                v1 = ((ejb.bprocess.opac.SearchCatalogueHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("SearchCatalogue")).create().getAttachmentsUrls(null, catrecid, ownlibid);
                if (v1.elementAt(0) != null) url = (java.util.Vector) v1.elementAt(0);
                if (v1.elementAt(1) != null) atc = (java.util.Vector) v1.elementAt(1);
                request.setAttribute("Attachments", atc);
                request.setAttribute("ownlibid", ownlibid);
                request.setAttribute("catrecid", catrecid);
                request.setAttribute("Urls", url);
                String path = "";
                if (type.equals("URL")) {
                    if (url.size() > 1) {
                        System.out.println("more than one urls");
                        path = "/jsp/cataloguing/Attachments-Urls.jsp";
                        RequestDispatcher rd = request.getRequestDispatcher(path);
                        rd.forward(request, response);
                    } else if (url.size() > 0) {
                        path = url.elementAt(0).toString();
                        response.sendRedirect(path);
                    }
                } else if (type.equals("ATT")) {
                    System.out.println("calling attachments");
                    if (atc.size() == 1) {
                        String filepath = ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords";
                        filepath += "/CAT_" + catrecid + "_" + ownlibid;
                        String filename = atc.elementAt(0).toString();
                        System.out.println("path name=" + filepath + "/" + filename);
                        java.io.File actualfile = new java.io.File(filepath + "/" + filename);
                        java.nio.channels.FileChannel fc = (new java.io.FileInputStream(actualfile)).getChannel();
                        int fileLength = (int) fc.size();
                        System.out.println("file length=" + fileLength);
                        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        System.out.println("calling1");
                        System.out.println("calling 2");
                        java.util.StringTokenizer stk = new java.util.StringTokenizer(filename, ".");
                        stk.nextToken();
                        String extension = stk.nextToken();
                        System.out.println("extension=" + extension);
                        java.util.Enumeration enumeration = java.util.ResourceBundle.getBundle("server").getKeys();
                        String contenttype = "";
                        try {
                            contenttype = java.util.ResourceBundle.getBundle("server").getString(extension.trim().toUpperCase());
                        } catch (Exception exp) {
                            java.util.Properties p1 = new java.util.Properties();
                            java.io.File propfile = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/SystemFiles/ContentTypes.properties");
                            System.out.println("----" + propfile.getPath());
                            java.io.FileInputStream f1 = new java.io.FileInputStream(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.util.ResourceBundle.getBundle("server").getString("SystemFilesPath") + "/ContentTypes.properties");
                            p1.load(f1);
                            contenttype = p1.getProperty(extension.trim().toUpperCase(), "");
                            System.out.println("content type in view attachmants page=" + contenttype);
                        }
                        System.out.println("content type=" + contenttype);
                        System.out.println("content type=" + contenttype);
                        response.setContentType(contenttype);
                        java.io.OutputStream ros = response.getOutputStream();
                        java.nio.channels.WritableByteChannel wbc = java.nio.channels.Channels.newChannel(ros);
                        wbc.write(bb);
                        System.out.println("here is the error");
                    } else if (atc.size() > 1) {
                        System.out.println("more than one attachments");
                        path = "/jsp/cataloguing/Attachments-Urls.jsp";
                        System.out.println(path);
                        RequestDispatcher rd = request.getRequestDispatcher(path);
                        System.out.println(atc.size());
                        System.out.println("attachments are set to request");
                        java.util.Vector v2 = (java.util.Vector) request.getAttribute("Attachments");
                        System.out.println(v2.size());
                        rd.forward(request, response);
                    }
                }
            } else if (type.equals("FROM")) {
                System.out.println("this is from Attachments page");
                System.out.println(catrecid);
                System.out.println(ownlibid);
                filename1 = request.getParameter("title1");
                System.out.println("file name send =" + filename1);
                String filepath = ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords";
                filepath += "/CAT_" + catrecid + "_" + ownlibid;
                System.out.println("file path=" + filepath);
                java.io.File actualfile = new java.io.File(filepath + "/" + filename1);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(actualfile)).getChannel();
                int fileLength = (int) fc.size();
                System.out.println("file lengt=" + fileLength);
                System.out.println("from attachments page");
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                System.out.println("calling1");
                System.out.println("---" + bb.capacity());
                java.util.StringTokenizer stk = new java.util.StringTokenizer(filename1, ".");
                stk.nextToken();
                String extension = stk.nextToken();
                System.out.println("extension=" + extension);
                String contenttype = "";
                try {
                    contenttype = java.util.ResourceBundle.getBundle("server").getString(extension.trim().toUpperCase());
                } catch (Exception exp) {
                    java.util.Properties p1 = new java.util.Properties();
                    java.io.File propfile = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/SystemFiles/ContentTypes.properties");
                    System.out.println("----" + propfile.getPath());
                    java.io.FileInputStream f1 = new java.io.FileInputStream(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.util.ResourceBundle.getBundle("server").getString("SystemFilesPath") + "/ContentTypes.properties");
                    p1.load(f1);
                    contenttype = p1.getProperty(extension.trim().toUpperCase(), "");
                    System.out.println("content type in view attachmants page=" + contenttype);
                }
                System.out.println("content type=" + contenttype);
                System.out.println("content type=" + contenttype);
                response.setContentType(contenttype);
                java.nio.channels.WritableByteChannel wbc = java.nio.channels.Channels.newChannel(response.getOutputStream());
                wbc.write(bb);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return (v1);
    }
}
