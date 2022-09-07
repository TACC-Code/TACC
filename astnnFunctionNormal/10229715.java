class BackupThread extends Thread {
    protected void patch(UpdateCheckInstance instance, Update updater_update, PluginInterface updater_plugin) {
        try {
            ResourceDownloader rd_log = updater_update.getDownloaders()[0];
            File[] files = new File(updater_plugin.getPluginDirectoryName()).listFiles();
            if (files == null) {
                if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "Core Patcher: no files in plugin dir!!!"));
                return;
            }
            String patch_prefix = "Azureus2_" + Constants.getBaseVersion() + "_P";
            int highest_p = -1;
            File highest_p_file = null;
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (name.startsWith(patch_prefix) && name.endsWith(".pat")) {
                    if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "Core Patcher: found patch file '" + name + "'"));
                    try {
                        int this_p = Integer.parseInt(name.substring(patch_prefix.length(), name.indexOf(".pat")));
                        if (this_p > highest_p) {
                            highest_p = this_p;
                            highest_p_file = files[i];
                        }
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
            if (CorePatchLevel.getCurrentPatchLevel() >= highest_p) {
                if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "Core Patcher: no applicable patch found (highest = " + highest_p + ")"));
                if (updater_update.getRestartRequired() == Update.RESTART_REQUIRED_MAYBE) {
                    updater_update.setRestartRequired(Update.RESTART_REQUIRED_NO);
                }
            } else {
                rd_log.reportActivity("Applying patch '" + highest_p_file.getName() + "'");
                if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "Core Patcher: applying patch '" + highest_p_file.toString() + "'"));
                InputStream pis = new FileInputStream(highest_p_file);
                patchAzureus2(instance, pis, "P" + highest_p, plugin_interface.getLogger().getChannel("CorePatcher"));
                Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_INFORMATION, "Patch " + highest_p_file.getName() + " ready to be applied"));
                String done_file = highest_p_file.toString();
                done_file = done_file.substring(0, done_file.length() - 1) + "x";
                highest_p_file.renameTo(new File(done_file));
                updater_update.setRestartRequired(Update.RESTART_REQUIRED_YES);
            }
        } catch (Throwable e) {
            Debug.printStackTrace(e);
            Logger.log(new LogAlert(LogAlert.UNREPEATABLE, "Core Patcher failed", e));
        }
    }
}
