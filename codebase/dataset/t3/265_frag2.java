    public int deleteTransaction(Session s, long txId) throws Exception {

        try {

            String query = "delete from Transaction T where T.txId=?";

            Query q = s.createQuery(query);

            q.setLong(0, txId);

            int r = q.executeUpdate();

            return r;

        } catch (Exception e) {

            throw e;

        }

    }
