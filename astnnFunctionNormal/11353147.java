class BackupThread extends Thread {
    public void delete() {
        try {
            deleteFile(prependPathAndReplaceSlashes(dis.readUTF()));
            writeFileListToSocket(prependPathAndReplaceSlashes(dis.readUTF()));
        } catch (Exception e) {
            errorHandling("Delete failed");
        }
    }
}
