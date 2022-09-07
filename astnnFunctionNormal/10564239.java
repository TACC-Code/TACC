class BackupThread extends Thread {
    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "systemConfig.shopName", message = "网店名称不允许为空!"), @RequiredStringValidator(fieldName = "systemConfig.shopUrl", message = "网店网址不允许为空!") }, requiredFields = { @RequiredFieldValidator(fieldName = "systemConfig.currencyType", message = "货币种类不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.currencySign", message = "货币符号不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.currencyUnit", message = "货币单位不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.priceScale", message = "商品价格精确位数不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.priceRoundType", message = "商品价格精确方式不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.orderScale", message = "订单金额精确位数不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.orderRoundType", message = "订单金额精确方式不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.storeAlertCount", message = "商品库存报警数量不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.storeFreezeTime", message = "库存预占时间点不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.uploadLimit", message = "文件上传最大值不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.isRegister", message = "是否开放注册不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.isLoginFailureLock", message = "是否开启自动锁定账号功能不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.loginFailureLockCount", message = "连续登录失败最大次数不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.loginFailureLockTime", message = "自动解锁时间不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.bigProductImageWidth", message = "商品图片（大）宽不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.bigProductImageHeight", message = "商品图片（大）高不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.smallProductImageWidth", message = "商品图片（小）宽不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.smallProductImageHeight", message = "商品图片（小）高不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.thumbnailProductImageWidth", message = "商品缩略图宽不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.thumbnailProductImageHeight", message = "商品缩略图高不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.watermarkAlpha", message = "水印透明度不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.pointType", message = "积分获取方式不允许为空!"), @RequiredFieldValidator(fieldName = "systemConfig.pointScale", message = "积分换算比率不允许为空!") }, intRangeFields = { @IntRangeFieldValidator(fieldName = "systemConfig.priceScale", min = "0", message = "商品价格精确位数必须为零或正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.priceScale", max = "4", message = "商品价格精确位数位不能大于4!"), @IntRangeFieldValidator(fieldName = "systemConfig.orderScale", min = "0", message = "订单金额精确位数必须为零或正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.orderScale", max = "4", message = "订单金额精确位数位不能大于4!"), @IntRangeFieldValidator(fieldName = "systemConfig.storeAlertCount", min = "0", message = "商品库存报警数量必须为零或正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.uploadLimit", min = "0", message = "文件上传最大值必须为零或正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.loginFailureLockCount", min = "1", message = "连续登录失败最大次数必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.loginFailureLockTime", min = "0", message = "自动解锁时间必须为零或正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.bigProductImageWidth", min = "1", message = "商品图片（大）宽必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.bigProductImageHeight", min = "1", message = "商品图片（大）高必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.smallProductImageWidth", min = "1", message = "商品图片（小）宽必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.smallProductImageHeight", min = "1", message = "商品图片（小）高必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.thumbnailProductImageWidth", min = "1", message = "商品缩略图宽必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.thumbnailProductImageHeight", min = "1", message = "商品缩略图高必须为正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.smtpPort", min = "0", message = "SMTP端口必须为零正整数!"), @IntRangeFieldValidator(fieldName = "systemConfig.watermarkAlpha", min = "0", max = "100", message = "水印透明度取值范围在${min}-${max}之间!") }, urls = { @UrlValidator(fieldName = "systemConfig.url", message = "网店网址不允许为空!") }, emails = { @EmailValidator(fieldName = "systemConfig.email", message = "E-mail格式错误!"), @EmailValidator(fieldName = "systemConfig.smtpFromMail", message = "发件人邮箱格式错误!") })
    @InputConfig(resultName = "error")
    public String update() throws Exception {
        if (systemConfig.getPointType() == PointType.orderAmount) {
            if (systemConfig.getPointScale() < 0) {
                addActionError("积分换算比率不允许小于0!");
                return ERROR;
            }
        } else {
            systemConfig.setPointScale(0D);
        }
        SystemConfig persistent = SystemConfigUtil.getSystemConfig();
        if (shopLogo != null || defaultBigProductImage != null || defaultSmallProductImage != null || defaultThumbnailProductImage != null || watermarkImage != null) {
            String allowedUploadImageExtension = getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
            if (StringUtils.isEmpty(allowedUploadImageExtension)) {
                addActionError("不允许上传图片文件!");
                return ERROR;
            }
            String[] imageExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
            if (shopLogo != null) {
                String shopLogoExtension = StringUtils.substringAfterLast(shopLogoFileName, ".").toLowerCase();
                if (!ArrayUtils.contains(imageExtensionArray, shopLogoExtension)) {
                    addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                    return ERROR;
                }
            }
            if (defaultBigProductImage != null) {
                String defaultBigProductImageExtension = StringUtils.substringAfterLast(defaultBigProductImageFileName, ".").toLowerCase();
                if (!ArrayUtils.contains(imageExtensionArray, defaultBigProductImageExtension)) {
                    addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                    return ERROR;
                }
            }
            if (defaultSmallProductImage != null) {
                String defaultSmallProductImageExtension = StringUtils.substringAfterLast(defaultSmallProductImageFileName, ".").toLowerCase();
                if (!ArrayUtils.contains(imageExtensionArray, defaultSmallProductImageExtension)) {
                    addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                    return ERROR;
                }
            }
            if (defaultThumbnailProductImage != null) {
                String defaultThumbnailProductImageExtension = StringUtils.substringAfterLast(defaultThumbnailProductImageFileName, ".").toLowerCase();
                if (!ArrayUtils.contains(imageExtensionArray, defaultThumbnailProductImageExtension)) {
                    addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                    return ERROR;
                }
            }
            if (watermarkImage != null) {
                String watermarkImageExtension = StringUtils.substringAfterLast(watermarkImageFileName, ".").toLowerCase();
                if (!ArrayUtils.contains(imageExtensionArray, watermarkImageExtension)) {
                    addActionError("只允许上传图片文件类型: " + allowedUploadImageExtension + "!");
                    return ERROR;
                }
            }
        }
        int uploadLimit = systemConfig.getUploadLimit() * 1024;
        if (uploadLimit != 0) {
            if (shopLogo != null && shopLogo.length() > uploadLimit) {
                addActionError("网店Logo文件大小超出限制!");
                return ERROR;
            }
            if (defaultBigProductImage != null && defaultBigProductImage.length() > uploadLimit) {
                addActionError("默认商品图片（大）文件大小超出限制!");
                return ERROR;
            }
            if (defaultSmallProductImage != null && defaultSmallProductImage.length() > uploadLimit) {
                addActionError("默认商品图片（小）文件大小超出限制!");
                return ERROR;
            }
            if (defaultThumbnailProductImage != null && defaultThumbnailProductImage.length() > uploadLimit) {
                addActionError("默认缩略图文件大小超出限制!");
                return ERROR;
            }
            if (watermarkImage != null && watermarkImage.length() > uploadLimit) {
                addActionError("水印图片文件大小超出限制!");
                return ERROR;
            }
        }
        if (StringUtils.isEmpty(systemConfig.getSmtpPassword())) {
            systemConfig.setSmtpPassword(persistent.getSmtpPassword());
        }
        if (systemConfig.getIsLoginFailureLock() == false) {
            systemConfig.setLoginFailureLockCount(3);
            systemConfig.setLoginFailureLockTime(10);
        }
        if (shopLogo != null) {
            File oldShopLogoFile = new File(ServletActionContext.getServletContext().getRealPath(persistent.getShopLogo()));
            if (oldShopLogoFile.isFile()) {
                oldShopLogoFile.delete();
            }
            String shopLogoFilePath = SystemConfig.UPLOAD_IMAGE_DIR + SystemConfig.LOGO_UPLOAD_NAME + "." + StringUtils.substringAfterLast(shopLogoFileName, ".").toLowerCase();
            File shopLogoFile = new File(ServletActionContext.getServletContext().getRealPath(shopLogoFilePath));
            FileUtils.copyFile(shopLogo, shopLogoFile);
            persistent.setShopLogo(shopLogoFilePath);
        }
        if (defaultBigProductImage != null) {
            File oldDefaultBigProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(persistent.getDefaultBigProductImagePath()));
            if (oldDefaultBigProductImageFile.exists()) {
                oldDefaultBigProductImageFile.delete();
            }
            String defaultBigProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + SystemConfig.DEFAULT_BIG_PRODUCT_IMAGE_FILE_NAME + "." + StringUtils.substringAfterLast(defaultBigProductImageFileName, ".").toLowerCase();
            File defaultBigProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(defaultBigProductImagePath));
            FileUtils.copyFile(defaultBigProductImage, defaultBigProductImageFile);
            persistent.setDefaultBigProductImagePath(defaultBigProductImagePath);
        }
        if (defaultSmallProductImage != null) {
            File oldDefaultSmallProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(persistent.getDefaultSmallProductImagePath()));
            if (oldDefaultSmallProductImageFile.exists()) {
                oldDefaultSmallProductImageFile.delete();
            }
            String defaultSmallProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + SystemConfig.DEFAULT_SMALL_PRODUCT_IMAGE_FILE_NAME + "." + StringUtils.substringAfterLast(defaultSmallProductImageFileName, ".").toLowerCase();
            File defaultSmallProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(defaultSmallProductImagePath));
            FileUtils.copyFile(defaultSmallProductImage, defaultSmallProductImageFile);
            persistent.setDefaultSmallProductImagePath(defaultSmallProductImagePath);
        }
        if (defaultThumbnailProductImage != null) {
            File oldDefaultThumbnailProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(persistent.getDefaultThumbnailProductImagePath()));
            if (oldDefaultThumbnailProductImageFile.exists()) {
                oldDefaultThumbnailProductImageFile.delete();
            }
            String defaultThumbnailProductImagePath = SystemConfig.UPLOAD_IMAGE_DIR + SystemConfig.DEFAULT_THUMBNAIL_PRODUCT_IMAGE_FILE_NAME + "." + StringUtils.substringAfterLast(defaultThumbnailProductImageFileName, ".").toLowerCase();
            File defaultThumbnailProductImageFile = new File(ServletActionContext.getServletContext().getRealPath(defaultThumbnailProductImagePath));
            FileUtils.copyFile(defaultThumbnailProductImage, defaultThumbnailProductImageFile);
            persistent.setDefaultThumbnailProductImagePath(defaultThumbnailProductImagePath);
        }
        if (watermarkImage != null) {
            File oldWatermarkImageFile = new File(ServletActionContext.getServletContext().getRealPath(persistent.getWatermarkImagePath()));
            if (oldWatermarkImageFile.exists()) {
                oldWatermarkImageFile.delete();
            }
            String watermarkImagePath = SystemConfig.UPLOAD_IMAGE_DIR + SystemConfig.WATERMARK_IMAGE_FILE_NAME + "." + StringUtils.substringAfterLast(watermarkImageFileName, ".").toLowerCase();
            File watermarkImageFile = new File(ServletActionContext.getServletContext().getRealPath(watermarkImagePath));
            FileUtils.copyFile(watermarkImage, watermarkImageFile);
            persistent.setWatermarkImagePath(watermarkImagePath);
        }
        BeanUtils.copyProperties(systemConfig, persistent, new String[] { "systemName", "systemVersion", "systemDescription", "isInstalled", "shopLogo", "defaultBigProductImagePath", "defaultSmallProductImagePath", "defaultThumbnailProductImagePath", "watermarkImagePath" });
        SystemConfigUtil.update(persistent);
        redirectionUrl = "system_config!edit.action";
        return SUCCESS;
    }
}
