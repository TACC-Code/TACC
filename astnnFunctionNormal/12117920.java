class BackupThread extends Thread {
    public String fileUpload() throws Exception {
        String targetDirectory = ServletActionContext.getServletContext().getRealPath("/img");
        if (upload != null) {
            targetFileName = generateFileName(uploadFileName);
            File target = new File(targetDirectory, targetFileName);
            FileUtils.copyFile(upload, target);
        } else {
            targetFileName = "default.png";
        }
        System.out.println(targetFileName);
        return SUCCESS;
    }
}
