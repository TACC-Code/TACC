class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printAccess(request, false);
        String url = request.getParameter("url");
        if (url == null || url.length() == 0) {
            reportJsonError(request, response, "No url given");
            return;
        }
        try {
            URLConnection conn = (new URL(url)).openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();
            JSONObject jso = new JSONObject();
            for (Entry<String, List<String>> s : map.entrySet()) {
                String k = s.getKey();
                k = (k != null) ? k.replaceAll("-", "") : "nokey";
                jso.put(k, s.getValue().get(0));
            }
            response.getWriter().write(jso.toJSONString());
            return;
        } catch (Exception e) {
            reportJsonError(request, response, e);
        }
    }
}
