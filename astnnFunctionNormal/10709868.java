class BackupThread extends Thread {
    public void renameSecretTokenParameterName(final String oldParameterName, final String newParameterName) {
        renameParameterNameWithinSet(this.allParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinSet(this.requiredParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinSet(this.readwriteParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.readonlyFieldsName2ExpectedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.hiddenFieldsName2RemovedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.selectboxFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.radiobuttonFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.checkboxFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.requestParamName2MinimumValueCount, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.requestParamName2MaximumValueCount, oldParameterName, newParameterName);
    }
}
