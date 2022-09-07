class BackupThread extends Thread {
    public void transform(String sourceCRS, String targetCRS, Point3d point) {
        try {
            URL url = new URL(serviceEndPoint + "?" + "sourceCRS=" + sourceCRS + "&targetCRS=" + targetCRS + "&coordinate=" + point.x + "," + point.y + "," + point.z);
            System.out.println("url " + url);
            URLConnection urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            urlc.connect();
            InputStream is = urlc.getInputStream();
            byte[] buffer = new byte[200];
            int length = is.read(buffer);
            String buffer_s = new String(buffer);
            String coord_s = buffer_s.substring(0, length);
            StringTokenizer tok = new StringTokenizer(coord_s, ",");
            String xs = tok.nextToken();
            String ys = tok.nextToken();
            String zs = tok.nextToken();
            double x = Double.parseDouble(xs);
            double y = Double.parseDouble(ys);
            double z = Double.parseDouble(zs);
            point.x = x;
            point.y = y;
            point.z = z;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
