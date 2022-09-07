class BackupThread extends Thread {
        public void run() {
            boolean isNull = false;
            PublisherPublicKeyDigest sentID = null;
            synchronized (_idSyncer) {
                isNull = (null == _ccndId);
            }
            if (isNull) {
                try {
                    sentID = fetchCCNDId(_networkManager, _keyManager);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null == sentID) {
                    Log.severe(Log.FAC_NETMANAGER, "CCNDIdGetter: call to fetchCCNDId returned null.");
                }
                synchronized (_idSyncer) {
                    _ccndId = sentID;
                    if (Log.isLoggable(Log.FAC_NETMANAGER, Level.INFO)) Log.info(Log.FAC_NETMANAGER, "CCNDIdGetter: ccndId {0}", ContentName.componentPrintURI(sentID.digest()));
                }
            }
        }
}
