class BackupThread extends Thread {
    @RequestMapping(value = "/system/user/modify_password", method = RequestMethod.POST)
    public String doModifyPassword(@ModelAttribute("modifyPassword") ModifyPasswordDO modifyPassword, BindingResult result, SystemAgent systemAgent, ModelMap model) throws Exception {
        if (StringUtils.isEmpty(modifyPassword.getOriPassword())) {
            result.rejectValue("oriPassword", null, null, "������ԭ����");
        }
        if (StringUtils.isEmpty(modifyPassword.getNewPassword())) {
            result.rejectValue("newPassword", null, null, "������������");
        }
        if (StringUtils.isEmpty(modifyPassword.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, null, "������������ȷ��");
        }
        if (!modifyPassword.getNewPassword().equals(modifyPassword.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, null, "������ȷ�ϲ���ȷ");
        }
        if (result.hasErrors()) {
            model.put("message", "");
            model.put("error", "");
            return "/system/user/modify_password";
        }
        try {
            int rows = systemUserManager.resetpasswd(systemAgent.getAccountId(), passwordValidator.digest(modifyPassword.getNewPassword(), 1), systemAgent.getAccount());
            if (rows < 1) {
                throw new Exception("modifyPasswdDo error!");
            }
            model.put("message", "�޸ĳɹ���");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "/error";
        }
        return "success";
    }
}
