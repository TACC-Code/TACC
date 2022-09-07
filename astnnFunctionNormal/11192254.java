class BackupThread extends Thread {
    private LibraryInternalItem registerItemInternal(String name, String desc, String path, AtomFactory factory, boolean overwrite, boolean internal) throws LibraryException {
        LibraryInternalItem parent = lookup(path);
        if (parent == null) throw new LibraryException("Path not found");
        if (!overwrite && parent.children.containsKey(name)) throw new LibraryException("Library Element already registered");
        LibraryInternalItem newe = new LibraryInternalItem(name, desc, parent, factory, internal);
        parent.put(name, newe);
        return newe;
    }
}
