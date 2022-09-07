class BackupThread extends Thread {
    private FileCreationResult createOutputFiles(String outputDir, TemplateTextBlockList textBlocks) throws InterruptedException {
        String userdir = System.getProperty("user.dir");
        System.setProperty("user.dir", outputDir);
        boolean skipFile = false;
        int newFileCounter = 0;
        int skippedFilesCounter = 0;
        Iterator iterator = textBlocks.iterator();
        StringBuffer fileBuffer = new StringBuffer();
        while (iterator.hasNext()) {
            skipFile = false;
            TemplateTextBlock textBlock = (TemplateTextBlock) iterator.next();
            fileBuffer.append(textBlock.getText());
            if (textBlock.newFile()) {
                try {
                    File tempFile = null;
                    File file = new File(new File(textBlock.getFile()).getCanonicalPath());
                    String path = file.getCanonicalPath();
                    String name = file.getName();
                    path = path.substring(0, (path.length() - name.length()));
                    if (file.exists() && overwrite != Boolean.TRUE) {
                        if (overwrite == Boolean.FALSE) {
                            skippedFilesCounter++;
                            fileBuffer = new StringBuffer();
                            continue;
                        }
                        tempFile = new File(path + "_generated_" + name);
                        FileUtils.createFile(tempFile, fileBuffer.toString());
                        String diff = new Diff(file, tempFile).performDiff();
                        int choice = 999;
                        if (diff != null) {
                            while (choice == 999 || JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                                choice = JOptionPane.showOptionDialog(null, "The file " + file + " differs from the existing copy.\n" + "Do you want to overwrite this file?", "File already exists!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, JApplicationGen.DIALOGUE_OPTIONS, JApplicationGen.OPTION_VIEW_DIFF);
                                if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                                    new HtmlContentPopUp(null, "Diff report:", true, diff, false).show();
                                }
                            }
                            if (choice == JOptionPane.CLOSED_OPTION || JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO) {
                                skipFile = true;
                            } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO_ALL) {
                                overwrite = Boolean.FALSE;
                                skipFile = true;
                            } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_YES_ALL) {
                                overwrite = Boolean.TRUE;
                            }
                        } else {
                            skipFile = true;
                        }
                    }
                    if (skipFile) {
                        FileUtils.deleteFile(tempFile);
                        skippedFilesCounter++;
                        fileBuffer = new StringBuffer();
                        continue;
                    }
                    if (tempFile != null) {
                        FileUtils.deleteFile(file);
                        tempFile.renameTo(file);
                    } else {
                        FileUtils.createFile(file, fileBuffer.toString());
                    }
                    fileBuffer = new StringBuffer();
                    newFileCounter++;
                } catch (IOException exc) {
                    JApplicationGen.log("[Error] Create output file failed : " + textBlock.getFile());
                    JApplicationGen.log(exc.getMessage());
                }
            }
        }
        System.setProperty("user.dir", userdir);
        return new FileCreationResult(newFileCounter, skippedFilesCounter);
    }
}
