class BackupThread extends Thread {
    protected final synchronized void setMaxValueList(List<HardwareValueData> valueDataList) {
        boolean maxValueUpdated = false;
        for (HardwareValueData value : valueDataList) {
            HardwareValueData oldValue = getMaxValue(value.getChannel());
            if (oldValue == null) {
                value = value.convertDataUnit(this.getPreferredDataUnit());
                maxValueList.add(value);
                maxValueUpdated = true;
                logger.debug("Updating maximum value: [CH-" + value.getChannel() + " " + value.getDataString() + "]");
            } else if (oldValue != null && value.compareTo(oldValue) > 0) {
                value = value.convertDataUnit(this.getPreferredDataUnit());
                maxValueList.add(value);
                maxValueUpdated = true;
                logger.debug("Updating maximum value: [CH-" + value.getChannel() + " " + value.getDataString() + "]");
            }
        }
        if (maxValueUpdated) {
            firePropertyChange(PROPERTYNAME_MAXVALUE, getMaxValueList(), valueDataList);
        }
    }
}
