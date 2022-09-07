class BackupThread extends Thread {
    void outputToXML(List<TransMeta> transformations, JobMeta job) throws IOException {
        Map<File, String> outputs = new LinkedHashMap<File, String>();
        for (TransMeta transMeta : transformations) {
            File file = new File(getTransFilePath(transMeta.getName()));
            transMeta.setFilename(file.getName());
            try {
                outputs.put(file, transMeta.getXML());
            } catch (KettleException e) {
                throw new RuntimeException(e);
            }
            if (monitor.isCancelled()) {
                cancel();
                return;
            }
        }
        for (int i = 1; i < job.nrJobEntries(); i++) {
            JobEntryTrans trans = (JobEntryTrans) (job.getJobEntry(i).getEntry());
            trans.setFileName(getTransFilePath(trans.getName()));
        }
        String fileName = settings.getFilePath();
        if (!fileName.toUpperCase().endsWith(".KJB")) {
            fileName += ".kjb";
        }
        job.setFilename(fileName);
        outputs.put(new File(fileName), job.getXML());
        UserPrompter up = session.createUserPrompter("The file {0} already exists. Overwrite?", UserPromptType.BOOLEAN, UserPromptOptions.OK_NOTOK_CANCEL, UserPromptResponse.NOT_OK, false, "Overwrite", "Don't Overwrite", "Cancel");
        for (File f : outputs.keySet()) {
            try {
                logger.debug("The file to output is " + f.getPath());
                if (f.exists()) {
                    UserPromptResponse overwriteOption = up.promptUser(f.getAbsolutePath());
                    if (overwriteOption == UserPromptResponse.OK) {
                        f.delete();
                    } else if (overwriteOption == UserPromptResponse.NOT_OK) {
                        continue;
                    } else if (overwriteOption == UserPromptResponse.CANCEL) {
                        cancel();
                        return;
                    } else {
                        throw new IllegalStateException("Unknown response value from user prompt: " + overwriteOption);
                    }
                }
                f.createNewFile();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
                out.write(outputs.get(f));
                out.flush();
                out.close();
                monitor.setProgress(monitor.getProgress() + 1);
                if (monitor.isCancelled()) {
                    cancel();
                    return;
                }
            } catch (IOException er) {
                tasksToDo.clear();
                tasksToDo.add("File " + f.getName() + " was not created");
                throw er;
            }
        }
    }
}
