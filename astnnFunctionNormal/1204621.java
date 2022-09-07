class BackupThread extends Thread {
    public static IWcmChannelFolder getChannelFolderByPath(IDfSessionManager sessionManager, String docbase, Locale locale, String path) throws DfException {
        IWcmAppContext appContext = getWcmAppContext(sessionManager, docbase, locale);
        IWcmChannelFolder folder = WcmFolderMapUtil.getChannelFolderByPath(appContext, path, sessionManager.getSession(docbase));
        return folder;
    }
}
