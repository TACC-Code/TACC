class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserHandler uh = (UserHandler) request.getSession().getAttribute("userHandler");
            ClientAccessor ca = (ClientAccessor) request.getSession().getAttribute("clientAccessor");
            if (uh == null || !uh.isLoggedIn() || uh.getUser().getCompany() != 0 || uh.getUser().getLoginLevel() != 0) {
                response.sendRedirect(request.getContextPath());
                return;
            }
            if (request.getParameter("X509Principal.PWD") == null) {
                request.setAttribute("T", ca.getPerson().getName());
                request.setAttribute("USER", ca.getPerson().getLoginCode());
                request.setAttribute("EMAIL", ca.getPerson().getEmail());
                RequestDispatcher rd = request.getRequestDispatcher("/intranet/generation.jsp");
                rd.forward(request, response);
                return;
            }
            Security.addProvider(new BouncyCastleProvider());
            Hashtable attrs = new Hashtable();
            Vector order = new Vector();
            InputStreamReader rd = new InputStreamReader(CertGenerator.class.getResourceAsStream("/desk.pem"));
            PEMReader reader = new PEMReader(rd);
            Object oo = (KeyPair) reader.readObject();
            KeyPair myKey = (KeyPair) oo;
            reader.close();
            rd = new InputStreamReader(CertGenerator.class.getResourceAsStream("/desk.crt"));
            reader = new PEMReader(rd);
            X509Certificate root = (X509Certificate) reader.readObject();
            reader.close();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            PublicKey users = kp.getPublic();
            String issuer = root.getSubjectDN().getName();
            attrs.put(X509Principal.T, request.getParameter("X509Principal.T"));
            attrs.put(X509Principal.C, request.getParameter("X509Principal.C"));
            attrs.put(X509Principal.O, request.getParameter("X509Principal.O"));
            attrs.put(X509Principal.OU, request.getParameter("X509Principal.OU"));
            attrs.put(X509Principal.L, request.getParameter("X509Principal.L"));
            attrs.put(X509Principal.CN, request.getParameter("X509Principal.CN"));
            attrs.put(X509Principal.EmailAddress, request.getParameter("X509Principal.EmailAddress"));
            order.addElement(X509Principal.T);
            order.addElement(X509Principal.C);
            order.addElement(X509Principal.O);
            order.addElement(X509Principal.OU);
            order.addElement(X509Principal.L);
            order.addElement(X509Principal.CN);
            order.addElement(X509Principal.EmailAddress);
            X509Principal subjectDn = new X509Principal(order, attrs);
            Session sess = SessionHolder.currentSession().getSess();
            CertificateEntry ce = new CertificateEntry();
            ce.setCert("");
            ce.setMd5Key("");
            ce.setName(subjectDn.getName());
            ce.setPerson(null);
            ce.setValid(false);
            sess.save(ce);
            sess.flush();
            X509V3CertificateGenerator v3c = new X509V3CertificateGenerator();
            v3c.reset();
            v3c.setSerialNumber(BigInteger.valueOf(ce.getId()));
            v3c.setIssuerDN(new X509Principal(issuer));
            v3c.setNotBefore(new Date());
            v3c.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
            v3c.setSubjectDN(subjectDn);
            v3c.setPublicKey(users);
            v3c.setSignatureAlgorithm("MD5WithRSAEncryption");
            v3c.addExtension(MiscObjectIdentifiers.netscapeCertType, false, new NetscapeCertType(NetscapeCertType.sslClient | NetscapeCertType.objectSigning));
            X509Certificate cert = v3c.generate(myKey.getPrivate(), "BC");
            cert.getSignature();
            cert.checkValidity(new Date());
            cert.verify(myKey.getPublic());
            KeyStore store = KeyStore.getInstance("PKCS12", "BC");
            store.load(null, null);
            store.setKeyEntry(request.getParameter("X509Principal.T"), kp.getPrivate(), null, new Certificate[] { cert, root });
            StringWriter sr = new StringWriter();
            sr.write("-----BEGIN CERTIFICATE-----\n");
            sr.write(new String(Base64.encode(cert.getEncoded())));
            sr.write("\n");
            sr.write("-----END CERTIFICATE-----");
            byte[] pwdMD5 = Hex.encode(MessageDigest.getInstance("MD5").digest(cert.getEncoded()));
            String code = new String(pwdMD5);
            if (code.length() < 32) {
                for (int i = (32 - code.length()); i > 0; i--) {
                    code = "0" + code;
                }
            }
            List<CertificateEntry> lce = (List<CertificateEntry>) sess.createQuery("FROM " + CertificateEntry.class.getName() + " WHERE person = ? AND valid = true ").setInteger(0, ca.getPersonId()).list();
            for (CertificateEntry cea : lce) {
                ce.setValid(false);
                sess.update(cea);
                sess.flush();
            }
            ce.setCert(sr.toString().trim());
            ce.setMd5Key(code.trim());
            ce.setPerson(ca.getPersonId());
            ce.setValid(true);
            sess.update(ce);
            sess.flush();
            SessionHolder.closeSession();
            System.out.println("Writing certificate");
            response.setContentType("application/pkcs-12");
            response.setHeader("Content-disposition", "inline;filename=" + request.getParameter("X509Principal.T").trim() + ".p12");
            OutputStream out = response.getOutputStream();
            store.store(out, request.getParameter("X509Principal.PWD").trim().toCharArray());
            out.close();
        } catch (Exception ex) {
            try {
                SessionHolder.endSession();
            } catch (Exception ejx) {
            }
            ex.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Error: ");
            out.println(ex.getMessage());
            out.println("</h1>");
            out.println("<br/>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }
}
