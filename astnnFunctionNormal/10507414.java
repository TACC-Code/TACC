class BackupThread extends Thread {
    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 2) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    byte[] pos = active.getArg(1);
                    ch.repositionFromCurrent(pos);
                } catch (Exception e) {
                    trac.zReturn(active.getArg(2));
                }
            }
        }
    }
}
