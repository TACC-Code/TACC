class BackupThread extends Thread {
    @Override
    public void init(OMElement configuration, ConfigManager mgr) {
        endpointMap = new ConcurrentHashMap<String, TraserEndpoint>();
        String rule = configuration.getAttributeValue(RULE);
        if (LOCAL_ONLY.equals(rule)) {
            this.rule = 0;
        } else if (STORE_ONLY.equals(rule)) {
            this.rule = 1;
        } else if (LOCAL_FIRST.equals(rule)) {
            this.rule = 2;
        } else if (STORE_FIRST.equals(rule)) {
            this.rule = 3;
        } else {
            throw new RuntimeException("The rule attribute contains an invalid value: " + rule);
        }
        String policyFile = configuration.getAttributeValue(POLICY);
        InputStream policyStream = ResourceLocator.findAsStream(policyFile);
        try {
            if (policyStream == null) {
                throw new RuntimeException("The policy file " + policyFile + " was not found.");
            }
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
            int read = 0;
            while ((read = policyStream.read(buffer)) >= 0) {
                if (read > 0) {
                    bout.write(buffer, 0, read);
                }
            }
            this.policy = bout.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Problem while loading the policy file " + policyFile, ex);
        } finally {
            if (policyStream != null) {
                try {
                    policyStream.close();
                } catch (IOException ex) {
                }
            }
        }
        this.repository = configuration.getAttributeValue(REPOSITORY);
        this.axisconfig = repository + "/client.axis2.xml";
        OMElement storeConfig = configuration.getFirstChildWithName(STORE);
        if (storeConfig != null) {
            Object storeObj = mgr.initInterface(storeConfig);
            if (storeObj instanceof CommunicatorStore) {
                store = (CommunicatorStore) storeObj;
            } else {
                throw new RuntimeException("The supplied Store object does not implement the CommunicatorStore interface");
            }
        } else {
            if (this.rule > 0) {
                throw new RuntimeException("The Store configuration must be present if the rule attribute is not LOCAL_ONLY!");
            }
        }
        Iterator<?> targets = configuration.getChildrenWithName(TARGET);
        while (targets.hasNext()) {
            OMElement target = (OMElement) targets.next();
            TraserEndpoint endpoint = new TraserEndpoint(target);
            endpointMap.put(endpoint.getUrl(), endpoint);
        }
    }
}
