class BackupThread extends Thread {
    @Override
    protected void executeCommand() {
        String currentDir = getCurrentDir();
        for (String fileName : files) {
            try {
                fileName = resolvePath(currentDir, fileName);
                int ret = parser.device.getFilesystem().runInputFileJob(fileName, new InputFileJob() {

                    @Override
                    public int workOnFile(InputStream input) throws Exception {
                        Scanner sc = new Scanner(input);
                        while (sc.hasNextLine()) {
                            parser.getShell().printLine(sc.nextLine());
                        }
                        return 0;
                    }
                });
                if (ret < 0) throw new FileNotFoundException();
            } catch (FileNotFoundException ex) {
                parser.getShell().printLine("cat: " + fileName + ": file not found");
            }
        }
    }
}
