class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    protected Pagination findByCriteria(Criteria crit, int pageNo, int pageSize) {
        CriteriaImpl impl = (CriteriaImpl) crit;
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<CriteriaImpl.OrderEntry> orderEntries;
        try {
            orderEntries = (List) MyBeanUtils.getFieldValue(impl, ORDER_ENTRIES);
            MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, new ArrayList());
        } catch (Exception e) {
            throw new RuntimeException("cannot read/write 'orderEntries' from CriteriaImpl", e);
        }
        int totalCount = ((Number) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        Pagination p = new Pagination(pageNo, pageSize, totalCount);
        if (totalCount < 1) {
            p.setList(new ArrayList());
            return p;
        }
        crit.setProjection(projection);
        if (projection == null) {
            crit.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            crit.setResultTransformer(transformer);
        }
        try {
            MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, orderEntries);
        } catch (Exception e) {
            throw new RuntimeException("set 'orderEntries' to CriteriaImpl faild", e);
        }
        crit.setFirstResult(p.getFirstResult());
        crit.setMaxResults(p.getPageSize());
        p.setList(crit.list());
        return p;
    }
}
