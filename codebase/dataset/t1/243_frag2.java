        } catch (SQLException sex) {

            try {

                log.write(sdf.format(new java.util.Date()));

                log.write("\tError closing statement in RDBMSParser.closeStatement.");

                log.write(Parser.END_OF_LINE);

                log.write(DataDirectException.getStackTraceString(sex));

                log.write(Parser.END_OF_LINE);

            } catch (IOException ioex) {

                ioex.printStackTrace();

            }
