class BackupThread extends Thread {
    public void execute() throws BuildException {
        bundleDir = new File(mRootDir, bundleProperties.getApplicationName() + ".app");
        if (bundleDir.exists()) {
            Delete deleteTask = new Delete();
            deleteTask.setProject(getProject());
            deleteTask.setDir(bundleDir);
            deleteTask.execute();
        }
        if (mRootDir == null) throw new BuildException("Required attribute \"dir\" is not set.");
        if (mJarAttrs.isEmpty() && mJarFileSets.isEmpty() && mJarFileLists.isEmpty()) throw new BuildException("Either the attribute \"jar\" must " + "be set, or one or more jarfilelists or " + "jarfilesets must be added.");
        if (!mJarAttrs.isEmpty() && (!mJarFileSets.isEmpty() || !mJarFileLists.isEmpty())) throw new BuildException("Cannot set both the attribute " + "\"jars\" and use jar filesets/filelists.  Use only one or the other.");
        if (bundleProperties.getApplicationName() == null) throw new BuildException("Required attribute \"name\" is not set.");
        if (bundleProperties.getMainClass() == null) throw new BuildException("Required attribute \"mainclass\" is not set.");
        if (useOldPropertyNames()) bundleProperties.addJavaProperty(ABOUTMENU_KEY, bundleProperties.getCFBundleName());
        String antiAliasedProperty = useOldPropertyNames() ? "com.apple.macosx.AntiAliasedGraphicsOn" : "apple.awt.antialiasing";
        if (mAntiAliasedGraphics != null) bundleProperties.addJavaProperty(antiAliasedProperty, mAntiAliasedGraphics.toString());
        String antiAliasedTextProperty = useOldPropertyNames() ? "com.apple.macosx.AntiAliasedTextOn" : "apple.awt.textantialiasing";
        if (mAntiAliasedText != null) bundleProperties.addJavaProperty(antiAliasedTextProperty, mAntiAliasedText.toString());
        if (useOldPropertyNames() && (mLiveResize != null)) bundleProperties.addJavaProperty("com.apple.mrj.application.live-resize", mLiveResize.toString());
        String screenMenuBarProperty = useOldPropertyNames() ? "com.apple.macos.useScreenMenuBar" : "apple.laf.useScreenMenuBar";
        if (mScreenMenuBar != null) bundleProperties.addJavaProperty(screenMenuBarProperty, mScreenMenuBar.toString());
        if ((useOldPropertyNames() == false) && (mGrowbox != null)) bundleProperties.addJavaProperty("apple.awt.showGrowBox", mGrowbox.toString());
        if (useOldPropertyNames() && (mGrowboxIntrudes != null)) bundleProperties.addJavaProperty("com.apple.mrj.application.growbox.intrudes", mGrowboxIntrudes.toString());
        if (!mRootDir.exists() || (mRootDir.exists() && !mRootDir.isDirectory())) throw new BuildException("Destination directory specified by \"dir\" " + "attribute must already exist.");
        if (bundleDir.exists()) throw new BuildException("The directory/bundle \"" + bundleDir.getName() + "\" already exists, cannot continue.");
        log("Creating application bundle: " + bundleDir);
        if (!bundleDir.mkdir()) throw new BuildException("Unable to create bundle: " + bundleDir);
        mContentsDir = new File(bundleDir, "Contents");
        if (!mContentsDir.mkdir()) throw new BuildException("Unable to create directory " + mContentsDir);
        mMacOsDir = new File(mContentsDir, "MacOS");
        if (!mMacOsDir.mkdir()) throw new BuildException("Unable to create directory " + mMacOsDir);
        mResourcesDir = new File(mContentsDir, "Resources");
        if (!mResourcesDir.mkdir()) throw new BuildException("Unable to create directory " + mResourcesDir);
        mJavaDir = new File(mResourcesDir, "Java");
        if (!mJavaDir.mkdir()) throw new BuildException("Unable to create directory " + mJavaDir);
        if (mAppIcon != null) {
            try {
                File dest = new File(mResourcesDir, mAppIcon.getName());
                if (mVerbose) log("Copying application icon file to \"" + bundlePath(dest) + "\"");
                mFileUtils.copyFile(mAppIcon, dest);
            } catch (IOException ex) {
                throw new BuildException("Cannot copy icon file: " + ex);
            }
        }
        try {
            Iterator itor = bundleProperties.getDocumentTypes().iterator();
            while (itor.hasNext()) {
                DocumentType documentType = (DocumentType) itor.next();
                File iconFile = documentType.getIconFile();
                if (iconFile != null) {
                    File dest = new File(mResourcesDir, iconFile.getName());
                    if (mVerbose) log("Copying document icon file to \"" + bundlePath(dest) + "\"");
                    mFileUtils.copyFile(iconFile, dest);
                }
            }
        } catch (IOException ex) {
            throw new BuildException("Cannot copy document icon file: " + ex);
        }
        processJarAttrs();
        processJarFileSets();
        processJarFileLists();
        processExecAttrs();
        processExecFileSets();
        processExecFileLists();
        processResourceFileSets();
        processJavaFileSets();
        processResourceFileLists();
        processJavaFileLists();
        processExtraClassPathAttrs();
        processExtraClassPathFileSets();
        processExtraClassPathFileLists();
        copyHelpBooks();
        copyApplicationStub();
        writeInfoPlist();
        writePkgInfo();
    }
}
