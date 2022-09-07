class BackupThread extends Thread {
    @RequestMapping(value = "/system/user/add", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("systemUser") SystemUser systemUser, BindingResult result, SystemAgent systemAgent, ModelMap model) throws Exception {
        systemUserValidator.validate(systemUser, result);
        if (result.hasErrors()) {
            return "/system/user/add";
        }
        systemUser.setPassword(passwordValidator.digest(systemUser.getPassword(), 1));
        Long id = systemUserManager.addSystemUser(systemUser);
        if (null == id) {
            model.put("result", "�������Աʧ�ܣ�");
            return "/error";
        }
        return "redirect:/system/user/list.htm";
    }
}
