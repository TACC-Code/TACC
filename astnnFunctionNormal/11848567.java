class BackupThread extends Thread {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String user = request.getParameter("user");
        String pass_temp = request.getParameter("pass");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(pass_temp.getBytes()));
        String pass = hash.toString(16);
        String msg = "Usu�rio n�o Cadastrado - Login j� existe!";
        DAOUsuario daouser = new DAOUsuario();
        Usuario teste = daouser.findByLogin(user);
        if (teste == null) {
            DAOProfessor daoProf = new DAOProfessor();
            daoProf.begin();
            try {
                Professor p = new Professor();
                p.setNome(nome);
                p.setLogin(user);
                p.setSenha(pass);
                daoProf.persist(p);
                daoProf.commit();
            } catch (Exception e) {
                e.getMessage();
            }
            msg = new String("Professor cadastrado!");
        }
        request.setAttribute("msg", msg);
        RequestDispatcher rqd = request.getRequestDispatcher("painel.jsp");
        rqd.forward(request, response);
    }
}
