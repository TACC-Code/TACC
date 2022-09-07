class BackupThread extends Thread {
    public void copyObject(String sourceUri, Map sourceMap, String targetUri, Map targetMap, final boolean overwrite, final boolean recursive) throws CCClientException, NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        cache.remove(targetUri);
        CCExecutionTemplate.execute(zoneName, userName, sourceMap, targetMap, CrossContextConstants.OPERATION_COPY_OBJECT, new CCClientCallback() {

            public void additionalInput(HttpServletRequest req) {
                req.setAttribute(CrossContextConstants.OVERWRITE, Boolean.valueOf(overwrite));
                req.setAttribute(CrossContextConstants.RECURSIVE, Boolean.valueOf(recursive));
            }
        });
    }
}
