class BackupThread extends Thread {
    private void unPublishMagazine(String userId, boolean isBackProcess, DocumentPublish doc) throws Exception {
        DocumentCBF[] magazine = DocumentCBF.getInstance(doc.id).getMagazineList(null);
        for (int i = 0; magazine != null && magazine.length > 0 && i < magazine.length; i++) {
            DocumentPublish tmp = new DocumentPublish(magazine[i], doc.getChannelPath(), doc.getPublisher(), doc.getPublishDate(), doc.getValidStartDate(), doc.getValidEndDate(), doc.getOrderNo());
            tmp.unPublishMagazine(userId, isBackProcess, tmp);
        }
        boolean bool = isBackProcess && doc.getDoctypePath().startsWith(Const.DOCTYPE_PATH_MAGAZINE_HEAD) && doc.getDoctypePath().length() == 15;
        unPublish(userId, bool);
    }
}
