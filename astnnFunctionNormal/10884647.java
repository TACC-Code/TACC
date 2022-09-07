class BackupThread extends Thread {
    @Override
    public RefactoringStatus checkInitialConditions(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
        mUnit = null;
        mTokenString = null;
        RefactoringStatus status = new RefactoringStatus();
        try {
            monitor.beginTask("Checking preconditions...", 6);
            if (mMode != Mode.EDIT_SOURCE) {
                monitor.worked(6);
                return status;
            }
            if (!checkSourceFile(mFile, status, monitor)) {
                return status;
            }
            try {
                mUnit = JavaCore.createCompilationUnitFrom(mFile);
                if (mUnit.isReadOnly()) {
                    status.addFatalError("The file is read-only, please make it writeable first.");
                    return status;
                }
                if (!findSelectionInJavaUnit(mUnit, status, monitor)) {
                    return status;
                }
            } catch (Exception e) {
            }
            if (mUnit != null) {
                monitor.worked(1);
                return status;
            }
            if (mFile != null && AndroidConstants.EXT_XML.equals(mFile.getFileExtension())) {
                IPath path = mFile.getFullPath();
                if (path.segmentCount() == 4) {
                    if (path.segment(1).equalsIgnoreCase(SdkConstants.FD_RESOURCES)) {
                        if (!findSelectionInXmlFile(mFile, status, monitor)) {
                            return status;
                        }
                    }
                }
            }
            if (!status.isOK()) {
                status.addFatalError("Selection must be inside a Java source or an Android Layout XML file.");
            }
        } finally {
            monitor.done();
        }
        return status;
    }
}
