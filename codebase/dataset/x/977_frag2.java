    public void getOffLineMsg(XMPPConnection connection) {

        OfflineMessageManager offlineManager = new OfflineMessageManager(connection);

        try {

            Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager.getMessages();

            if (0 == offlineManager.getMessageCount()) {

                Log.i(TAG, "������Ϣ����: " + offlineManager.getMessageCount());

                return;

            }

            Map<String, ArrayList<Message>> offlineMsgs = new HashMap<String, ArrayList<Message>>();

            while (it.hasNext()) {

                org.jivesoftware.smack.packet.Message message = it.next();

                Log.i(TAG, "�յ�������Ϣ, Received from ��" + message.getFrom() + "�� message: " + message.getBody());

                String fromUser = message.getFrom().split("/")[0];

                showNotification(R.drawable.icon, "�¤�����å��`��", "From:" + fromUser, message.getBody(), 1);

                if (offlineMsgs.containsKey(fromUser)) {

                    offlineMsgs.get(fromUser).add(message);

                } else {

                    ArrayList<Message> temp = new ArrayList<Message>();

                    temp.add(message);

                    offlineMsgs.put(fromUser, temp);

                }

            }

            offlineManager.deleteMessages();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
