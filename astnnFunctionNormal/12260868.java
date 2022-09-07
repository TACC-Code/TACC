class BackupThread extends Thread {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement ucpst = null;
        PreparedStatement ucpst2 = null;
        ResultSet rs = null;
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String school = request.getParameter("school");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            byte[] defaultBytes = password.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            messageDigest.toString();
            password = hexString + "";
            conn = DBConnection.GetConnection();
            String usercheck = "SELECT username, email FROM students WHERE username = ? OR email = ?";
            ucpst = conn.prepareStatement(usercheck);
            ucpst.setString(1, username);
            ucpst.setString(2, email);
            rs = ucpst.executeQuery();
            if (!rs.next()) {
                String sql = "INSERT INTO students(username, password, firstName, lastName, school, email) values(?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, firstName);
                pst.setString(4, lastName);
                pst.setString(5, school);
                pst.setString(6, email);
                pst.executeUpdate();
                sql = "SELECT studentID, username FROM students WHERE username = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                rs = pst.executeQuery();
                HttpSession session = request.getSession(true);
                String tempuserid = (String) session.getAttribute("userName");
                if (rs.next()) {
                    session.setAttribute("userName", rs.getString("username"));
                    session.setAttribute("userID", rs.getInt("studentID"));
                    session.setAttribute("userLevel", "student");
                    String updatequery = "UPDATE TutorialInfo SET createdBy = ?, userLevel=\"student\" WHERE createdBy = ?";
                    ucpst2 = conn.prepareStatement(updatequery);
                    ucpst2.setString(1, (String) session.getAttribute("userName"));
                    ucpst2.setString(2, tempuserid);
                    ucpst2.executeUpdate();
                    String redirectURL1 = "students.jsp";
                    response.sendRedirect(redirectURL1);
                } else {
                    String redirectURL2 = "students.jsp?error=4";
                    response.sendRedirect(redirectURL2);
                }
            } else {
                if (rs.getString("email").equals(email) && rs.getString("username").equals(username)) {
                    conn.close();
                    String redirectURL2 = "students.jsp?error=3";
                    response.sendRedirect(redirectURL2);
                } else if (rs.getString("username").equals(username)) {
                    conn.close();
                    String redirectURL3 = "students.jsp?error=1";
                    response.sendRedirect(redirectURL3);
                } else {
                    conn.close();
                    String redirectURL4 = "students.jsp?error=2";
                    response.sendRedirect(redirectURL4);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
