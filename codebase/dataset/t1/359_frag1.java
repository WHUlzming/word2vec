    public static void saveCustomTitlesAndColumnNumbers(String usrlogin, String tabid, String[] custom_titles, String[] column_numbers) throws DbException {

        Db db = null;

        String sql = "";

        try {

            db = new Db();

            Statement stmt = db.getStatement();

            SQLRenderer r = new SQLRenderer();

            int seq = 0;

            for (int i = 0; i < custom_titles.length; i++) {

                seq++;

                r.clear();

                r.add("module_custom_title", custom_titles[i]);

                r.add("column_number", column_numbers[i]);

                r.update("user_login", usrlogin);

                r.update("tab_id", tabid);

                r.update("sequence", seq);

                sql = r.getSQLUpdate("user_module_template");

                stmt.executeUpdate(sql);

            }

        } catch (SQLException ex) {

            throw new DbException(ex.getMessage() + ": " + sql);

        } finally {

            if (db != null) db.close();

        }

    }
