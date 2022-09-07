class BackupThread extends Thread {
    public ValidateResult validate(final DigitalObject digitalObject, final URI format, final List<Parameter> parameters) {
        File file = FileUtils.writeInputStreamToTmpFile(digitalObject.getContent().read(), "pngcheck-temp", "bin");
        boolean valid = basicValidateOneBinary(file, format);
        ValidateResult result = new ValidateResult.Builder(format, new ServiceReport(Type.INFO, Status.SUCCESS, "OK")).ofThisFormat(valid).validInRegardToThisFormat(valid).build();
        return result;
    }
}
