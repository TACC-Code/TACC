class BackupThread extends Thread {
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        new File(pathDest).mkdirs();
        final int BUFFER = 2048;
        try {
            monitor.beginTask(PropertiesUtil.getMessage("dialog.progressDownload.description", "spider"), downloadSize);
            for (String pathZip : pathZips) {
                BufferedInputStream buffInputStream = null;
                try {
                    URL url = new URL(pathZip);
                    InputStream is = url.openStream();
                    buffInputStream = new BufferedInputStream(is);
                } catch (MalformedURLException e1) {
                    FileInputStream fis = new FileInputStream(pathZip);
                    buffInputStream = new BufferedInputStream(fis);
                }
                String pathTemplateList1 = null;
                String pathTemplateList2 = null;
                BufferedOutputStream dest = null;
                ZipInputStream zis = new ZipInputStream(buffInputStream);
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (monitor.isCanceled()) {
                        return Status.CANCEL_STATUS;
                    }
                    monitor.subTask(PropertiesUtil.getMessage("dialog.progressDownload.status", new String[] { entry.getName() }, "spider"));
                    int count;
                    byte data[] = new byte[BUFFER];
                    String pathFile = pathDest + File.separator + entry.getName();
                    File file = new File(pathFile);
                    if (entry.isDirectory()) {
                        file.mkdir();
                        continue;
                    }
                    FileOutputStream fos = new FileOutputStream(pathFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                        monitor.worked(count);
                    }
                    dest.flush();
                    dest.close();
                }
                zis.close();
            }
            List<RepositoryTemplateInfo> templateList = new ArrayList<RepositoryTemplateInfo>();
            String pathFileTemplateList = pathDest + File.separator + "templateList.xml";
            for (String urlTemplateInfo : urlsTemplateInfo) {
                RepositoryTemplateInfo templateInfo = mapTemplateInfo.get(urlTemplateInfo);
                templateList.add(templateInfo);
            }
            File fileTemplateList = new File(pathFileTemplateList);
            if (fileTemplateList.exists()) {
                List<RepositoryTemplateInfo> oldTemplateList = (List<RepositoryTemplateInfo>) XmlManager.loadXml(pathFileTemplateList, XML_TYPE.TEMPLATE_LIST);
                old: for (RepositoryTemplateInfo oldTemplateInfo : oldTemplateList) {
                    for (RepositoryTemplateInfo newTemplateInfo : templateList) {
                        if (oldTemplateInfo.getFolder().equals(newTemplateInfo.getFolder())) {
                            continue old;
                        }
                    }
                    templateList.add(oldTemplateInfo);
                }
            }
            XmlManager.writeXml(templateList, pathFileTemplateList, XML_TYPE.TEMPLATE_LIST);
        } catch (Exception e) {
            logger.error(e);
            return new Status(Status.ERROR, SpiderPlugin.ID, e.getMessage());
        } finally {
            monitor.done();
        }
        return Status.OK_STATUS;
    }
}
