class BackupThread extends Thread {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String num = "";
        String supp = null;
        if (req.getParameter("admin") != null) {
            num = req.getParameter("numeroetu");
            supp = "2";
        } else num = req.getParameter("numeroetudiant" + supp);
        String nom = req.getParameter("nom" + supp);
        String prenom = req.getParameter("prenom" + supp);
        String email = req.getParameter("email" + supp);
        String promo = req.getParameter("promo" + supp);
        String filiere = req.getParameter("filiere" + supp);
        Date naissance = null;
        try {
            naissance = formatDate.parse(req.getParameter("naissance" + supp));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String ecole = req.getParameter("ecole" + supp);
        String ville = req.getParameter("ville" + supp);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] pwdByte = null;
        if (req.getParameter("pwd") == null || req.getParameter("pwd").equals("")) pwdByte = null; else pwdByte = md5.digest(req.getParameter("pwd").getBytes());
        Membre co = Membre.readMembre(num);
        String stat = "etudiant";
        if (req.getParameter("statut" + supp) != null) {
            if (req.getParameter("statut" + supp).equals("oui")) stat = "enseignant";
        }
        co.updateMembre(nom, prenom, email, ecole, naissance, filiere, promo, stat, ville, pwdByte);
        if (req.getParameter("admin") == null) {
            formatDate = new SimpleDateFormat("dd/MM/yyyy");
            HttpSession session = req.getSession(true);
            session.setAttribute("connected", true);
            session.setAttribute("email", co.getEmail());
            session.setAttribute("idMembre", co.getNumeroEtudiant());
            session.setAttribute("nom", co.getNom());
            session.setAttribute("prenom", co.getPrenom());
            session.setAttribute("promotion", co.getPromotion());
            session.setAttribute("filiere", co.getFiliere());
            session.setAttribute("dateNaissance", co.getDateNaissance());
            session.setAttribute("ecoleActuelle", co.getEcoleActuelle());
            session.setAttribute("ville", co.getVille());
            session.setAttribute("statut", co.getStatut());
        }
        if (req.getParameter("admin") != null) {
            try {
                resp.sendRedirect("/A_Membre.jsp?login=" + num + "&&validate=" + new String("mod") + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                resp.sendRedirect("/profil.jsp?validate=ok");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
