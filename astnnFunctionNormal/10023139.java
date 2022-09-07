class BackupThread extends Thread {
    protected void modifySEPath(String userID, String wfName) {
        File wfDir = getWorkflowDir(userID, wfName);
        String PSDir = "/" + userID + "/" + getName().replaceAll("\\W", "_");
        String PSOutputDir = PSDir + "/outputs/" + "input_ohm";
        String PSInputDir = PSDir + "/inputs/" + "output_ohm";
        if (wf.isPS()) {
            File propDesc = new File(wfDir, ".PS=PROPS1.desc");
            MiscUtils.writeStrToFile(propDesc.getAbsolutePath(), MiscUtils.readFileToStr(propDesc.getAbsolutePath()).replaceAll("/grid/([^/]*)/[^ ]*", "/grid/$1" + PSDir));
        }
        for (SZGJob agenJob : wf.getJobList()) {
            if (agenJob.getType() == SZGJob.GENERATOR_TYPE) {
                File jdl = new File(wfDir, agenJob.getName() + "/" + agenJob.getName() + ".jdl");
                MiscUtils.writeStrToFile(jdl.getAbsolutePath(), (MiscUtils.readFileToStr(jdl.getAbsolutePath()).replaceAll("\"/grid/([^/]*)/([^ ])*\"", "\"/grid/$1" + PSInputDir + "\"")).replaceAll("/users/([^/])*/([^/])*_files", "/users/" + userID + "/" + wfName));
            }
        }
        File wrk = new File(wfDir, wfName + "_remote.wrk");
        String wrkText = MiscUtils.readFileToStr(wrk.getAbsolutePath());
        wrkText = wrkText.replaceAll("\"/grid/([^/]*)/([^ ])*\"", "\"/grid/$1" + PSInputDir + "\"");
        wrkText = wrkText.replaceAll("\"lfn:/grid/([^/]*)/[^/]*/([^ ]*)\" ([^ ]*) PERMANENT OUTPUT", "\"lfn:/grid/$1" + PSOutputDir + "/$2\" /$3 PERMANENT OUTPUT");
        wrkText = wrkText.replaceAll("/users/([^/])*/([^/])*_files", "/users/" + userID + "/" + wfName + "_files");
        MiscUtils.writeStrToFile(wrk.getAbsolutePath(), wrkText);
    }
}
