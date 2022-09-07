class BackupThread extends Thread {
    public Object getValueAt(int row, int column) {
        switch(column) {
            case INDEX_COLUMN:
                return new Integer(row + 1);
            case NAME_COLUMN:
                return scopeModel.getChannelModel(row).getChannelName();
            case ENABLE_COLUMN:
                return new Boolean(scopeModel.getChannelModel(row).isEnabled());
            case SCALE_COLUMN:
                return scaleFormat.format(scopeModel.getChannelModel(row).getSignalScale());
            case OFFSET_COLUMN:
                return offsetFormat.format(scopeModel.getChannelModel(row).getSignalOffset());
            default:
                return "";
        }
    }
}
