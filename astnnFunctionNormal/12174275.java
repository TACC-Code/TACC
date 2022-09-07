class BackupThread extends Thread {
    private final boolean glXMakeContextCurrent(long dpy, long writeDrawable, long readDrawable, long ctx) {
        boolean res = false;
        try {
            if (TRACE_CONTEXT_CURRENT) {
                Throwable t = new Throwable(Thread.currentThread() + " - glXMakeContextCurrent(" + toHexString(dpy) + ", " + toHexString(writeDrawable) + ", " + toHexString(readDrawable) + ", " + toHexString(ctx) + ") - GLX >= 1.3 " + glXVersionOneThreeCapable);
                t.printStackTrace();
            }
            if (glXVersionOneThreeCapable) {
                res = GLX.glXMakeContextCurrent(dpy, writeDrawable, readDrawable, ctx);
            } else if (writeDrawable == readDrawable) {
                res = GLX.glXMakeCurrent(dpy, writeDrawable, ctx);
            } else {
                throw new InternalError("Given readDrawable but no driver support");
            }
        } catch (RuntimeException re) {
            if (DEBUG) {
                System.err.println("Warning: X11GLXContext.glXMakeContextCurrent failed: " + re + ", with " + "dpy " + toHexString(dpy) + ", write " + toHexString(writeDrawable) + ", read " + toHexString(readDrawable) + ", ctx " + toHexString(ctx));
                re.printStackTrace();
            }
        }
        return res;
    }
}
