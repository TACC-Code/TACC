class BackupThread extends Thread {
    public Boolean populate(String code, String sSourceFilename) {
        Configuration oConfiguration = Configuration.getInstance();
        String sDataStoresDir = oConfiguration.getBusinessModelDataSourcesDir();
        if (!this.exists(code)) {
            this.create(code, sSourceFilename);
        }
        AgentFilesystem.writeFile(sDataStoresDir + Strings.BAR45 + code + Common.FileExtensions.XML, AgentFilesystem.readFile(sSourceFilename));
        return this.populate(code);
    }
}
