    public void deleteNode(Node node) throws NetworkFailureException {

        commitUpdate();

        try {

            JSONObject jsPost = new JSONObject();

            JSONArray actionList = new JSONArray();

            node.setDeleted(true);

            actionList.put(node.getUpdateAction(getActionId()));

            jsPost.put(GTaskStringUtils.GTASK_JSON_ACTION_LIST, actionList);

            jsPost.put(GTaskStringUtils.GTASK_JSON_CLIENT_VERSION, mClientVersion);

            postRequest(jsPost);

            mUpdateArray = null;

        } catch (JSONException e) {

            Log.e(TAG, e.toString());

            e.printStackTrace();

            throw new ActionFailureException("delete node: handing jsonobject failed");

        }

    }
