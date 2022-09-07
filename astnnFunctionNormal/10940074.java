class BackupThread extends Thread {
    public void moveObject(String sourceUri, Map sourceMap, String targetUri, Map targetMap, final boolean overwrite) throws CCClientException, NoAccessException, NoSuchObjectException, AlreadyExistsException, TypeMismatchException {
        cache.remove(sourceUri);
        cache.remove(targetUri);
        CCExecutionTemplate.execute(serverName, userName, sourceMap, targetMap, CrossContextConstants.OPERATION_MOVE_OBJECT, new CCClientCallback() {

            public void additionalInput(HttpServletRequest req) {
                req.setAttribute(CrossContextConstants.OVERWRITE, Boolean.valueOf(overwrite));
            }
        });
    }
}
