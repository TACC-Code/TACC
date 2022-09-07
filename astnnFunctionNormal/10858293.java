class BackupThread extends Thread {
    private void genJNLP(Admin admin, String orgId, HashMap<String, ResourceDataVO> resources, String fileName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sourceFilename = "src_" + fileName;
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(sourceFilename);
        if (is == null) {
            log.error("Error souce JNLP file :" + sourceFilename + " not found.");
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int read = -1;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            String jnlpFile = new String(baos.toByteArray());
            String replaceString = "<jar href=\"" + application + "/" + orgId + "/customcode.jar\"/>\n";
            for (ResourceDataVO res : resources.values()) {
                if (res.getType().equals(Constants.RESOURCE_TYPE_CUSTOMCODEJAR)) {
                    replaceString += "<jar href=\"" + application + "/" + orgId + "/" + res.getName() + "\"/>\n";
                }
            }
            jnlpFile = jnlpFile.replace("<!--CUSTOMJARS-->", replaceString);
            jnlpFile = jnlpFile.replace("<!--ORGHREF-->", application + "/" + orgId + "/");
            resp.setContentType("application/x-java-jnlp-file");
            resp.setDateHeader("Last-Modified", System.currentTimeMillis());
            resp.getOutputStream().write(jnlpFile.getBytes());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
