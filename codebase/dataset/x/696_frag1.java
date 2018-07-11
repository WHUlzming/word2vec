    protected void initAddUserToListButton() {

        Button b = (Button) findViewById(R.id.testAddUserToListButton);

        if (b == null) Log.e(Config.SS_TAG, "null value for button"); else {

            b.setOnClickListener(new OnClickListener() {



                @Override

                public void onClick(View v) {

                    Log.d(Config.SS_TAG, "Add User to List button called.");

                    try {

                        JSONRPCMethod createList = new JSONRPCMethod.AddUserToList("test@gmail.com", "list1");

                        JSONObject returnValue = RPCClient.execute(createList);

                        String ret = returnValue.getString(OneListProtocol.AddUserToList.RETURN[0]);

                        Log.d(Config.SS_TAG, "Message Received: " + ret);

                    } catch (JSONException e) {

                        Log.e(Config.SS_TAG, "We got an error while making the object");

                        e.printStackTrace();

                    }

                }

            });

        }

    }
