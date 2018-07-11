    public Account getAccountFromName(long uid, int acctType, String acctName) throws Exception {

        Session s = null;

        try {

            s = HibernateUtils.getSessionFactory().getCurrentSession();

            s.beginTransaction();

            String query = "select R from Account R where R.ownerId=? and R.accountType=? and R.accountName=?";

            Query q = s.createQuery(query);

            q.setLong(0, uid);

            q.setInteger(1, acctType);
