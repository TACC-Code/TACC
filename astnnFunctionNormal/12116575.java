class BackupThread extends Thread {
    private String importAll() throws Exception {
        StringBuffer buffer = new StringBuffer();
        FileHandler fileHandler = new FileHandler();
        DatabaseHandler databaseHandler = new DatabaseHandler();
        FileSearchEngine fileSearchEngine = FileSearchEngine.getInstance();
        VersionManager fileVersionManager = FileVersionManager.getInstance();
        VersionManager databaseVersionManager = DatabaseVersionManager.getInstance();
        Collection virtualWikis = fileHandler.getVirtualWikiList();
        for (Iterator virtualWikiIterator = virtualWikis.iterator(); virtualWikiIterator.hasNext(); ) {
            String virtualWiki = (String) virtualWikiIterator.next();
            logger.info("importing for virtual wiki " + virtualWiki);
            buffer.append("imported for virtual wiki " + virtualWiki);
            buffer.append("<br/>");
            databaseHandler.addVirtualWiki(virtualWiki);
            Collection topics = fileSearchEngine.getAllTopicNames(virtualWiki);
            for (Iterator topicIterator = topics.iterator(); topicIterator.hasNext(); ) {
                String topicName = (String) topicIterator.next();
                List versions = fileVersionManager.getAllVersions(virtualWiki, topicName);
                logger.info("importing " + versions.size() + " versions of topic " + topicName);
                buffer.append("imported " + versions.size() + " versions of topic " + topicName);
                buffer.append("<br/>");
                for (Iterator topicVersionIterator = versions.iterator(); topicVersionIterator.hasNext(); ) {
                    TopicVersion topicVersion = (TopicVersion) topicVersionIterator.next();
                    databaseVersionManager.addVersion(virtualWiki, topicVersion.getTopicName(), topicVersion.getRawContents(), topicVersion.getRevisionDate());
                }
            }
            for (Iterator topicIterator = topics.iterator(); topicIterator.hasNext(); ) {
                String topicName = (String) topicIterator.next();
                logger.info("importing topic " + topicName);
                buffer.append("imported topic " + topicName);
                buffer.append("<br/>");
                databaseHandler.write(virtualWiki, fileHandler.read(virtualWiki, topicName), false, topicName);
            }
            Collection readOnlys = fileHandler.getReadOnlyTopics(virtualWiki);
            for (Iterator readOnlyIterator = readOnlys.iterator(); readOnlyIterator.hasNext(); ) {
                String topicName = (String) readOnlyIterator.next();
                logger.info("import read-only topicname " + topicName);
                buffer.append("imported read-only topicname " + topicName);
                buffer.append("<br/>");
                databaseHandler.addReadOnlyTopic(virtualWiki, topicName);
            }
            WikiMembers fileMembers = new FileWikiMembers(virtualWiki);
            WikiMembers databaseMembers = new DatabaseWikiMembers(virtualWiki);
            Collection members = fileMembers.getAllMembers();
            for (Iterator memberIterator = members.iterator(); memberIterator.hasNext(); ) {
                WikiMember wikiMember = (WikiMember) memberIterator.next();
                logger.info("importing member " + wikiMember);
                buffer.append("imported member " + wikiMember);
                buffer.append("<br/>");
                databaseMembers.addMember(wikiMember.getUserName(), wikiMember.getEmail(), wikiMember.getKey());
            }
            Collection fileNotifications = FileNotify.getAll(virtualWiki);
            for (Iterator iterator = fileNotifications.iterator(); iterator.hasNext(); ) {
                FileNotify fileNotify = (FileNotify) iterator.next();
                logger.info("importing notification " + fileNotify);
                buffer.append("imported notification " + fileNotify);
                buffer.append("<br/>");
                DatabaseNotify databaseNotify = new DatabaseNotify(virtualWiki, fileNotify.getTopicName());
                Collection notifyMembers = fileNotify.getMembers();
                for (Iterator notifyMemberIterator = notifyMembers.iterator(); notifyMemberIterator.hasNext(); ) {
                    String memberName = (String) notifyMemberIterator.next();
                    databaseNotify.addMember(memberName);
                }
            }
            Collection templates = fileHandler.getTemplateNames(virtualWiki);
            for (Iterator templateIterator = templates.iterator(); templateIterator.hasNext(); ) {
                String templateName = (String) templateIterator.next();
                logger.info("importing template " + templateName);
                buffer.append("imported template " + templateName);
                buffer.append("<br/>");
                databaseHandler.saveAsTemplate(virtualWiki, templateName, fileHandler.getTemplate(virtualWiki, templateName));
            }
        }
        return buffer.toString();
    }
}
