    public void setTestCaseInfo(Document htmlDoc) {

        if (htmlDoc == null) {

            tcDocsPane.setDocument(EMPTY_DOC);

        } else {

            tcDocsPane.setDocument(htmlDoc);

        }

        tcDocsPane.setCaretPosition(0);

        tcDocsPane.scrollRectToVisible(new Rectangle(0, 0, 0, 0));

        tcDocsPane.setEditable(false);

    }
