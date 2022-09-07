class BackupThread extends Thread {
    public void rebuildIndex(IndexWriter writer, CmsIndexingThreadManager threadManager, CmsSearchIndexSource source) throws CmsIndexException {
        List resourceNames = source.getResourcesNames();
        Iterator i = resourceNames.iterator();
        while (i.hasNext()) {
            String resourceName = (String) i.next();
            List resources = null;
            try {
                resources = m_cms.readResources(resourceName, CmsResourceFilter.DEFAULT.addRequireFile());
            } catch (CmsException e) {
                if (m_report != null) {
                    m_report.println(Messages.get().container(Messages.RPT_UNABLE_TO_READ_SOURCE_2, resourceName, e.getLocalizedMessage()), I_CmsReport.FORMAT_WARNING);
                }
                if (LOG.isWarnEnabled()) {
                    LOG.warn(Messages.get().getBundle().key(Messages.LOG_UNABLE_TO_READ_SOURCE_2, resourceName, m_index.getName()), e);
                }
            }
            if (resources != null) {
                Iterator j = resources.iterator();
                while (j.hasNext()) {
                    CmsResource resource = (CmsResource) j.next();
                    updateResource(writer, threadManager, resource);
                }
            }
        }
    }
}
