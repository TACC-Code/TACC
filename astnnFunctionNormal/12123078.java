class BackupThread extends Thread {
    public DocumentPublish[] getMagazineList() throws Exception {
        DocumentCBF[] docCBF = new DocumentCBFDAO().getMagazineList(id, null);
        if (docCBF == null || docCBF.length == 0) {
            return null;
        }
        DocumentPublish[] result = new DocumentPublish[docCBF.length];
        for (int i = 0; i < docCBF.length; i++) {
            result[i] = new DocumentPublish(docCBF[i], this.getChannelPath(), this.getPublisher(), this.publishDate, this.validStartDate, this.validEndDate, this.orderNo);
        }
        return result;
    }
}
