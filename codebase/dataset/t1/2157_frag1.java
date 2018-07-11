    private static final Resources resources = Resources.getDashBundle("dash.file");



    protected void writeHeader() {

    }



    /** Generate CGI script output. */

    protected void writeContents() throws IOException {

        parseFormData();

        String filename = getParameter(FILE_PARAM);

        if (filename == null) ;

        Element file = findFile(filename);

        if (file == null) {

            sendNoSuchFileMessage(filename);

            return;

        }

        File result = computePath(file, false);

        if (!metaPathVariables.isEmpty()) {

            pathVariables = metaPathVariables;

            pathVariableNames = metaPathVariableNames;

            displayNeedInfoForm(filename, null, false, MISSING_META, file);

            return;

        }

        if (result == null && !needPathInfo()) {

            sendNoSuchFileMessage(filename);

            return;

        }

        if (result == null) {

            displayNeedInfoForm(filename, result, false, MISSING_INFO, file);

            return;

        }

        if (!result.exists()) {

            if (getParameter(CONFIRM_PARAM) == null) {

                displayNeedInfoForm(filename, result, false, CREATE_CONFIRM, file);

                return;

            }

            if (isDirectory) {

                if (!result.mkdirs()) {

                    String message = resources.format("Create_Directory_Error_FMT", HTMLUtils.escapeEntities(result.getPath()));
