        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUREBANNEDCREEP)) {

            String txt = App.localization.localize("app", "irc.err_yourebannedcreep", "You are banned from this server");

            append(txt, "ERROR", false);

        } else if (msg.getType().equals(IRCMessageTypes.ERR_YOUWILLBEBANNED)) {

            String txt = App.localization.localize("app", "irc.err_youwillbebanned", "You will be banned from this server if you don't change " + "your behaviour");

            append(txt, "ERROR", false);

        } else if (msg.getType().equals(IRCMessageTypes.ERR_KEYSET)) {
