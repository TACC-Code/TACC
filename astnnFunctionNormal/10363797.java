class BackupThread extends Thread {
    public void update(t_channel_sale_Form vo) throws HibernateException {
        Session session = this.beginTransaction();
        String id = vo.getId();
        t_channel_sale po = (t_channel_sale) session.load(t_channel_sale.class, new Integer(id));
        try {
            po.setId(java.lang.Integer.parseInt(vo.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setThe_month(vo.getThe_month());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setChannel_code(vo.getChannel_code());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setChannel_name(vo.getChannel_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setUpdated_day(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setRecompense(java.lang.Double.parseDouble(vo.getRecompense()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCharge_avg(java.lang.Integer.parseInt(vo.getCharge_avg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCard_sale_avg(java.lang.Integer.parseInt(vo.getCard_sale_avg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setCard_apply_avg(java.lang.Integer.parseInt(vo.getCard_apply_avg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setInsertDay(vo.getInsert_day());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            po.setOpentype(vo.getOpentype());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            session.update(po);
            this.endTransaction(true);
        } catch (Exception e) {
            this.endTransaction(false);
            e.printStackTrace();
            throw new HibernateException(e);
        }
    }
}
