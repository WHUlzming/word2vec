    private String getContentType(String filename) {

        String mimeType = MimeType.getMimeTypeFromName(filename);

        if (mimeType != null) {

            return mimeType;

        }

        if (filename.endsWith("png")) {

            return "image/png";

        } else if (filename.endsWith("css")) {

            return "text/css";

        } else if (filename.endsWith("gif")) {

            return "image/gif";

        } else if (filename.endsWith("jpeg")) {

            return "image/jpeg";

        } else if (filename.endsWith("js")) {

            return "text/javascript";

        } else if (filename.endsWith("html")) {

            return "text/html";

        } else if (filename.endsWith("htm")) {

            return "text/html";

        } else {

            return "text/plain";

        }

    }
