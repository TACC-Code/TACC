class BackupThread extends Thread {
    @Override
    protected final synchronized void setCurrentData(List<HardwareValueData> dataList) {
        for (HardwareValueData dataValue : dataList) {
            if (!getPreferredDataUnit().equals(dataValue.getDataObject().getUnit())) {
                logger.debug("Converting hardware data [CH-" + dataValue.getChannel() + ", " + dataValue.getDataString() + ", hardwareAddr=" + getHardwareAddr() + "]");
                dataValue = dataValue.convertDataUnit(getPreferredDataUnit());
                logger.debug("Hardware data converted [CH-" + dataValue.getChannel() + ", " + dataValue.getDataString() + ", hardwareAddr=" + getHardwareAddr() + "]");
            }
        }
        super.setCurrentData(dataList);
        if (doesSupportMinMaxValues()) {
            setMaxValueList(dataList);
            setMinValueList(dataList);
        }
        if (firstPoll) {
        }
        firstPoll = false;
    }
}
