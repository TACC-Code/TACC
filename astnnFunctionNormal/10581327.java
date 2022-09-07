class BackupThread extends Thread {
    protected TreeMap addSubMenu(TreeMap in, String[] path, String id, String label) {
        if (path.length == 0) {
            XMLLine line = new XMLLine(Context.XML_TAG);
            line.setId(id);
            line.setLabel(label);
            in.put(line.getItemName(), line);
        } else {
            String currentPathComponent = path[0];
            String[] nextPath = new String[path.length - 1];
            for (int i = 0; i < path.length - 1; i++) {
                nextPath[i] = path[i + 1];
            }
            TreeMap nextTreeMap = (TreeMap) in.get(currentPathComponent);
            nextTreeMap = addSubMenu(nextTreeMap, nextPath, id, label);
            in.put(currentPathComponent, nextTreeMap);
        }
        return in;
    }
}
