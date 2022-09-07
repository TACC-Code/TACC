class BackupThread extends Thread {
        public void run(final IProgressMonitor monitor) throws InvocationTargetException {
            uk.ac.reload.diva.util.IProgressMonitor divaMonitor = new uk.ac.reload.diva.util.IProgressMonitor() {

                public void setNote(String name) {
                    monitor.subTask("Copying " + name);
                }

                public boolean isCanceled() {
                    return monitor.isCanceled();
                }

                public void close() {
                    monitor.done();
                }
            };
            monitor.setTaskName("Copying files...");
            for (int i = 0; i < srcfiles.length; i++) {
                File src = new File(srcfiles[i]);
                File tgt = new File(targetParentFolder, src.getName());
                if (!src.equals(tgt)) {
                    try {
                        if (src.isDirectory()) {
                            FileUtils.copyFolder(src, tgt, divaMonitor);
                        } else {
                            monitor.subTask("Copying " + src.getName());
                            FileUtils.copyFile(src, tgt);
                        }
                    } catch (IOException ex) {
                        throw new InvocationTargetException(ex);
                    }
                }
            }
        }
}
