class BackupThread extends Thread {
    private AttachedFile getAttachedFile(AttachType type, int folderId, String mailId, String fileId, String fileName) throws IOException {
        log.debug("# 添付ファイルのダウンロード " + type + "/" + mailId + "/" + fileId);
        StringBuilder query = new StringBuilder();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        query.append("folder.id=" + Integer.toString(folderId));
        query.append("&folder.mail.id=" + mailId);
        if (type == AttachType.Attach) {
            query.append("&folder.attach.id=" + fileId);
        } else {
            query.append("&folder.mail.img.id=" + fileId);
        }
        query.append("&cdflg=0");
        HttpGet get = null;
        try {
            if (type == AttachType.Attach) {
                get = new HttpGet(AttachedFileUrl + "?" + query.toString());
            } else {
                get = new HttpGet(InlineFileUrl + "?" + query.toString());
            }
            addDumyHeader(get);
            HttpResponse res = this.executeHttp(get);
            AttachedFile file = new AttachedFile();
            file.setFolderId(folderId);
            file.setMailId(mailId);
            file.setId(fileId);
            file.setFilename(fileName);
            HttpEntity entity = res.getEntity();
            file.setContentType(entity.getContentType().getValue());
            file.setData(EntityUtils.toByteArray(entity));
            log.info("ファイル名   " + file.getFilename());
            log.info("Content-type " + file.getContentType());
            log.info("サイズ       " + file.getData().length);
            return file;
        } finally {
            get.abort();
        }
    }
}
