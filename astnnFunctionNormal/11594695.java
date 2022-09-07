class BackupThread extends Thread {
    public String toString() {
        String s = "Reads: " + m_reads + "\n" + "Writes: " + m_writes + "\n" + "Hits: " + m_hits + "\n" + "Misses: " + m_misses + "\n" + "Tree height: " + m_treeHeight + "\n" + "Number of data: " + m_data + "\n" + "Number of nodes: " + m_nodes + "\n";
        for (int cLevel = 0; cLevel < m_treeHeight; cLevel++) {
            s += ("Level " + cLevel + " pages: " + ((Integer) m_nodesInLevel.get(cLevel)).intValue() + "\n");
        }
        s += ("Splits: " + m_splits + "\n" + "Adjustments: " + m_adjustments + "\n" + "Query results: " + m_queryResults);
        return s;
    }
}
