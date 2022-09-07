class BackupThread extends Thread {
    public boolean commit() throws LoginException {
        Map settings = super.getSettings();
        if (super.getCurrentMode().equals(AuthenticationModule.OUTGOING_MODE)) {
            String wikiGroup = (String) settings.get(WIKI_GROUP);
            String autoSignonUrl = (String) settings.get(AUTOSIGNON_URL);
            String secret = (String) settings.get(SECRET);
            String institutionName = (String) settings.get(INSTITUTIONNAME);
            String sectionName = (String) settings.get(SECTIONNAME);
            String userName = (String) settings.get(USERNAME);
            String readPassword = (String) settings.get(READPASSWORD);
            String writePassword = (String) settings.get(WRITEPASSWORD);
            String uploadPassword = (String) settings.get(UPLOADPASSWORD);
            String attrPassword = (String) settings.get(ATTRPASSWORD);
            String studentRight = (String) settings.get(STUDENT);
            String skin = (String) settings.get(SKIN);
            String sectionId = this.getCurrentLearningContextId().toString();
            if (autoSignonUrl == null || secret == null || autoSignonUrl.trim().length() == 0 || secret.trim().length() == 0) {
                throw new LoginException("A required setting is missing (AUTOSIGNONURL, SECRET)");
            }
            try {
                ContextSDK context = new ContextSDK();
                SessionVO session = context.getCurrentSession();
                Long lcid = super.getCurrentLearningContextId();
                LearningCtxtVO lc = context.getLearningContext(session, lcid.longValue());
                SubjectVO subject = session.getSubject();
                long[] lcids = context.getLearningContextIDs(session);
                String[] roles = context.getRoleIDs(session, lcids[0]);
                RoleVO role = context.getRoleDefinition(session, roles[0]);
                String roleName = role.getRoleDefinitionName();
                String password = "";
                if (roleName.equals("SDES") || roleName.equals("SINS")) {
                    password = uploadPassword;
                } else if (studentRight.equals("rw")) {
                    password = writePassword;
                } else if (studentRight.equals("rwu")) {
                    password = uploadPassword;
                } else {
                    password = readPassword;
                }
                String[] paramArray = { userName, roleName, sectionId, sectionName, skin, password, readPassword, writePassword, uploadPassword, attrPassword };
                String mac = calculateMac(paramArray, secret);
                Hashtable params = new Hashtable();
                params.put("username", userName);
                params.put("role", roleName);
                params.put("course_id", sectionId);
                params.put("course_name", sectionName);
                params.put("rp", readPassword);
                params.put("wp", writePassword);
                params.put("up", uploadPassword);
                params.put("ap", attrPassword);
                params.put("password", password);
                params.put("skin", skin);
                params.put("wiki_group", wikiGroup);
                params.put("MAC", mac);
                super.setUrlParameters(params);
                super.setRedirectUrl(autoSignonUrl);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }
}
