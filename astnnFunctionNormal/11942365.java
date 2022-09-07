class BackupThread extends Thread {
    public FieldPattern(boolean findDeclarations, boolean readAccess, boolean writeAccess, char[] name, char[] declaringQualification, char[] declaringSimpleName, int matchRule) {
        super(FIELD_PATTERN, findDeclarations, readAccess, writeAccess, name, matchRule);
        this.declaringQualification = isCaseSensitive() ? declaringQualification : CharOperation.toLowerCase(declaringQualification);
        this.declaringSimpleName = isCaseSensitive() ? declaringSimpleName : CharOperation.toLowerCase(declaringSimpleName);
        ((InternalSearchPattern) this).mustResolve = mustResolve();
    }
}
