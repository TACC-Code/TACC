class BackupThread extends Thread {
    @RequestMapping(value = "/system/user/resetpasswd", method = RequestMethod.GET)
    public String resetPassword(@RequestParam("id") Long id, @RequestParam("operator") String systemAgentAccount) {
        systemUserManager.initionPassword(id, passwordValidator.digest("123456", 1), systemAgentAccount);
        return "redirect:/system/user/list.htm";
    }
}
