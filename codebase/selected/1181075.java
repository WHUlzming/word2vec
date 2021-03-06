package org.spantus.work.io;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.spi.mpeg.sampled.file.MpegEncoding;
import org.spantus.core.extractor.SignalFormat;
import org.spantus.core.io.DefaultAudioReader;
import org.spantus.logger.Logger;

/**
 * 
 * @author mondhs
 *
 */
public class Mp3SignalReader extends DefaultAudioReader {

    Logger log = Logger.getLogger(getClass());

    @Override
    public boolean isFormatSupported(URL url) {
        AudioFileFormat audioFileFormat;
        try {
            audioFileFormat = AudioSystem.getAudioFileFormat(url);
            if (audioFileFormat.getFormat().getEncoding() instanceof MpegEncoding) {
                return true;
            }
        } catch (UnsupportedAudioFileException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    @Override
    public void readAudioInternal(List<URL> urls) throws UnsupportedAudioFileException, IOException {
        URL url = urls.get(0);
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        Long size = 1L;
        byte[] data = new byte[4096];
        started(size);
        int nBytesRead = 0;
        long index = 0;
        while (nBytesRead != -1) {
            nBytesRead = din.read(data, 0, data.length);
            if (nBytesRead != -1) {
                for (byte readByte : data) {
                    getWraperExtractorReader().put((byte) readByte);
                    processed(index, size);
                    index++;
                }
            }
        }
        getWraperExtractorReader().pushValues();
        din.close();
        ended();
    }

    public SignalFormat getFormat(URL url) {
        SignalFormat signalFormat = null;
        AudioInputStream in;
        try {
            signalFormat = new SignalFormat();
            in = AudioSystem.getAudioInputStream(url);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            Long size = Long.valueOf(in.getFrameLength() * baseFormat.getFrameSize());
            signalFormat.setLength(size.doubleValue());
            signalFormat.setSampleRate((double) decodedFormat.getSampleRate());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signalFormat;
    }

    public AudioFormat getCurrentAudioFormat(URL url) {
        AudioFormat decodedFormat = null;
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(url);
            AudioFormat baseFormat = in.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodedFormat;
    }

    public AudioFileFormat getAudioFormat(URL url) {
        return null;
    }
}
