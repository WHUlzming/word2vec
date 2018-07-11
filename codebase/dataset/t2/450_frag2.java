    private boolean decodeAudioPacket(StreamState state) {

        boolean complete = false;

        while (!state.isPacketEmpty()) {

            int decodedBytes = audioCoder.decodeAudio(audioSamples, state.getPacket(), state.getOffset());

            if (decodedBytes < 0) {

                state.releasePacket();

                break;

            }

            state.updateTimestamps();

            state.consume(decodedBytes);

            if (audioSamples.isComplete()) {

                updateSoundSamples(audioSamples);

                complete = true;

                break;

            }

        }

        return complete;

    }
