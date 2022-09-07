class BackupThread extends Thread {
    public void updateResources(IndexWriter writer, CmsIndexingThreadManager threadManager, List resourcesToUpdate) throws CmsIndexException {
        if ((resourcesToUpdate == null) || resourcesToUpdate.isEmpty()) {
            return;
        }
        List resourcesAlreadyUpdated = new ArrayList(resourcesToUpdate.size());
        Iterator i = resourcesToUpdate.iterator();
        while (i.hasNext()) {
            CmsPublishedResource res = (CmsPublishedResource) i.next();
            CmsResource resource = null;
            try {
                resource = m_cms.readResource(res.getRootPath());
            } catch (CmsException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(Messages.get().getBundle().key(Messages.LOG_UNABLE_TO_READ_RESOURCE_2, res.getRootPath(), m_index.getName()), e);
                }
            }
            if (resource != null) {
                if (!resourcesAlreadyUpdated.contains(resource.getRootPath())) {
                    resourcesAlreadyUpdated.add(resource.getRootPath());
                    updateResource(writer, threadManager, resource);
                }
            }
        }
    }
}
