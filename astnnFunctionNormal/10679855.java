class BackupThread extends Thread {
    private WSRPPersistenceHelper() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            _jaxbContext = JAXBContext.newInstance("com.liferay.wsrp.consumer.admin", classLoader);
            _marshaller = _jaxbContext.createMarshaller();
            _marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            _unmarshaller = _jaxbContext.createUnmarshaller();
            _objectFactory = new ObjectFactory();
            String wsrpDataDir = WSRPConfig.getWSRPDataDirectory();
            FileUtil.mkdirs(wsrpDataDir);
            File wsrpConsumerFile = new File(wsrpDataDir + "/consumer.xml");
            if (!wsrpConsumerFile.exists()) {
                FileUtil.write(wsrpConsumerFile, StringUtil.read(classLoader, "com/liferay/wsrp/consumer/data/consumer.xml"));
            }
            _wsrpPortletsFileName = wsrpDataDir + "/wsrpportlets.xml";
            File wsrpPortletsFile = new File(_wsrpPortletsFileName);
            if (!wsrpPortletsFile.exists()) {
                wsrpPortletsFile.createNewFile();
            }
        } catch (Exception e) {
            _log.error(e, e);
        }
    }
}
