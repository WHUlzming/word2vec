                if (result) {

                    accessKey = m.group(1);

                    secretKey = m.group(2);

                    URL = "s3://" + m.group(3);

                } else {

                    try {

                        HashMap<String, String> settings = (HashMap<String, String>) ConfigTools.getSettings();

                        accessKey = settings.get("AWS_ACCESS_KEY");

                        secretKey = settings.get("AWS_SECRET_KEY");

                    } catch (Exception e) {
