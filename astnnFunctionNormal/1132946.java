class BackupThread extends Thread {
    public Object getValueAt(int rowIndex, int columnIndex) {
        PortDefinition pd = (PortDefinition) portDefinitions.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return pd.getDisplayName();
            case 1:
                return pd.getChannelName();
            case 2:
                return new Float(pd.getRangeMin());
            case 3:
                return new Float(pd.getRangeMax());
            case 4:
                return Boolean.valueOf(pd.isLogarithmic());
        }
        return null;
    }
}
