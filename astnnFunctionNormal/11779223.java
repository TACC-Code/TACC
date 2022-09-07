class BackupThread extends Thread {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            com.oreilly.servlet.ParameterParser paramParser = new ParameterParser(request);
            String username = paramParser.getStringParameter("a");
            String password = paramParser.getStringParameter("b");
            String dateOfRegistration = paramParser.getStringParameter("c");
            String fullName = paramParser.getStringParameter("d");
            String email = paramParser.getStringParameter("e");
            username = Str.decrypt(username);
            password = Str.decrypt(password);
            dateOfRegistration = Str.decrypt(dateOfRegistration);
            fullName = Str.decrypt(fullName);
            email = Str.decrypt(email);
            Calendar then = Calendar.getInstance();
            then.setTimeInMillis(Long.parseLong(dateOfRegistration));
            then.roll(Calendar.DAY_OF_MONTH, 2);
            Calendar now = Calendar.getInstance();
            if (now.after(then)) {
                PrintWriter out = response.getWriter();
                out.write("This link is no longer valid, please try to register again.");
                out.close();
                return;
            }
            if (UsersHelper.getUsernameExists(username)) {
                PrintWriter out = response.getWriter();
                out.write("ERROR: The username " + username + " already exists.");
                out.close();
                return;
            }
            if (UsersHelper.getEmailExists(email)) {
                PrintWriter out = response.getWriter();
                out.write("ERROR: The e-mail address: " + email + " already used.");
                out.close();
                return;
            }
            if (UsersHelper.registerUser(username, password, fullName, email) != null) {
                PrintWriter out = response.getWriter();
                out.write("GWTM registration service:\nRegistration completed successfully!");
                out.write("<br> Username:" + username);
                out.write("<br> Password:" + password);
                out.close();
            } else {
                PrintWriter out = response.getWriter();
                out.write("ERROR: Registration failed!");
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write(ex.getMessage());
        }
    }
}
