            try {

                throw e;

            } catch (SQLException ex) {

                java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);

            }

        } finally {

            try {

                stmt.close();

                this.fechaConexao();

            } catch (SQLException e) {

                try {

                    throw e;

                } catch (SQLException ex) {

                    java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);

                }

            }

        }

    }
