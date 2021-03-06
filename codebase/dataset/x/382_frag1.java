                    cfg.put("set-complete", "true");

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmGen DcmGen = new DcmGen(cfg, new DcmURL(args[optind]), argc);

            DcmGen.execute(args, optind + 1);

        } catch (IllegalArgumentException e) {

            exit(e.getMessage(), true);

        }

    }
