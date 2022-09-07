class BackupThread extends Thread {
    public File exportTemplateArchive() {
        if (menueDef == null) setup();
        Theme theme2Export = menueDef.getTheme();
        Map<MenuePageType, PageSkin> skin2Export = menueDef.getSkin();
        File exportDir = new File(workingDir, theme2Export.getName());
        File archive = new File(workingDir, genArchiveName(theme2Export));
        File themeExportFile = new File(exportDir, String.format("%s-%s-Theme.xml", theme2Export.getName(), theme2Export.getAspect()));
        File skinExportFile = new File(exportDir, String.format("%s-Skin.xml", menueDef.getSkinName()));
        File targetFile;
        File tmp;
        try {
            exportDir.mkdirs();
            repository.performExport(theme2Export, themeExportFile);
            repository.performExport(skin2Export, menueDef.getSkinName(), skinExportFile);
        } catch (Throwable t) {
            throw new ApplicationException("failed to export menue template", t);
        }
        for (MenuePageType mpt : MenuePageType.values()) {
            List<ThemeElement<?>> elems = theme2Export.getThemeElements(mpt);
            for (ThemeElement<?> te : elems) {
                if (te.getImageName() != null) {
                    getLogger().info("check image [ " + te.getImageName().getAbsolutePath() + " ]");
                    targetFile = new File(exportDir, te.getImageName().getName());
                    if (!targetFile.exists()) FileUtils.copyFile(targetFile, te.getImageName());
                }
            }
        }
        for (MenuePageType mpt : skin2Export.keySet()) {
            PageSkin skin = skin2Export.get(mpt);
            for (MenueElementCategory cat : skin.getElements().keySet()) {
                ElementSkin eSkin = skin.getElementSkin(cat);
                if (eSkin.getFont() != null) {
                    getLogger().info("check font [" + eSkin.getFont() + "]");
                    if (fontCache.containsKey(eSkin.getFont())) {
                        for (File fontFile : fontCache.get(eSkin.getFont())) {
                            targetFile = new File(exportDir, fontFile.getName());
                            if (!targetFile.exists()) FileUtils.copyFile(targetFile, fontFile);
                        }
                    }
                }
                if (eSkin.getImage() != null) {
                    tmp = eSkin.getImage();
                    if (tmp.isFile() && tmp.canRead()) {
                        getLogger().info("check image " + tmp.getAbsolutePath());
                        targetFile = new File(exportDir, tmp.getName());
                        if (tmp.isFile() && tmp.canRead() && !targetFile.exists()) {
                            FileUtils.copyFile(targetFile, tmp);
                        }
                    }
                }
            }
        }
        createArchive(exportDir, archive);
        if (archive.exists()) FileUtils.removeDirectory(exportDir);
        return archive;
    }
}
