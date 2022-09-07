class BackupThread extends Thread {
    private void fillParameters(HttpServletRequest request, ModelMap params) {
        Enumeration names = request.getAttributeNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement().toString();
            params.put(name, request.getAttribute(name));
        }
        names = request.getParameterNames();
        while (names.hasMoreElements()) {
            name = names.nextElement().toString();
            params.put(name, request.getParameter(name));
        }
        try {
            String charset = request.getCharacterEncoding();
            if (charset == null) charset = "UTF-8";
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));
            CharArrayWriter data = new CharArrayWriter();
            char buf[] = new char[4096];
            int ret;
            while ((ret = in.read(buf, 0, 4096)) != -1) data.write(buf, 0, ret);
            String content = URLDecoder.decode(data.toString().trim(), charset);
            if (content != "") {
                String[] param_pairs = content.split("&");
                String[] kv;
                for (String p : param_pairs) {
                    kv = p.split("=");
                    if (kv.length > 1) params.put(kv[0], kv[1]);
                }
            }
            data.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
