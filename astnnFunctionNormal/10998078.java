class BackupThread extends Thread {
    public void searchTests() throws CoreException {
        String varPackageName = null;
        List<String> results = null;
        List<JUTestHelper> JUTestHelperList = null;
        varPackageName = EclipseUtil.getPackageFullName("persistence");
        results = FileUtil.findJavaTestFiles(varPackageName);
        if (results != null) {
            for (String test : results) {
                JUTestHelper testHelper = new JUTestHelper();
                test = test.replace("/", ".");
                test = test.substring(0, test.lastIndexOf(".java"));
                testHelper.setTargetClass(new ClassRepresentation(test));
                varPackageName = varPackageName + "dao";
                testHelper.setPackageName(varPackageName);
                testHelper.setAbsolutePath(EclipseUtil.getTestSourceLocation() + "/" + varPackageName.replace(".", "/") + "/");
                testHelper.setReadOnly(false);
                if (JUTestHelperList == null) {
                    JUTestHelperList = new ArrayList<JUTestHelper>();
                }
                JUTestHelperList.add(testHelper);
            }
        }
        varPackageName = EclipseUtil.getPackageFullName("business");
        results = FileUtil.findJavaTestFiles(varPackageName);
        if (results != null) {
            for (String test : results) {
                JUTestHelper testHelper = new JUTestHelper();
                test = test.replace("/", ".");
                test = test.substring(0, test.lastIndexOf(".java"));
                testHelper.setTargetClass(new ClassRepresentation(test));
                testHelper.setPackageName(varPackageName);
                testHelper.setAbsolutePath(EclipseUtil.getTestSourceLocation() + "/" + varPackageName.replace(".", "/") + "/");
                testHelper.setReadOnly(false);
                if (JUTestHelperList == null) {
                    JUTestHelperList = new ArrayList<JUTestHelper>();
                }
                JUTestHelperList.add(testHelper);
            }
        }
        varPackageName = EclipseUtil.getPackageFullName("managedbean");
        results = FileUtil.findJavaTestFiles(varPackageName);
        if (results != null) {
            for (String test : results) {
                JUTestHelper testHelper = new JUTestHelper();
                test = test.replace("/", ".");
                test = test.substring(0, test.lastIndexOf(".java"));
                testHelper.setTargetClass(new ClassRepresentation(test));
                testHelper.setPackageName(varPackageName);
                testHelper.setAbsolutePath(EclipseUtil.getTestSourceLocation() + "/" + varPackageName.replace(".", "/") + "/");
                testHelper.setReadOnly(false);
                if (JUTestHelperList == null) {
                    JUTestHelperList = new ArrayList<JUTestHelper>();
                }
                JUTestHelperList.add(testHelper);
            }
        }
        if (JUTestHelperList != null && JUTestHelperList.size() > 0) {
            Configurator reader = new Configurator();
            reader.writeTests(JUTestHelperList, getXml(), false);
        }
    }
}
