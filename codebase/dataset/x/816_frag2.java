            MessageQueueManager aqm = mediator.getArtifactQueueManager();

            Object jossoMsg = aqm.pullMessage(new ArtifactImpl(jossoArtifact));

            return new MediationMessageImpl(httpMsg.getMessageId(), jossoMsg, null, relayState, null, state);

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage(), e);
