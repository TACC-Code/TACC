class BackupThread extends Thread {
    public void unthreadedCopyBetweenWindows(DN oldNodeDN, DataBrokerUnthreadedInterface externalDataSource, DN newNodeDN, boolean overwriteExistingData) throws NamingException {
        if (oldNodeDN == null || newNodeDN == null || externalDataSource == null) return;
        if (!externalDataSource.unthreadedExists(oldNodeDN)) return;
        boolean newNodeExists = this.unthreadedExists(newNodeDN);
        CBpbar progressBar = new CBpbar(CBUtility.getDefaultDisplay(), CBIntText.get("Cross-Window Copy"), CBIntText.get("estimate"));
        if (newNodeExists && oldNodeDN.getLowestRDN().equals(newNodeDN.getLowestRDN())) {
            if (overwriteExistingData) {
                System.out.println("REPLACING");
                this.unthreadedModify(new DXEntry(newNodeDN), null);
                unthreadedCopyBetweenWindowsLoop(oldNodeDN, externalDataSource, newNodeDN, progressBar);
            } else {
                System.out.println("MERGING");
                unthreadedMergeBetweenWindowsLoop(oldNodeDN, externalDataSource, newNodeDN, progressBar);
            }
        } else {
            System.out.println("COPYING");
            unthreadedCopyBetweenWindowsLoop(oldNodeDN, externalDataSource, newNodeDN, progressBar);
        }
    }
}
