class BackupThread extends Thread {
    public static List<List<byte[]>> calculateMerkleParentNodes(List<byte[]> childNodes) {
        List<List<byte[]>> merkleTreeNodes = new ArrayList<List<byte[]>>();
        merkleTreeNodes.add(Collections.unmodifiableList(childNodes));
        List tmpNodes = childNodes;
        while (tmpNodes.size() > 1) {
            MessageDigest md = new Tiger();
            int size = (int) Math.ceil(tmpNodes.size() / 2.0);
            List<byte[]> parentNodes = new ArrayList<byte[]>(size);
            Iterator iterator = tmpNodes.iterator();
            while (iterator.hasNext()) {
                byte[] left = (byte[]) iterator.next();
                if (iterator.hasNext()) {
                    byte[] right = (byte[]) iterator.next();
                    md.reset();
                    md.update(MERKLE_IH_PREFIX);
                    md.update(left, 0, left.length);
                    md.update(right, 0, right.length);
                    byte[] result = md.digest();
                    parentNodes.add(result);
                } else {
                    parentNodes.add(left);
                }
            }
            merkleTreeNodes.add(0, parentNodes);
            tmpNodes = parentNodes;
        }
        return merkleTreeNodes;
    }
}
