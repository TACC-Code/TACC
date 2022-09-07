class BackupThread extends Thread {
    private Integer uploadRoute() throws IOException {
        CtrlMemory cm = new CtrlMemory(ctx);
        Route r = cm.getFullRoute(routeId);
        String distance = Double.toString(r.getDistance());
        String desnivell = Double.toString(r.getDesnivell());
        String type = r.getType();
        CtrlUser cu = new CtrlUser(ctx);
        String user = cu.getUser();
        String pword = cu.getPassword();
        KmzAdapter k = new KmzAdapter(ctx);
        File kmz = k.getTmpKmz(r);
        HttpClient httpClient = new DefaultHttpClient();
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            Log.e(TAG, "error creating keystore", e);
        }
        InputStream instream = ctx.getResources().openRawResource(R.raw.etracks_keystore);
        Log.d(TAG, "loading keystore...");
        try {
            trustStore.load(instream, "pxc4l0h4".toCharArray());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "keystore load: no such algorithm", e);
        } catch (CertificateException e) {
            Log.e(TAG, "keystore load: certificate problem", e);
        } catch (IOException e) {
            Log.e(TAG, "keystore load: input/output error", e);
        } finally {
            instream.close();
        }
        Log.d(TAG, "open SSL socket...");
        SSLSocketFactory socketFactory = null;
        try {
            socketFactory = new SSLSocketFactory(trustStore);
        } catch (KeyManagementException e) {
            Log.e(TAG, "KeyManagementException", e);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException", e);
        } catch (KeyStoreException e) {
            Log.e(TAG, "KeyStoreException", e);
        } catch (UnrecoverableKeyException e) {
            Log.e(TAG, "UnrecoverableKeyException", e);
        }
        Scheme sch = new Scheme("https", socketFactory, 443);
        httpClient.getConnectionManager().getSchemeRegistry().register(sch);
        HttpPost request = new HttpPost(URL);
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("data[User][username]", new StringBody(user));
        entity.addPart("data[User][password]", new StringBody(pword));
        entity.addPart("data[Route][type]", new StringBody(type));
        entity.addPart("data[Route][ramp]", new StringBody(desnivell));
        entity.addPart("data[Route][distance]", new StringBody(distance));
        entity.addPart("data[Route][file_up]", new FileBody(kmz));
        ListIterator<Video> iter = cm.getAllVideos(routeId).listIterator();
        int i = 0;
        while (iter.hasNext()) {
            Video v = iter.next();
            File fv = new File(v.getPath());
            entity.addPart("data[Video][" + i + "][file]", new FileBody(fv));
            entity.addPart("data[Video][" + i + "][description]", new StringBody(v.getDesc()));
            entity.addPart("data[Video][" + i + "][point][long]", new StringBody(v.getLon() + ""));
            entity.addPart("data[Video][" + i + "][point][latitude]", new StringBody(v.getLat() + ""));
            i++;
        }
        request.setEntity(entity);
        Log.i(TAG, "Executing HTTP POST request to upload route");
        HttpResponse response = httpClient.execute(request);
        Integer status = response.getStatusLine().getStatusCode();
        Log.d(TAG, "HTTP POST request executed, status return code: " + status);
        if (status == HttpStatus.SC_OK) {
            Log.d(TAG, "Route uploaded without errors, now save it to DB");
            serverId = getRouteId(response.getEntity());
            DataBaseRoutes db = new DataBaseRoutes(ctx);
            db.open();
            db.setServerId(routeId, serverId);
            db.close();
        }
        k.deleteTmpFile();
        return status;
    }
}
