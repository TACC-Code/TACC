class BackupThread extends Thread {
    @Override
    public String writeToString() throws ConditionThrowable {
        final LispThread thread = LispThread.currentThread();
        int maxLevel = Integer.MAX_VALUE;
        LispObject printLevel = SymbolConstants.PRINT_LEVEL.symbolValue(thread);
        if (printLevel instanceof Fixnum) maxLevel = printLevel.intValue();
        LispObject currentPrintLevel = _CURRENT_PRINT_LEVEL_.symbolValue(thread);
        int currentLevel = currentPrintLevel.intValue();
        if (currentLevel >= maxLevel) return "#";
        if (typep(SymbolConstants.CONDITION) != NIL) {
            StringOutputStream stream = new StringOutputStream();
            SymbolConstants.PRINT_OBJECT.execute(this, stream);
            return stream.getStringOutputString().getStringValue();
        }
        return unreadableString(typeOf().writeToString());
    }
}
