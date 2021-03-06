    public float getUserRatingAverageByRound(int round) {

        float userRatingAverage = 0;

        try {

            String sql = "select avg(rate) from users_link_rate where round=" + round;

            PreparedStatement ps = dbAccess.prepareStatement(sql);

            ResultSet rs = dbAccess.ExecQuery(ps);

            if (rs != null && rs.next()) {

                userRatingAverage = rs.getFloat("avg(rate)");

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return userRatingAverage;

    }
