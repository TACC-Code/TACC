class BackupThread extends Thread {
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (obj.getClass() != this.getClass())) return false;
        final ParameterAndFormProtection other = (ParameterAndFormProtection) obj;
        return this.allParameterNames.equals(other.allParameterNames) && this.requiredParameterNames.equals(other.requiredParameterNames) && this.readwriteParameterNames.equals(other.readwriteParameterNames) && this.readonlyFieldsName2ExpectedValues.equals(other.readonlyFieldsName2ExpectedValues) && this.hiddenFieldsName2RemovedValues.equals(other.hiddenFieldsName2RemovedValues) && this.selectboxFieldsName2AllowedValues.equals(other.selectboxFieldsName2AllowedValues) && this.radiobuttonFieldsName2AllowedValues.equals(other.radiobuttonFieldsName2AllowedValues) && this.checkboxFieldsName2AllowedValues.equals(other.checkboxFieldsName2AllowedValues) && this.requestParamName2MinimumValueCount.equals(other.requestParamName2MinimumValueCount) && this.requestParamName2MaximumValueCount.equals(other.requestParamName2MaximumValueCount);
    }
}
