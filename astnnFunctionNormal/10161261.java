class BackupThread extends Thread {
    private static List<byte[]> calculateMerkleParentRow(List<byte[]> childNodes) {
        MessageDigest md = new Tiger();
        int size = childNodes.size();
        List<byte[]> parentRow = new ArrayList<byte[]>((int) Math.ceil(size / 2.0));
        Iterator<byte[]> childNodesIterator = childNodes.iterator();
        while (childNodesIterator.hasNext()) {
            byte[] leftNode = childNodesIterator.next();
            if (!childNodesIterator.hasNext()) {
                parentRow.add(leftNode);
                continue;
            }
            byte[] rightNode = childNodesIterator.next();
            md.reset();
            md.update(MERKLE_IH_PREFIX);
            md.update(leftNode, 0, leftNode.length);
            md.update(rightNode, 0, rightNode.length);
            byte[] result = md.digest();
            parentRow.add(result);
        }
        return parentRow;
    }
}
