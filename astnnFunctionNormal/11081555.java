class BackupThread extends Thread {
    private void setupSpecialPage(String vWiki, String specialPage) throws Exception {
        if (!exists(vWiki, specialPage)) {
            logger.fine("Setting up " + specialPage);
            write(vWiki, WikiBase.readDefaultTopic(specialPage), true, specialPage);
        }
    }
}
