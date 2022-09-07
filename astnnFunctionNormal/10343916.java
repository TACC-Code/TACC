class BackupThread extends Thread {
    @Test
    public void shouldNotCompleteTransactionWhenDebtorIsNotValid() throws ServiceException {
        valueToBeTransfered = new BigDecimal("90");
        given(validationService.conformsTo(debtor)).willReturn(Boolean.FALSE);
        try {
            paymentService.transferFrom(debtor, creditor, valueToBeTransfered);
        } catch (ServiceException e) {
            assertTrue(true);
        } finally {
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered.negate());
            verify(paymentRepository, never()).addBalance(debtor, valueToBeTransfered);
        }
    }
}
