/**
 * LIEB PROJECT 2019/2020
 * BREATHALIZER - Breathing Monitor
 * @author Duarte Rodrigues
 * @author João Fonseca
 * 
 * AudioPlayer: class that implements the functionalities of a typical audio player, such as
 * start, pause, resume, go to a specific time. It also has an indicative slider of the current time.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; 
import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineEvent.Type;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener; 

public class AudioPlayer {
	
	private AudioFormat format;
	private long frameCount;
	private double duration;
	private boolean playing = false;
	private JSlider internalslider;
	private Timer playTimer;
	private boolean ignoreStateChange = false;
	private JLabel currentDuration;
    private Long currentFrame; 
    private Clip clip;   
    String status;
    AudioInputStream audioInputStream;
    private String internalPath;
    
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static DecimalFormat df1 = new DecimalFormat("#.#");

    /**
	   * Constructor to initialize audio streams and clip.
	   *
	   * @param filePath  Path for the selected audio, in .wav format.
	   * 
	   * @throws UnsupportedAudioFileException
	   * @throws IOException
	   * @throws LineUnavailableException
	   */
    public AudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException  { 

        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
        clip = AudioSystem.getClip(); 
        clip.open(audioInputStream);
        internalPath=filePath;
    }

    /**
	   * Method to return a 2 decimal precise time, in seconds.
	   *
	   * @return sec  2 decimal precise time.
	   */
    public String getStrSecond() {
    	double sec = this.getCurrentTime();
    	return df2.format(sec);
    }

    /**
	   * Method that returns the elapsed time to indicate alongside the slider.
	   *
	   * @return sec  1 decimal precise time.
	   */
    public String ElapsedTime() {
    	double sec = this.getCurrentTime();
    	return df1.format(sec);
    }

    /**
	   * Method to play the audio.
	   */
    public void play()  
    {
        if (!playing) {
            int frame = getStartDesiredFrame();
            if (frame >= frameCount) {
                frame = 0;
            }
            clip.setFramePosition(frame);
            clip.start();
            playing = true;
            playTimer.start();
        }
        status = "play";
    } 
    
    /**
	   * Method to pause the audio.
	   */
    public void pause()  
    {
        this.currentFrame = this.clip.getMicrosecondPosition();
        if (playing) {
        	clip.stop();
        playing = false;
        playTimer.stop();
        }
        status = "paused"; 
    } 

    /**
	   * Method to resume the audio.
	   * 
	   * @throws UnsupportedAudioFileException
	   * @throws IOException
	   * @throws LineUnavailableException
	   */
    public void resume() throws UnsupportedAudioFileException,IOException, LineUnavailableException
    {    	
    	//Prevents the clip from glitching if button pressed while playing
        if (status.equals("play"))  
        {
            return; 
        } 
        clip.close(); 
        resetAudioStream(); 
        clip.setMicrosecondPosition(currentFrame); 
        this.play(); 
    } 

    /**
	   * Method to restart the audio.
	   * 
	   * @throws UnsupportedAudioFileException
	   * @throws IOException
	   * @throws LineUnavailableException
	   */
    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException  
    { 
        clip.stop(); 
        clip.close(); 
        resetAudioStream(); 
        currentFrame = 0L; 
        clip.setMicrosecondPosition(0);
        updateState();
        this.play(); 
    }  
 
    /**
	   * Method to jump over to a specific time.
	   * 
	   * @param c  Position to where we want to jump in microseconds.
	   * 
	   * @throws UnsupportedAudioFileException
	   * @throws IOException
	   * @throws LineUnavailableException
	   */
    public void jump(long c) throws UnsupportedAudioFileException, IOException,LineUnavailableException  
    {
        if (c > 0 && c < clip.getMicrosecondLength())  
        { 
            clip.stop(); 
            clip.close();
            resetAudioStream(); 
            currentFrame = c; 
            clip.setMicrosecondPosition(c);
            updateState();
            this.play(); 
        } 
    } 

    /**
	   * Method to reset audio stream and frame.
	   * 
	   * @throws UnsupportedAudioFileException
	   * @throws IOException
	   * @throws LineUnavailableException
	   */
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,LineUnavailableException  
    { 
        audioInputStream = AudioSystem.getAudioInputStream(new File(internalPath).getAbsoluteFile()); 
        clip.open(audioInputStream); 
    }

    /**
	   * Method to implement the slider indicator.
	   * 
	   * @param slider     		 Slider to indicate the moment that is being listened to.
	   * @param currentDuration  The duration of the audio elapsed untill the moment
	   */
    public void AudioSlider(JSlider slider, JLabel currentDuration) {
        AudioInputStream ais = null;
        internalslider=slider;
        this.currentDuration = currentDuration;
        try {
            File file = new File(internalPath);
            ais = AudioSystem.getAudioInputStream(file);
            format = ais.getFormat();
            frameCount = ais.getFrameLength();
            duration = ((double) frameCount) / format.getFrameRate();
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
        
        // If the player is not running also stop the slider
        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType().equals(Type.STOP) || event.getType().equals(Type.CLOSE)) {                 
                    playing = false;
                    playTimer.stop();
                    updateState();
                }
            }
        });
        
        //Update slider possition every 10 ms
        playTimer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateState();
            }
        });
        
        //updates the elapsed time indicator every 250 ms. Since indicator only shows the seconds is more than an enough update rate.
        Timer delayedUpdate = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDuration.setText(ElapsedTime());
            }
        });
        delayedUpdate.setRepeats(false);
        
        // Since the slider is a mere indicator, if the user tries to change its position that will take no effect
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (ignoreStateChange) {
                    return;
                }
                delayedUpdate.restart();
            }
        });      
    }

    /**
	   * Method to extract the audio frame from the slider position (progress).
	   * 
	   * @return frame  Frame that corresponds to the slider position.
	   */
    public int getStartDesiredFrame() {
        int progress = internalslider.getValue();
        double frame = ((double) frameCount * ((double) progress / 100.0));
        return (int) frame;
    }

    /**
	   * Method to update the progress of the slider, in order for it to be concordant with the time.
	   */
    public void updateState() {
        ignoreStateChange = true;
        int frame = clip.getFramePosition();
        int progress = (int) (((double) frame / (double) frameCount) * 100);
        internalslider.setValue(progress);
        currentDuration.setText(ElapsedTime());
        ignoreStateChange = false;
    }
 
    /**
	   * Method to get the current time.
	   * 
	   * @return time current time.
	   */
    public double getCurrentTime() {
        int currentFrame = clip.getFramePosition();
        double time = (double) currentFrame / format.getFrameRate();
        return time;
    }

    /**
	   * Method to get the full audio duration
	   * 
	   * @return fullSec full audio duration in seconds.
	   */
    public int getFullDuration() {
    	int fullSec= (int) duration;
    	return fullSec;
    }    
}
