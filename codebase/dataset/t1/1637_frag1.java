                public void run() {

                    DataOutputStream stdin = new DataOutputStream(tac.getOutputStream());

                    try {

                        for (int x = 0; x < 10000; x++) {

                            for (int i = 0; i < testData.length; i++) {

                                charsWritten += testData[i].length();

                                stdin.writeUTF(testData[i]);

                            }

                        }

                        stdin.flush();

                        stdin.close();

                    } catch (IOException e) {

                        throw new Error("TestRuntimeExec FAILED");

                    }

                }
