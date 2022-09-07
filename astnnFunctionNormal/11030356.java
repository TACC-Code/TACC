class BackupThread extends Thread {
    private PagedResult getPagedProjects(int offset, int limit) throws RepositoryException {
        return new DefaultPagedResult(getSession(), "/jcr:root/projects/element(*, project)[\n" + "  managers/@groups = '" + group.getIdentifier().replaceAll("'", "''") + "'" + " or\n" + "  writers/@groups = '" + group.getIdentifier().replaceAll("'", "''") + "'" + " or\n" + "  readers/@groups = '" + group.getIdentifier().replaceAll("'", "''") + "'\n" + "]\n" + "order by @lastModified descending", offset, limit);
    }
}
