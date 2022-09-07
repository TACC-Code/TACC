class BackupThread extends Thread {
    private boolean canCreateObject(String bucketName, String objectKey, File file) {
        if (controller.objectExists(bucketName, objectKey)) {
            String message = "File %s already exists in folder %s.\nDo you want to overwrite?";
            return display.confirmMessage("Oops", String.format(message, file.getName(), bucketName));
        }
        return true;
    }
}
