    private boolean sendMessage(String message, BufferedWriter writer) {

        try {

            writer.write(message);

            writer.flush();

        } catch (IOException ex) {

            log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, "LogProcessor.sendMessage() -- IO Error while sending message : " + ex.getMessage());

            return false;

        }

        return true;

    }
