class BackupThread extends Thread {
    public static OBOSession convertFiles(OBOFileAdapter.OBOAdapterConfiguration readConfig, OBOFileAdapter.OBOAdapterConfiguration writeConfig, boolean parseObsoleteComments, boolean writeObsoleteComments, boolean fixDbxrefs, List scripts) throws Exception {
        OBOFileAdapter adapter = new OBOFileAdapter();
        OBOSession session = adapter.doOperation(OBOAdapter.READ_ONTOLOGY, readConfig, null);
        if (parseObsoleteComments) {
            parseComments(session);
        }
        if (writeObsoleteComments) {
            writeComments(session);
        }
        if (fixDbxrefs) {
            fixDbxrefs(session);
        }
        if (readConfig.getSemanticParser() != null) {
            SemanticParser semanticParser = readConfig.getSemanticParser();
            semanticParser.index(session);
            Collection<? extends HistoryItem> items = semanticParser.parseTerms();
            for (HistoryItem item : items) {
                logger.error("  change:" + item);
            }
            semanticParser.apply(items);
            if (semanticParser.getNamer() != null) {
                semanticParser.apply(semanticParser.getNamer().generateSynonymChanges(session));
                semanticParser.apply(semanticParser.getNamer().generateDefinitionChanges(session));
            }
            logger.info("SEMANTIC PARSER REPORT:");
            for (String report : semanticParser.getReports()) {
                System.out.println(report);
            }
        }
        for (Object sw : scripts) {
            ScriptWrapper wrapper = (ScriptWrapper) sw;
            runScript(session, wrapper.getScript(), wrapper.getArgs());
        }
        logger.info("About to write files... session object count = " + session.getObjects().size());
        logger.info("writePath = " + writeConfig.getWritePath());
        logger.info("savePath = " + writeConfig.getSaveRecords());
        adapter.doOperation(OBOAdapter.WRITE_ONTOLOGY, writeConfig, session);
        return session;
    }
}
