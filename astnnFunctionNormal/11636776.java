class BackupThread extends Thread {
    public void actionJobSubmit(String pPath, JobConfig pJC, SubmitterPortalClient pProxy) {
        Vector val = pJC.getInputsPortSequence();
        try {
            String paramName = "";
            String param = new String(""), str = "";
            for (int i = 0; i < val.size(); i++) {
                paramName = "" + val.get(i);
                FileInputStream in = new FileInputStream(pPath + "/" + paramName);
                byte[] btmp = new byte[512];
                int itmp = 0;
                boolean first = true;
                while ((itmp = in.read(btmp)) > (-1)) {
                    if (first) param = param.concat(paramName + "=" + (new String(btmp, 0, itmp))); else param = param.concat("&" + paramName + "=" + (new String(btmp, 0, itmp)));
                    first = false;
                }
            }
            URL url = new URL(pJC.getJobPropertyValue("gaeurl"));
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(param);
            writer.flush();
            writer.close();
            File f = new File(pPath + "/outputs/" + pJC.getOutputPropertyValue((String) (pJC.getOutputs().nextElement()), "intname") + "_0");
            f.createNewFile();
            FileOutputStream fw = new FileOutputStream(f);
            byte[] buf = new byte[1024];
            InputStream is = conn.getInputStream();
            int bs = 10;
            while ((bs = is.read(buf)) > 0) {
                fw.write(buf, 0, bs);
                fw.flush();
            }
            is.close();
            fw.close();
            status = 6;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                File f = new File(pPath + "/outputs/stderr.log");
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                fw.write(e.getMessage() + "\n");
                for (int ii = 0; ii < e.getStackTrace().length; ii++) {
                    fw.write(e.getStackTrace()[ii].toString() + "\n");
                }
                fw.flush();
                fw.close();
            } catch (Exception e0) {
                e0.printStackTrace();
            }
            status = 7;
        }
    }
}
