class BackupThread extends Thread {
        public void processGET(Request request) {
            String path = "c:\\" + request.requestLine[1].substring(1);
            File filehd = new File(path);
            int len;
            long llen = filehd.length();
            if (llen > Long.MAX_VALUE) len = Integer.MAX_VALUE; else len = (int) llen;
            Response response = new Response();
            response.put("content-type", "text/html");
            response.put("content-length", "" + len);
            write(response.toString().getBytes());
            System.out.println("WRITING:\n" + response);
            try {
                FileChannel sourceChannel = new FileInputStream(filehd).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), (SocketChannel) channel);
                setWriteReady(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
