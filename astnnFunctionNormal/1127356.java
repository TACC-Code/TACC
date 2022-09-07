class BackupThread extends Thread {
    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        String submissionSalt = null;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            f = df.getInstance();
            submissionSalt = df.getSubmissionHashSalt();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        Assignment assignment = null;
        String assignmentString = pageContext.getParameter("assignment");
        if (assignmentString == null) {
            throw new ServletException("No assignment parameter given");
        }
        Long assignmentId = Long.valueOf(pageContext.getParameter("assignment"));
        Collection<String> fileNames = null;
        try {
            f.beginTransaction();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            IStaffInterfaceQueriesDAO staffInterfaceQueriesDao = f.getStaffInterfaceQueriesDAOInstance();
            assignment = assignmentDao.retrievePersistentEntity(assignmentId);
            if (!staffInterfaceQueriesDao.isStaffModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new DAOException("permission denied (not on module)");
            }
            templateContext.put("assignment", assignment);
            fileNames = assignmentDao.fetchRequiredFilenames(assignmentId);
            f.endTransaction();
        } catch (DAOException e) {
            f.abortTransaction();
            throw new ServletException("dao exception", e);
        }
        String securityCode = null;
        MessageDigest digest = null;
        HashSet<String> remainingFiles = new HashSet<String>(fileNames);
        try {
            digest = MessageDigest.getInstance("MD5");
            FileItemIterator fileIterator = pageContext.getUploadedFiles();
            while (fileIterator.hasNext()) {
                FileItemStream currentUpload = fileIterator.next();
                if (fileNames.contains(currentUpload.getFieldName())) {
                    InputStream is = currentUpload.openStream();
                    try {
                        byte buffer[] = new byte[1024];
                        int nread = -1;
                        long total = 0;
                        while ((nread = is.read(buffer)) != -1) {
                            total += nread;
                            digest.update(buffer, 0, nread);
                        }
                        if (total > 0) {
                            remainingFiles.remove(currentUpload.getFieldName());
                        }
                    } catch (IOException e) {
                        throw new DAOException("IO error returning file stream", e);
                    }
                }
            }
            if (remainingFiles.equals(fileNames)) {
                pageContext.performRedirect(pageContext.getPageUrl("staff", "test_hash") + "?assignment=" + assignmentId + "&missing=true");
                return;
            }
            securityCode = byteArrayToHexString(digest.digest());
        } catch (Exception e) {
            throw new ServletException("error hashing upload", e);
        }
        securityCode = securityCode + submissionSalt;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            securityCode = byteArrayToHexString(digest.digest(securityCode.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new ServletException("error salt-hashing upload", e);
        }
        templateContext.put("hash", securityCode);
        templateContext.put("now", new Date());
        templateContext.put("greet", pageContext.getSession().getPersonBinding().getChosenName());
        pageContext.renderTemplate(template, templateContext);
    }
}
