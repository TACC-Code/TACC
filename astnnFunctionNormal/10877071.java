class BackupThread extends Thread {
    public static UndoableModifyChannel startUndo(int channelId) {
        UndoableModifyChannel undoable = new UndoableModifyChannel(channelId);
        undoable.doAction = UNDO_ACTION;
        undoable.undoCaret = new UndoableCaretHelper();
        undoable.undoChannel = undoable.cloneChannel(undoable.getChannel());
        return undoable;
    }
}
