class BackupThread extends Thread {
    private void generateInstallCode(File javaFile, String instTemplate, String classLoader) throws IOException {
        String tempString;
        PrintWriter writer = new PrintWriter(new FileWriter(javaFile));
        int read = 0;
        byte[] buf = new byte[128];
        InputStream is = getClass().getResourceAsStream("/" + instTemplate);
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader reader = new LineNumberReader(isr);
        System.out.println(VAGlobals.i18n("VAArchiver_GeneratingInstallClassCode"));
        String line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassName"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("public class " + instClassName_ + " {");
        writer.println("  private static final Class installClass=new " + instClassName_ + "().getClass();");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ArchivingMethod"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String ARCH_METHOD=\"" + archMethod_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> TargetType"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String TARGET_TYPE=\"" + currentTarget_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassOffset"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long ICLASS_OFFSET=" + installClassOffset_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> InstallClassSize"))) {
            writer.println(line);
            line = reader.readLine();
        }
        if (installClassSize_ != archOffset_) writer.println("  private static long ICLASS_SIZE=" + installClassSize_ + "L;"); else writer.println("  private static long ICLASS_SIZE=-1234543210L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ArchiveOffset"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long ARCH_OFFSET=" + archOffset_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> JarSize"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static long JAR_SIZE=" + jarSize_ + "L;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIMode"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_MODE=\"" + uiMode_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIBluescreen"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_BLUESCREEN=\"" + uiBluescreen_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> UIBluescreenColor"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String UI_BLUESCREEN_COLOR=\"" + uiBluescreenColor_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> DestPath"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String DEST_PATH=\"" + destPath_ + "\";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> AppInfo"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String APP_NAME=\"" + appName_ + "\";");
        if (appVersion_ != null) writer.println("  private static String APP_VERSION=\"" + appVersion_ + "\";"); else writer.println("  private static String APP_VERSION=null;");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> LinkInfos"))) {
            System.out.println("fred " + line);
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String LINK_SECTION_NAME=\"" + linkSectionName_ + "\";");
        writer.println("  private static String LINK_SECTION_ICON=\"" + linkSectionIcon_ + "\";");
        writer.println("  private static String LINK_ENTRY_NAME=\"" + linkEntryName_ + "\";");
        writer.println("  private static String LINK_ENTRY_ICON=\"" + linkEntryIcon_ + "\";");
        if (createUninstallShortcut_) tempString = "true"; else tempString = "false";
        writer.println("  private static boolean CREATE_UNINSTALL_SHORTCUT=" + tempString + ";");
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> LicenseKey"))) {
            writer.println(line);
            line = reader.readLine();
        }
        writer.println("  private static String LICENSE_KEY_SUPPORT_NAME=\"" + licenseKeySupportClassName_ + "\";");
        System.out.println(VAGlobals.i18n("VAArchiver_AppendingClassloader"));
        line = reader.readLine();
        while ((line != null) && (!line.startsWith("// --> ClassLoader"))) {
            writer.println(line);
            line = reader.readLine();
        }
        InputStream isClassLoader = getClass().getResourceAsStream("/" + classLoader);
        writer.println("  private static String[] CL_CLASS={");
        read = isClassLoader.read(buf);
        while (read > 0) {
            writer.println("\"" + codeLine(buf, read) + "\",");
            read = isClassLoader.read(buf);
        }
        isClassLoader.close();
        writer.println("  };\n}");
        reader.close();
        writer.close();
        is.close();
        isr.close();
    }
}
