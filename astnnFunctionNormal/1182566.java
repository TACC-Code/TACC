class BackupThread extends Thread {
    protected void updateResource(IndexWriter writer, CmsIndexingThreadManager threadManager, CmsResource resource) throws CmsIndexException {
        if (resource.isInternal()) {
            return;
        }
        try {
            if (m_report != null) {
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SUCCESSION_1, String.valueOf(threadManager.getCounter() + 1)), I_CmsReport.FORMAT_NOTE);
                m_report.print(Messages.get().container(Messages.RPT_SEARCH_INDEXING_FILE_BEGIN_0), I_CmsReport.FORMAT_NOTE);
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, m_report.removeSiteRoot(resource.getRootPath())));
                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0), I_CmsReport.FORMAT_DEFAULT);
            }
            threadManager.createIndexingThread(m_cms, writer, resource, m_index, m_report);
        } catch (Exception e) {
            if (m_report != null) {
                m_report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_FAILED_0), I_CmsReport.FORMAT_WARNING);
            }
            if (LOG.isWarnEnabled()) {
                LOG.warn(Messages.get().getBundle().key(Messages.ERR_INDEX_RESOURCE_FAILED_2, resource.getRootPath(), m_index.getName()), e);
            }
            throw new CmsIndexException(Messages.get().container(Messages.ERR_INDEX_RESOURCE_FAILED_2, resource.getRootPath(), m_index.getName()));
        }
    }
}
