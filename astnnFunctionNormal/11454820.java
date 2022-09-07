class BackupThread extends Thread {
    private ServiceRequestBuffer getRestoreSRB(int options) {
        GDS gds = getGds();
        ServiceRequestBuffer restoreSPB = gds.createServiceRequestBuffer(ISCConstants.isc_action_svc_restore);
        for (Iterator iter = backupPaths.iterator(); iter.hasNext(); ) {
            PathSizeStruct pathSize = (PathSizeStruct) iter.next();
            restoreSPB.addArgument(ISCConstants.isc_spb_bkp_file, pathSize.getPath());
        }
        for (Iterator iter = restorePaths.iterator(); iter.hasNext(); ) {
            PathSizeStruct pathSize = (PathSizeStruct) iter.next();
            restoreSPB.addArgument(ISCConstants.isc_spb_dbname, pathSize.getPath());
            if (iter.hasNext() && pathSize.getSize() != -1) restoreSPB.addArgument(ISCConstants.isc_spb_res_length, pathSize.getSize());
        }
        if (restoreBufferCount != -1) restoreSPB.addArgument(ISCConstants.isc_spb_res_buffers, restoreBufferCount);
        if (restorePageSize != -1) restoreSPB.addArgument(ISCConstants.isc_spb_res_page_size, restorePageSize);
        restoreSPB.addArgument(ISCConstants.isc_spb_res_access_mode, ((byte) (restoreReadOnly ? ISCConstants.isc_spb_res_am_readonly : ISCConstants.isc_spb_res_am_readwrite)));
        if (verbose) restoreSPB.addArgument(ISCConstants.isc_spb_verbose);
        if ((options & RESTORE_CREATE) != RESTORE_CREATE && (options & RESTORE_REPLACE) != RESTORE_REPLACE) {
            options |= restoreReplace ? RESTORE_REPLACE : RESTORE_CREATE;
        }
        restoreSPB.addArgument(ISCConstants.isc_spb_options, options);
        return restoreSPB;
    }
}
