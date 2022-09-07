class BackupThread extends Thread {
    public String resUploadSubmit() {
        String path = contextPvd.getAppRealPath(getWeb().getResRootBuf().append(relPath).append(SPT).toString());
        if (resFile != null) {
            try {
                for (int i = 0; i < resFile.length; i++) {
                    FileUtils.copyFile(resFile[i], new File(path + FILE_SPT + resFileFileName[i]));
                }
                addActionMessage("�ϴ��ɹ���");
            } catch (IOException e) {
                addActionError("�ϴ�ʧ�ܣ�" + e.getMessage());
            }
        }
        return resList();
    }
}
