class BackupThread extends Thread {
    @Test
    public void sendActivationEmail() throws Exception {
        User user = new User();
        user.setName("UserTest");
        user.setEmail("foo@foo.com");
        user.setPassword("FOO");
        String registrationHash = es.alvsanand.webpage.common.StringUtils.getValidName(new String(Base64.encodeBase64(cryptographyService.digest((user.getPassword() + Math.random()).getBytes()))));
        user.setRegistrationHash(registrationHash);
        userService.saveUser(user);
        userService.sendActivationEmail(user);
    }
}
