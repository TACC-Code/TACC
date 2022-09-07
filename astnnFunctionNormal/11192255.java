class BackupThread extends Thread {
    private void registerItemInternal(LibraryInternalItem item, String path, boolean overwrite) throws LibraryException {
        LibraryInternalItem parent = lookup(path);
        if (parent == null) throw new LibraryException("Path not found");
        if (!overwrite && parent.children.containsKey(item.item.getName())) throw new LibraryException("Library Element already registered");
        parent.put(item.item.getName(), item);
        item.parent = parent;
        item.item.parent = parent.item;
    }
}
