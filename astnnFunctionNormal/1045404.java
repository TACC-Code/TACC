class BackupThread extends Thread {
    public static Style generateCommonStyleLayerDescriptor(final IProject project, final Feature fCategory, final Feature fData, final String dataSetName, final boolean defaultStyle) throws CoreException, IOException {
        if (project == null || fCategory == null || fData == null) throw new IllegalStateException();
        boolean bDefaultStyle = false;
        File templateSld = null;
        File workingSld = null;
        final Object objDataDescr = fCategory.getProperty(IGeodataSet.QN_ABSTRACT_GEODATA_MEMBER);
        if (objDataDescr instanceof Feature) {
            final Feature fDataDescr = (Feature) objDataDescr;
            final Object objSld = fDataDescr.getProperty(IDataStructureMember.QN_MAP_SYMBOLIZATION);
            if (!(objSld instanceof String) || ((String) objSld).equals("")) bDefaultStyle = true; else {
                final IFolder folder = NofdpCorePlugin.getProjectManager().getBaseProject().getFolder(NofdpIDSSConstants.NOFDP_PROJECT_GLOBAL_STYLES_FOLDER);
                final IFile file = folder.getFile((String) objSld);
                if (file.exists()) templateSld = file.getLocation().toFile(); else bDefaultStyle = true;
            }
        } else bDefaultStyle = true;
        if (defaultStyle) bDefaultStyle = true;
        if (!bDefaultStyle) {
            final IFolder folderCategory = BaseGeoUtils.getSubDirLocation(project, fCategory);
            final IFolder folderStyles = folderCategory.getFolder(NofdpIDSSConstants.NOFDP_PROJECT_GEODATA_STYLES_FOLDER);
            if (!folderStyles.exists()) folderStyles.create(true, true, new NullProgressMonitor());
            workingSld = new File(folderStyles.getLocation().toFile(), dataSetName + ".sld");
            FileUtils.copyFile(templateSld, workingSld);
            final IStyleReplacements replacer = new StyleReplacerCommon(fData, workingSld);
            final boolean bReplaced = replacer.replace();
            if (bReplaced) {
                final IFolder dir = BaseGeoUtils.getSubDirLocation(project, fCategory);
                final Style style = MapTool.generateStyle(fData, dir);
                return style;
            }
        }
        return null;
    }
}
