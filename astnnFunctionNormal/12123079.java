class BackupThread extends Thread {
    private void getMagazineTree(DocumentPublish doc, List treeList) throws Exception {
        DocumentCBF[] magazine = DocumentCBF.getInstance(doc.id).getMagazineList(null);
        for (int i = 0; magazine != null && magazine.length > 0 && i < magazine.length; i++) {
            DocumentPublish tmp = new DocumentPublish(magazine[i], doc.getChannelPath(), doc.getPublisher(), doc.getPublishDate(), doc.getValidStartDate(), doc.getValidEndDate(), doc.getOrderNo());
            treeList.add(tmp);
        }
        treeList.add(this);
    }
}
