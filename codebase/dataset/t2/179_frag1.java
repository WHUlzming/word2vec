                case 'y':

                    putKey(cfg, g.getOptarg());

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }
