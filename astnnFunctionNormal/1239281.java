class BackupThread extends Thread {
    private void executeChildren(ByteBuffer bb, DataOutputStream os) throws JdwpException, IOException {
        ObjectId oid = idMan.readObjectId(bb);
        ThreadGroup group = (ThreadGroup) oid.getObject();
        ThreadGroup jdwpGroup = Thread.currentThread().getThreadGroup();
        int numThreads = group.activeCount();
        Thread allThreads[] = new Thread[numThreads];
        group.enumerate(allThreads, false);
        numThreads = 0;
        for (int i = 0; i < allThreads.length; i++) {
            Thread thread = allThreads[i];
            if (thread == null) break;
            if (!thread.getThreadGroup().equals(jdwpGroup)) numThreads++;
        }
        os.writeInt(numThreads);
        for (int i = 0; i < allThreads.length; i++) {
            Thread thread = allThreads[i];
            if (thread == null) break;
            if (!thread.getThreadGroup().equals(jdwpGroup)) idMan.getObjectId(thread).write(os);
        }
        int numGroups = group.activeCount();
        ThreadGroup allGroups[] = new ThreadGroup[numGroups];
        group.enumerate(allGroups, false);
        numGroups = 0;
        for (int i = 0; i < allGroups.length; i++) {
            ThreadGroup tgroup = allGroups[i];
            if (tgroup == null) break;
            if (!tgroup.equals(jdwpGroup)) numGroups++;
        }
        os.writeInt(numGroups);
        for (int i = 0; i < allGroups.length; i++) {
            ThreadGroup tgroup = allGroups[i];
            if (tgroup == null) break;
            if (!tgroup.equals(jdwpGroup)) idMan.getObjectId(tgroup).write(os);
        }
    }
}
