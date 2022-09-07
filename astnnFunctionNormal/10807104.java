class BackupThread extends Thread {
    public ItemPromotionsResult umpPromotionGet(ItemRequest itemRequest) throws UnsupportedOperationException {
        ItemPromotionsResult res = new ItemPromotionsResult();
        TaobaoItemRequest itemReq = (TaobaoItemRequest) itemRequest;
        if (itemReq == null || itemReq.getShopId() == null || itemReq.getItemId() == null) {
            logger.error("umpPromotionGet param error:itemReq=" + itemReq);
            res.setError(ResultConstants.RESULT_PARAM_NULL, ResultConstants.RESULT_PARAM_NULL_INFO);
            return res;
        }
        UmpPromotionGetRequest req = new UmpPromotionGetRequest();
        if (StringUtil.isNotBlank(itemReq.getChannelKey())) {
            req.setChannelKey(itemReq.getChannelKey());
        }
        String sessionKey = commonCache.getSessionKey(itemReq.getShopId());
        if (StringUtil.isBlank(sessionKey)) {
            logger.error("umpPromotionGet shop[" + itemReq.getShopId() + "] sessionKey is empty.");
            res.setError(ResultConstants.RESULT_SESSIONKEY_NULL, ";shopId[" + itemReq.getShopId() + "] " + ResultConstants.RESULT_SESSIONKEY_NULL_INFO);
            return res;
        }
        try {
            UmpPromotionGetResponse response = taobaoClient.execute(req, sessionKey);
            if (!response.isSuccess()) {
                logger.error("umpPromotionGet ErrorCode=" + response.getErrorCode() + ";ErrorMsg=" + response.getMsg() + ";itemReq=" + itemReq);
                res.setError(response.getErrorCode(), response.getMsg());
                return res;
            }
            res.setPromotions(response.getPromotions());
        } catch (ApiException e) {
            logger.error("umpPromotionGet ApiException", e);
            res.setError(ResultConstants.RESULT_TAOBAO_TOP_API_ERROR, ResultConstants.RESULT_TAOBAO_TOP_API_ERROR_INFO);
            return res;
        }
        return res;
    }
}
