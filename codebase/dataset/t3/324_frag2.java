    public Company getCompany(String companyName) {

        if (companyName == null) {

            return null;

        }

        DatabaseAdapter db = null;

        ResultSet rs = null;

        PreparedStatement ps = null;

        try {

            db = DatabaseAdapter.getInstance();

            String sql = "select ID_FIRM, full_name, short_name, " + "	    address, chief, buh, url,  " + "	    short_info, is_work, is_search, is_deleted " + "from 	WM_LIST_COMPANY " + "where  full_name=? ";

            ps = db.prepareStatement(sql);

            ps.setString(1, companyName);

            rs = ps.executeQuery();

            Company company = null;

            if (rs.next()) {

                company = loadCompanyFromResultSet(rs);

            }

            return company;

        } catch (Exception e) {

            String es = "Error load company for name: " + companyName;

            throw new IllegalStateException(es, e);

        } finally {

            DatabaseManager.close(db, rs, ps);

            db = null;

            rs = null;

            ps = null;

        }

    }
