class BackupThread extends Thread {
    public final void addSubserviceCallMetricsToParent() {
        for (Class currClass : this.inputParameter.getCustomerClasses()) {
            for (int i = this.inputParameter.getMaxSubserviceLevel(); i >= 0; i--) {
                for (int j = this.inputParameter.getSubserviceParentRelationships().size() - 1; j >= 0; j--) {
                    List<Object> entries = this.inputParameter.getSubserviceParentRelationships().get(j);
                    if (entries.get(0).equals(currClass.getClassID()) && ((Integer) entries.get(3)).intValue() == i) {
                        this.tableMVAResponseTimePerStationAndClass.write((String) entries.get(1), currClass.getClassID(), this.tableMVAResponseTimePerStationAndClass.read((String) entries.get(1), currClass.getClassID()) + this.tableMVAResponseTimePerStationAndClass.read((String) entries.get(2), currClass.getClassID()));
                    }
                }
            }
        }
    }
}
