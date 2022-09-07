class BackupThread extends Thread {
    public int hashCode() {
        int hash = 31;
        hash = 7 * hash + this.allParameterNames.hashCode();
        hash = 7 * hash + this.requiredParameterNames.hashCode();
        hash = 7 * hash + this.readwriteParameterNames.hashCode();
        hash = 7 * hash + this.readonlyFieldsName2ExpectedValues.hashCode();
        hash = 7 * hash + this.hiddenFieldsName2RemovedValues.hashCode();
        hash = 7 * hash + this.selectboxFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.radiobuttonFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.checkboxFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.requestParamName2MinimumValueCount.hashCode();
        hash = 7 * hash + this.requestParamName2MaximumValueCount.hashCode();
        return hash;
    }
}
