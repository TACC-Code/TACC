class BackupThread extends Thread {
    public DataStore create(String code, String sSourceFilename) {
        LinkedHashMap<String, Object> hmParameters = new LinkedHashMap<String, Object>();
        DataStore oDataStore = new DataStore();
        HashSet<DatabaseQuery> hsQueries = new HashSet<DatabaseQuery>();
        DatabaseQuery[] aQueries;
        Configuration oConfiguration = Configuration.getInstance();
        String sDataStoresDir = oConfiguration.getBusinessModelDataSourcesDir();
        oDataStore.setCode(code);
        try {
            hmParameters.put(Database.QueryFields.DATA_STORE, code);
            hsQueries.add(new DatabaseQuery(Database.Queries.DATA_SOURCE_CREATE, hmParameters));
            hsQueries.add(new DatabaseQuery(Database.Queries.DATA_INDEX_CREATE, hmParameters));
            hsQueries.add(new DatabaseQuery(Database.Queries.DATA_INDEX_CREATE_SEQUENCE, hmParameters));
            hsQueries.add(new DatabaseQuery(Database.Queries.DATA_INDEX_CREATE_WORDS, hmParameters));
            hsQueries.add(new DatabaseQuery(Database.Queries.DATA_USER_CREATE, hmParameters));
            aQueries = new DatabaseQuery[hsQueries.size()];
            this.oAgentDatabase.executeQueries(hsQueries.toArray(aQueries));
            AgentFilesystem.writeFile(sDataStoresDir + Strings.BAR45 + code + Common.FileExtensions.XML, (sSourceFilename != null) ? AgentFilesystem.readFile(sSourceFilename) : "");
        } catch (Exception oException) {
            this.remove(code);
            throw new DataException(ErrorCode.CREATE_DATASTORE, code, oException);
        }
        return oDataStore;
    }
}
