class BackupThread extends Thread {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream sis = request.getInputStream();
        User user = UserServiceFactory.getUserService().getCurrentUser();
        LocalUserPersistence pLocalUser = projectDao.getLocalUserbyUserId(user.getUserId());
        LanguagePersistence pLanguage = null;
        byte[] buf = new byte[8192];
        int len = 0;
        String limiter = null;
        String line = null;
        String disposition = null;
        boolean dataReading = false;
        String name = null;
        String filename = null;
        String description = null;
        String meaning = null;
        String parameters = null;
        File file = new File();
        String sProject = null;
        String locale = null;
        PersistenceManager pm = PersistenceManagerFactory.get().getPersistenceManager();
        try {
            Date start = new Date();
            Date end = null;
            while ((len = sis.readLine(buf, 0, 8096)) >= 0) {
                line = getLine(buf, len);
                if (limiter == null) {
                    limiter = line;
                    continue;
                }
                if (line.startsWith("Content-Disposition: ")) {
                    disposition = line.substring(21);
                    StringTokenizer st = new StringTokenizer(disposition, ";");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken().trim();
                        if (token.indexOf("=") >= 0) {
                            String key = token.substring(0, token.indexOf("="));
                            String value = token.substring(token.indexOf("=") + 2, token.length() - 1);
                            if ("name".equals(key)) {
                                name = value;
                            } else if ("filename".equals(key)) {
                                filename = value;
                            }
                        }
                    }
                    continue;
                }
                if (line.startsWith("Content-Type: ")) {
                    continue;
                }
                if (!dataReading && line.equals("")) {
                    dataReading = true;
                    if ("uploader".equals(name)) {
                        file.setFilename(filename);
                        if (GenericValidator.isBlankOrNull(locale)) {
                            file = service.addFileInProject(file, sProject);
                        }
                    }
                    continue;
                }
                if (line.startsWith(limiter)) {
                    dataReading = false;
                    continue;
                }
                if ("project".equals(name)) {
                    sProject = line;
                    boolean participate = service.doesUserParticipateInProject(user, sProject, new Role[] { Role.ADMINISTRATOR, Role.DEVELOPER });
                    if (!participate) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                    continue;
                }
                if ("locale".equals(name)) {
                    locale = line;
                    pLanguage = projectDao.getLanguageByLocale(locale);
                    continue;
                }
                if ("uploader".equals(name)) {
                    line = line.trim();
                    if ((line.startsWith("#")) || (line.startsWith("//"))) {
                        line = line.substring(2);
                        if (line.startsWith("Description")) {
                            description = line.substring(13);
                        } else if (line.startsWith("Meaning")) {
                            meaning = line.substring(9);
                        } else if (line.startsWith("0=")) {
                            parameters = line;
                        }
                    } else {
                        int pos = line.indexOf("=");
                        if (pos > 0) {
                            String key = line.substring(0, pos);
                            String value = line.substring(pos + 1);
                            Project project = new Project();
                            project.setId(sProject);
                            Entry entry = new Entry();
                            entry.setKey(key);
                            entry.setContent(value);
                            entry.setProject(project);
                            entry.setMeaning(meaning);
                            entry.setParameters(parameters);
                            entry.setDescription(description);
                            if (GenericValidator.isBlankOrNull(locale)) {
                                service.addEntryInFile(entry, file, pm);
                            } else {
                                Translation translation = new Translation();
                                translation.setEntry(entry);
                                translation.setLocale(locale);
                                translation.setTranslation(value);
                                service.addTranslationByKey(translation, pLocalUser, pLanguage, pm);
                            }
                            description = null;
                            meaning = null;
                            parameters = null;
                        }
                    }
                }
                end = new Date();
                log.info("Processing: " + line + " [" + (end.getTime() - start.getTime()) + "ms]");
                start = new Date();
            }
            response.getOutputStream().write("Upload successful.".getBytes());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (TranslationAlreadyExistException e) {
            response.getOutputStream().write("Attempt to translate a term already translated.".getBytes());
            log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            pm.close();
        }
    }
}
