class BackupThread extends Thread {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (false) {
            HttpAuthentication.Authenticate("testuser", "testpass", HttpAuthentication.AUTHTYPE_MD5, request, response);
        }
        if (true) {
            System.out.println("src: addr=" + request.getRemoteAddr() + " port=" + request.getRemotePort());
            String auth = request.getHeader("Authorization");
            if (auth == null) {
                System.out.println("CLIENT: PorcessRequest " + response.SC_UNAUTHORIZED + " " + request.getHeader("Authorization"));
                byte[] nonce = new byte[16];
                Random r = new Random();
                r.nextBytes(nonce);
                response.setHeader("WWW-Authenticate", "Digest realm=\"OpenACS\",qop=\"auth,auth-int\",nonce=\"" + cvtHex(nonce) + "\"");
                response.setStatus(response.SC_UNAUTHORIZED);
            } else {
                if (auth.startsWith("Basic ")) {
                    String up = auth.substring(6);
                    String ds = null;
                    try {
                        InputStream i = javax.mail.internet.MimeUtility.decode(new ByteArrayInputStream(up.getBytes()), "base64");
                        byte[] d = new byte[i.available()];
                        i.read(d);
                        ds = new String(d);
                    } catch (MessagingException ex) {
                        Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (up.endsWith("==")) {
                        ds = ds.substring(0, ds.length() - 2);
                    } else if (up.endsWith("=")) {
                        ds = ds.substring(0, ds.length() - 1);
                    }
                    String[] upa = ds.split(":");
                    System.out.println("CLIENT: up=" + up + " d='" + ds + "' user=" + upa[0] + " pass=" + upa[1]);
                } else if (auth.startsWith("Digest ")) {
                    if (auth.indexOf("nc=00000001") != -1) {
                        byte[] nonce = new byte[16];
                        Random r = new Random();
                        r.nextBytes(nonce);
                        System.out.println("Saying it is stale: " + auth);
                        response.setHeader("WWW-Authenticate", "Digest realm=\"OpenACS\",qop=\"auth,auth-int\",stale=true,nonce=\"" + cvtHex(nonce) + "\"");
                        response.setStatus(response.SC_UNAUTHORIZED);
                        return;
                    }
                    ByteArrayInputStream bi = new ByteArrayInputStream(auth.substring(6).replace(',', '\n').replaceAll("\"", "").getBytes());
                    Properties p = new Properties();
                    p.load(bi);
                    p.setProperty("method", request.getMethod());
                    for (Entry<Object, Object> e : p.entrySet()) {
                        System.out.println("Entry " + e.getKey() + " -> " + e.getValue());
                    }
                    MessageDigest digest = null;
                    try {
                        digest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    postDigest(digest, p);
                    String udigest = (String) p.getProperty("response");
                    String dd = cvtHex(digest.digest());
                    System.out.println("respone: got='" + udigest + "' expected: '" + dd + "'");
                }
            }
            out.println("Hello");
            out.close();
            return;
        }
        int i;
        String oui = "00147F";
        String sn = "CP0713JTP7W";
        CPELocal cpe = Ejb.lookupCPEBean();
        out.println("Lookup bean .....\n" + cpe);
        GetParameterNamesResponse gpnr = null;
        Enumeration pnks = gpnr.names.keys();
        while (pnks.hasMoreElements()) {
            String k = (String) pnks.nextElement();
            out.println(k + " = " + gpnr.names.get(k));
        }
        if (true) {
            String[] n = new String[6];
            n[0] = "InternetGatewayDevice.DeviceSummary";
            n[1] = "InternetGatewayDevice.DeviceInfo.Manufacturer";
            n[2] = "InternetGatewayDevice.DeviceInfo.ManufacturerOUI";
            n[3] = "InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState";
            n[4] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
            n[5] = "InternetGatewayDevice.DeviceInfo.ProductClass";
            GetParameterValuesResponse values = null;
            if (values == null) {
                out.println("No response .....");
            }
            Enumeration ve = values.values.keys();
            out.println("-----------------------------------------------------------------");
            while (ve.hasMoreElements()) {
                String k = (String) ve.nextElement();
                String v = (String) values.values.get(k);
                out.println(k + "=" + v);
            }
            out.println("-----------------------------------------------------------------");
            GetRPCMethodsResponse methods = null;
            if (methods == null) {
                out.println("No response .....");
            }
            for (i = 0; i < methods.methods.length; i++) {
                out.println(methods.methods[i]);
            }
        }
        out.close();
    }
}
