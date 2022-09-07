class BackupThread extends Thread {
    @Override
    protected void failed(Throwable e, Description description) {
        final String testOutputDirectory = "./target";
        WebDriver webdriver = WebDriverManager.getWebdriver();
        TakesScreenshot takesScreenshot = (TakesScreenshot) webdriver;
        String pageSource = webdriver.getPageSource();
        File screenshotAs = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String path = testOutputDirectory + "/failed-webdriver-tests" + "/" + BUILD_INDENTIFIER + "/" + description.getClassName() + "_" + description.getMethodName() + "/";
        try {
            FileUtils.copyFile(screenshotAs, new File(path + "screenshot.png"));
            FileUtils.writeStringToFile(new File(path + "source.html"), pageSource);
            e.printStackTrace(new PrintStream(new File(path + "trace.txt")));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
