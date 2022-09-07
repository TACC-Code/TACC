class BackupThread extends Thread {
    static double ReadFile(ArrayList<MonthlyExpense> MEs, AccountType types) throws IOException {
        double numberOfLines = 0.0;
        CSVReader reader = new CSVReader(new FileReader("cvreport.data"));
        String nextLine[];
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[0].equals("*")) {
                System.out.println(new Date() + " COMMENT: " + nextLine[1]);
                Global.numberOfData++;
            } else {
                Global.numberOfData++;
                if (nextLine[6].equals("")) {
                    System.out.println(nextLine[0]);
                }
                if (nextLine[1].equals("OPENACCT")) {
                    float balance;
                    balance = Float.valueOf(nextLine[6].trim()).floatValue();
                    Global.acct.add(new Account(nextLine[2], nextLine[3], balance, nextLine[0]));
                    types.addItem(nextLine[3]);
                    lgr.debug(new Date() + " - New Account: " + nextLine[2] + " - " + Global.acct.size());
                    Account a = (Account) Global.acct.get(0);
                    float oldBalanceNetworth = a.getBalance();
                    float newBalanceNetworth = oldBalanceNetworth + balance;
                    a.setBalance(newBalanceNetworth);
                    a.addPop(nextLine[0], newBalanceNetworth);
                    if (nextLine[3].equals("CHEQUING") || nextLine[3].equals("RRSP") || nextLine[3].equals("RESP") || nextLine[3].equals("INVESTMENTS") || nextLine[3].equals("ASSETT")) {
                        Account b = (Account) Global.acct.get(1);
                        float boldBalanceNetworth = b.getBalance();
                        float bnewBalanceNetworth = boldBalanceNetworth + balance;
                        b.setBalance(bnewBalanceNetworth);
                        b.addPop(nextLine[0], bnewBalanceNetworth);
                        ArrayList<Security> p = b.getPortfolio();
                        p.add(new Security(nextLine[2], "1", String.valueOf(balance), nextLine[0]));
                    }
                    if (nextLine[3].equals("LOANS") || nextLine[3].equals("MORTGAGE") || nextLine[3].equals("CC")) {
                        Account c = (Account) Global.acct.get(2);
                        float coldBalanceNetworth = c.getBalance();
                        float cnewBalanceNetworth = coldBalanceNetworth + balance;
                        c.setBalance(cnewBalanceNetworth);
                        c.addPop(nextLine[0], cnewBalanceNetworth);
                        ArrayList<Security> p = c.getPortfolio();
                        p.add(new Security(nextLine[2], "1", String.valueOf(balance * (-1)), nextLine[0]));
                    }
                }
                if (nextLine[1].equals("EXP")) {
                    float expense;
                    expense = Float.valueOf(nextLine[6].trim()).floatValue();
                    Account a = (Account) Global.acct.get(0);
                    float oldBalanceNetworth = a.getBalance();
                    float newBalanceNetworth = oldBalanceNetworth + expense;
                    a.setBalance(newBalanceNetworth);
                    a.addPop(nextLine[0], newBalanceNetworth);
                    StringTokenizer st = new StringTokenizer(nextLine[0], "/", false);
                    int year = Integer.parseInt(st.nextToken());
                    int month = Integer.parseInt(st.nextToken());
                    int day = Integer.parseInt(st.nextToken());
                    Global.ExpensesMonthly.add(new Monthly(year, month, day, expense, nextLine[4]));
                    if (nextLine[4].equals("MORTGAGE_INTEREST")) {
                        Global.MortgIntMonthly.add(new Monthly(year, month, day, expense, nextLine[4]));
                    }
                    if (!Global.ExpenseType.contains(nextLine[4])) {
                        Global.ExpenseType.add(nextLine[4]);
                    }
                    Collections.sort(Global.ExpenseType);
                }
            }
            if (nextLine[1].equals("BUY")) {
                int i = 0;
                for (i = 0; i < Global.acct.size(); i++) {
                    Account a = (Account) Global.acct.get(i);
                    Account aN = (Account) Global.acct.get(0);
                    String acctName = a.getName();
                    String account = nextLine[2];
                    if (account.equals(acctName)) {
                        int j = 0;
                        boolean found = false;
                        ArrayList<Security> p = a.getPortfolio();
                        ArrayList<Security> pN = aN.getPortfolio();
                        for (j = 0; j < p.size(); j++) {
                            Security s = (Security) p.get(j);
                            String security = nextLine[3];
                            String secName = s.getName();
                            if (security.equals(secName)) {
                                found = true;
                                s.addMore(nextLine[3], nextLine[4], nextLine[5], nextLine[0]);
                            }
                        }
                        if (found == false) {
                            p.add(new Security(nextLine[3], nextLine[4], nextLine[5], nextLine[0]));
                        }
                        a.AddAllsecs(nextLine[0]);
                        for (j = 0; j < pN.size(); j++) {
                            Security s = (Security) pN.get(j);
                            String security = nextLine[3];
                            String secName = s.getName();
                            if (security.equals(secName)) {
                                found = true;
                                s.addMore(nextLine[3], nextLine[4], nextLine[5], nextLine[0]);
                            }
                        }
                        if (found == false) {
                            pN.add(new Security(nextLine[3], nextLine[4], nextLine[5], nextLine[0]));
                        }
                    }
                }
            }
            if (nextLine[1].equals("SELL")) {
                int i = 0;
                for (i = 0; i < Global.acct.size(); i++) {
                    Account a = (Account) Global.acct.get(i);
                    Account aN = (Account) Global.acct.get(0);
                    String acctName = a.getName();
                    String account = nextLine[2];
                    if (account.equals(acctName)) {
                        int j = 0;
                        boolean found = false;
                        ArrayList<Security> p = a.getPortfolio();
                        ArrayList<Security> pN = aN.getPortfolio();
                        for (j = 0; j < p.size(); j++) {
                            Security s = (Security) p.get(j);
                            String security = nextLine[3];
                            String secName = s.getName();
                            if (security.equals(secName)) {
                                found = true;
                                s.subMore(nextLine[3], nextLine[4], nextLine[5], nextLine[0]);
                            }
                        }
                        if (found == false) {
                            p.add(new Security(nextLine[3], nextLine[4], nextLine[5], nextLine[0]));
                        }
                        a.AddAllsecs(nextLine[0]);
                        for (j = 0; j < pN.size(); j++) {
                            Security s = (Security) pN.get(j);
                            String security = nextLine[3];
                            String secName = s.getName();
                            if (security.equals(secName)) {
                                found = true;
                                s.subMore(nextLine[3], nextLine[4], nextLine[5], nextLine[0]);
                            }
                        }
                        if (found == false) {
                            pN.add(new Security(nextLine[3], nextLine[4], nextLine[5], nextLine[0]));
                        }
                    }
                }
            }
            if (nextLine[1].equals("DIV")) {
                float dividend;
                dividend = Float.valueOf(nextLine[6].trim()).floatValue();
                Account a = (Account) Global.acct.get(0);
                float oldBalanceNetworth = a.getBalance();
                float newBalanceNetworth = oldBalanceNetworth + dividend;
                a.setBalance(newBalanceNetworth);
                a.addPop(nextLine[0], newBalanceNetworth);
            }
            if (nextLine[1].equals("INC")) {
                float income;
                income = Float.valueOf(nextLine[6].trim()).floatValue();
                Account a = (Account) Global.acct.get(0);
                float oldBalanceNetworth = a.getBalance();
                float newBalanceNetworth = oldBalanceNetworth + income;
                a.setBalance(newBalanceNetworth);
                a.addPop(nextLine[0], newBalanceNetworth);
            }
            if (nextLine[1].equals("UPD")) {
                int i = 0;
                for (i = 0; i < Global.acct.size(); i++) {
                    Account a = (Account) Global.acct.get(i);
                    String acctName = a.getName();
                    String account = nextLine[2];
                    if (account.equals(acctName)) {
                        int j = 0;
                        @SuppressWarnings("unused") boolean found = false;
                        ArrayList<Security> p = a.getPortfolio();
                        for (j = 0; j < p.size(); j++) {
                            Security s = (Security) p.get(j);
                            String security = nextLine[3];
                            String secName = s.getName();
                            if (security.equals(secName)) {
                                found = true;
                                float price = Float.valueOf(nextLine[5].trim()).floatValue();
                                s.setCurrPrice(price);
                                s.setCurrMarket(price * s.getUnits());
                            }
                        }
                    }
                }
            }
            if (nextLine[1].equals("XFR")) {
            }
        }
        return numberOfLines;
    }
}
