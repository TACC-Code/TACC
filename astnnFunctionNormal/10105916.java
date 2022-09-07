class BackupThread extends Thread {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        HttpURLConnection httpURLConnection = null;
        byte[] result = null;
        try {
            byte[] bytes = HttpUtil.getHttpURLReturnData(request);
            if (-1 == bytes.length || 23 > bytes.length) throw new Exception();
            String userTag = request.getParameter("userTag");
            String isEncrypt = request.getParameter("isEncrypt");
            URL httpurl = new URL(ProtocolContanst.TRANSFERS_URL + userTag + "&isEncrypt=" + isEncrypt);
            httpURLConnection = (HttpURLConnection) httpurl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
            MsgPrint.showMsg("接收到字节的长度=" + httpURLConnection.getContentLength());
            if (0 >= httpURLConnection.getContentLength()) {
                throw new Exception();
            }
            InputStream is = httpURLConnection.getInputStream();
            byte[] resultBytes = new byte[httpURLConnection.getContentLength()];
            byte[] tempByte = new byte[1024];
            int length = 0;
            int index = 0;
            while ((length = is.read(tempByte)) != -1) {
                System.arraycopy(tempByte, 0, resultBytes, index, length);
                index += length;
            }
            is.close();
            result = resultBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServletOutputStream sos = response.getOutputStream();
        response.setContentLength(result.length);
        sos.write(result);
        sos.flush();
        sos.close();
    }
}
