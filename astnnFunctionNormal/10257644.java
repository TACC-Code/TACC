class BackupThread extends Thread {
    private static void writeSystemInfo() {
        gDebugInfo = new String[10];
        gDebugInfo[0] = llerror.getLogfilename();
        String clientinfo[] = new String[5];
        clientinfo[0] = llversioninfo.getChannelName();
        clientinfo[1] = llversioninfo.getLlversionmajor();
        clientinfo[2] = llversioninfo.getLlversionminor();
        clientinfo[3] = llversioninfo.getLlversionbuild();
        clientinfo[4] = llversioninfo.getLlversionpatch();
        out.println(gDebugInfo[0]);
        out.close();
    }
}
