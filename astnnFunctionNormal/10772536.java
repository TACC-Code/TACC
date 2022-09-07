class BackupThread extends Thread {
    protected synchronized void setMinValueList(List<HardwareValueData> valueDataList) {
        boolean minValueUpdated = false;
        for (HardwareValueData value : valueDataList) {
            HardwareValueData oldValue = getMinValue(value.getChannel());
            if (oldValue == null) {
                value = value.convertDataUnit(this.getPreferredDataUnit());
                minValueList.add(value);
                minValueUpdated = true;
                logger.debug("Updating minimum value: [CH-" + value.getChannel() + " " + value.getDataString() + "]");
            } else if (oldValue != null && value.compareTo(oldValue) < 0) {
                value = value.convertDataUnit(this.getPreferredDataUnit());
                minValueList.add(value);
                minValueUpdated = true;
                logger.debug("Updating minimum value: [CH-" + value.getChannel() + " " + value.getDataString() + "]");
            }
        }
        if (minValueUpdated) {
            firePropertyChange(PROPERTYNAME_MINVALUE, getMaxValueList(), valueDataList);
        }
    }
}
