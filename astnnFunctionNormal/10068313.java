class BackupThread extends Thread {
    @Override
    @Transactional()
    public void migrateUserData(final Boolean digestPasswords) {
        String sql = "select first_name, last_name, user_name, user_passwd, " + "phone, fax, email, register_date, expire_date, update_date, company from users where users.user_name <> 'admin'";
        ParameterizedRowMapper<User> mapper = new ParameterizedRowMapper<User>() {

            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUsername(rs.getString("user_name"));
                user.setPassword(rs.getString("user_passwd"));
                user.setPhone(rs.getString("phone"));
                user.setFax(rs.getString("fax"));
                user.setEmail(rs.getString("email"));
                user.setRegistrationDate(rs.getTimestamp("register_date"));
                user.setUpdateDate(rs.getTimestamp("update_date"));
                user.setCompany(rs.getString("company"));
                user.setEnabled(true);
                return user;
            }
        };
        final List<User> users = this.jdbcTemplateV1.query(sql, mapper);
        if (digestPasswords) {
            int counter = 0;
            for (User user : users) {
                LOGGER.info("[" + ++counter + "/" + users.size() + "] User: " + user + "...digesting password.");
                user.setPassword(stringDigester.digest(user.getPassword()));
            }
        }
        final Role managerRole = roleDao.getRole(Constants.Roles.MANAGER.name());
        if (managerRole == null) {
            throw new IllegalStateException("Role was not found but is required.");
        }
        int counter = 0;
        for (User user : users) {
            LOGGER.info("[" + ++counter + "/" + users.size() + "] User: " + user + "...saving.");
            Set<UserToRole> userToRoles = user.getUserToRoles();
            UserToRole utr = new UserToRole();
            utr.setRole(managerRole);
            utr.setUser(user);
            userToRoles.add(utr);
            userDao.save(user);
            entityManager.flush();
        }
        LOGGER.info("Total number of records saved: " + users.size());
    }
}
