                AudioFormat decoded_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, base_format.getSampleRate(), 16, base_format.getChannels(), base_format.getChannels() * 2, base_format.getSampleRate(), false);

                decodedInputStream = AudioSystem.getAudioInputStream(decoded_format, audioInputStream);
