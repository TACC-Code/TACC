class BackupThread extends Thread {
    private boolean checkSourceFile(IFile file, RefactoringStatus status, IProgressMonitor monitor) {
        if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
            status.addFatalError("The file is not synchronized. Please save it first.");
            return false;
        }
        monitor.worked(1);
        ResourceAttributes resAttr = file.getResourceAttributes();
        if (resAttr == null || resAttr.isReadOnly()) {
            status.addFatalError("The file is read-only, please make it writeable first.");
            return false;
        }
        monitor.worked(1);
        return true;
    }
}
