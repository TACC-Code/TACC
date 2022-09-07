class BackupThread extends Thread {
    public void run() {
        super.run();
        if (UiUtil.getActiveTableEditor().getName().equals("日志")) {
            if (parent.isMulti()) return;
            int index = parent.getTv().getTable().getSelectionIndex();
            if (index != -1) {
                Work[] list = parent.getData().getWorks();
                if (index < list.length - 1) {
                    Work selected = list[index];
                    list[index] = list[index + 1];
                    list[index].setWid(String.valueOf(index + 1));
                    list[index + 1] = selected;
                    list[index + 1].setWid(String.valueOf(index + 2));
                    parent.getTv().setInput(list);
                    parent.getTv().getTable().setSelection(index + 1);
                    parent.setDirty(true);
                }
            }
        } else {
            BindedTableViewer btv = UiUtil.getActiveTableEditor().getViewer();
            if (!btv.getModel().isEditable()) return;
            btv.getModel().moveDown(btv.getTable().getSelectionIndex());
            btv.dataReordered();
            btv.getTableCursor().setSelection(btv.getTable().getSelectionIndex(), btv.getTableCursor().getColumn());
            btv.getModel().setDirty(true);
        }
    }
}
