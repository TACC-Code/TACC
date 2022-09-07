    @SuppressWarnings("unchecked")
    public void importTemplateArchive(File importArchive, String sudoPassword) {
        if (repository == null) setup();
        Theme theme2Import = null;
        Map<MenuePageType, PageSkin> skin2Import = null;
        File fontTargetDir = new File(fontBaseDir, "vatemplates");
        File tmp;
        File targetFile;
        if (sysInfo.isSuse()) fontTargetDir = fontBaseDir;
        if (importArchive.exists() && importArchive.isFile() && importArchive.canRead()) {
            getLogger().info("found archive " + importArchive + " to extract");
            File importDir = extractArchive(importArchive);
            if (importDir == null || !(importDir.exists() && importDir.isDirectory() && importDir.canRead())) {
                throw new ApplicationException("failed to extract archive " + importArchive);
            }
            for (File archiveEntry : importDir.listFiles()) {
                if (archiveEntry.getName().toUpperCase().endsWith(".TTF")) {
                    getLogger().info(archiveEntry.getName() + " should be copied to fonts directory");
                    targetFile = new File(fontTargetDir, archiveEntry.getName());
                    FileUtils.copyFile(targetFile, archiveEntry, sudoPassword);
                    continue;
                }
                if (archiveEntry.getName().endsWith(".xml")) {
                    try {
                        Object any = repository.performImport(archiveEntry);
                        if (any instanceof Theme) theme2Import = (Theme) any; else if (any instanceof Map<?, ?>) skin2Import = (Map<MenuePageType, PageSkin>) any;
                    } catch (Throwable t) {
class BackupThread extends Thread {
                        throw new ApplicationException("failed to import " + archiveEntry, t);
                    }
                }
            }
            if (theme2Import != null) {
                if (menueDef.isKnownTheme(theme2Import)) {
                    String originalName = theme2Import.getName();
                    for (int i = 0; i < 10; i++) {
                        theme2Import.setThemeName(originalName + i);
                        if (!menueDef.isKnownTheme(theme2Import)) break;
                    }
                    if (menueDef.isKnownTheme(theme2Import)) {
                        throw new ApplicationException("theme does already exists and I could not guess an unused name");
                    }
                }
                try {
                    Transaction ta = taFactory.createTransaction();
                    ta.add(new TOSave<Theme>(theme2Import));
                    ta.execute();
                    if (ta.getStatus().equals(TransactionStatus.STATUS_COMMITTED)) {
                        for (MenuePageType mpt : MenuePageType.values()) {
                            List<ThemeElement<?>> elems = theme2Import.getThemeElements(mpt);
                            for (ThemeElement<?> te : elems) {
                                if (te.getImageName() != null) {
                                    targetFile = te.getImageName();
                                    if (targetFile == null) continue;
                                    if (targetFile.getName().length() < 2) continue;
                                    tmp = new File(importDir, targetFile.getName());
                                    if (tmp.exists()) {
                                        getLogger().info("should copy " + targetFile.getName() + " to " + targetFile);
                                        FileUtils.copyFile(targetFile, tmp, sudoPassword);
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    throw new ApplicationException("failed to import theme", t);
                }
            }
            if (skin2Import != null) {
                PageSkin any = skin2Import.values().iterator().next();
                if (menueDef.isKnownSkin(any.getName())) {
                    String tempName = any.getName();
                    for (int i = 0; i < 10; i++) {
                        tempName = any.getName() + i;
                        if (!menueDef.isKnownSkin(tempName)) break;
                    }
                    if (menueDef.isKnownSkin(tempName)) {
                        throw new ApplicationException("skin does already exists and I could not guess an unused name");
                    }
                    if (tempName.compareTo(any.getName()) != 0) {
                        for (MenuePageType mpt : skin2Import.keySet()) skin2Import.get(mpt).setName(tempName);
                    }
                }
                try {
                    Transaction ta = taFactory.createTransaction();
                    for (MenuePageType mpt : skin2Import.keySet()) ta.add(new TOSave<PageSkin>(skin2Import.get(mpt)));
                    ta.execute();
                    if (ta.getStatus().equals(TransactionStatus.STATUS_COMMITTED)) {
                        for (MenuePageType mpt : skin2Import.keySet()) {
                            PageSkin skin = skin2Import.get(mpt);
                            for (MenueElementCategory cat : skin.getElements().keySet()) {
                                ElementSkin eSkin = skin.getElementSkin(cat);
                                targetFile = eSkin.getImage();
                                if (targetFile == null) continue;
                                if (targetFile.getName().length() < 2) continue;
                                tmp = new File(importDir, targetFile.getName());
                                if (tmp.exists()) {
                                    getLogger().info("should copy " + targetFile.getName() + " to " + targetFile);
                                    FileUtils.copyFile(targetFile, tmp, sudoPassword);
                                }
                            }
                        }
                    }
                } catch (Throwable t) {
                    throw new ApplicationException("failed to import skin", t);
                }
            }
        }
    }
}
