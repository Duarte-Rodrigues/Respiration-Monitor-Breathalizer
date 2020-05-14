
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
	
	// to store current position 
    private Long currentFrame; 
    private Clip clip;   
    // current status of clip 
    String status;
    
    private static DecimalFormat df2 = new DecimalFormat("#.##");
      
    AudioInputStream audioInputStream;
    private String internalPath;
    //===============================================================================================================
    // constructor to initialize streams and clip 
    public AudioPlayer(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException  { 
        // create AudioInputStream object 
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
        // create clip reference 
        clip = AudioSystem.getClip(); 
        // open audioInputStream to the clip 
        clip.open(audioInputStream);
        
        internalPath=filePath;
    }
    
    public String getStrSecond() {
    	
    	double sec = this.getCurrentTime();
    	return df2.format(sec);
    }


    // Method to play the audio 
    public void play()  
    { 
        //start the clip 
        //clip.start();
        
        if (!playing) {
            int frame = getDesiredFrame();
            if (frame >= frameCount) {
                frame = 0;
            }
            clip.setFramePosition(frame);
            clip.start();
            //action.setText("Stop");
            playing = true;
            playTimer.start();
        }
        
        status = "play";
//      this.getFrames();
    } 
      
    // Method to pause the audio 
    public void pause()  
    { 
        if (status.equals("paused"))  
        { 
            System.out.println("audio is already paused"); 
            return; 
        } 
        this.currentFrame = this.clip.getMicrosecondPosition();
        if (playing) {
        	clip.stop();
        playing = false;
        playTimer.stop();
        }
        
        status = "paused"; 
    } 
      
    // Method to resume the audio 
    public void resume() throws UnsupportedAudioFileException,IOException, LineUnavailableException
    {    	
        if (status.equals("play"))  
        {
            System.out.println("Audio is already being played"); 
            return; 
        } 
        clip.close(); 
        resetAudioStream(); 
        clip.setMicrosecondPosition(currentFrame); 
        this.play(); 
    } 
      
    // Method to restart the audio 
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
      
    // Method to jump over a specific part 
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
      
    // Method to reset audio stream and frame
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,LineUnavailableException  
    { 
        audioInputStream = AudioSystem.getAudioInputStream(new File(internalPath).getAbsoluteFile()); 
        clip.open(audioInputStream); 
    }

    //==============================================================================================================================
    //Method to implement a slider
    public void AudioSlider(JSlider slider, JLabel currentDuration ) {
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
        
        

        
        playTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateState();
            }
        });
        
        Timer delayedUpdate = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //int time = (int)getCurrentTime();
                currentDuration.setText(getStrSecond());//"Current duration: " + 
            }
        });
        
        delayedUpdate.setRepeats(false);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (ignoreStateChange) {
                    return;
                }
                delayedUpdate.restart();
            }
        });
        
//        slider.addChangeListener(new ChangeListener() {
//        	public void stateChanged(ChangeEvent event) {
//        		if(slider.getValueIsAdjusting()==true) {
//        			clip.stop(); 
//                    clip.close();
//                    try {
//						resetAudioStream();
//					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        			
//        			
//        			int value = slider.getValue();
//        			moveChange(value);
//        		}
//
//        	}
//        });
        
    }

    public int getDesiredFrame() {
        int progress = internalslider.getValue();
        double frame = ((double) frameCount * ((double) progress / 100.0));
        return (int) frame;
    }

    public void updateState() {
        ignoreStateChange = true;
        int frame = clip.getFramePosition();//o clip continua a correr
        int progress = (int) (((double) frame / (double) frameCount) * 100);
        internalslider.setValue(progress);
        currentDuration.setText(getStrSecond());//"Current duration: " + 
        ignoreStateChange = false;
    }

    public double getCurrentTime() {
        int currentFrame = clip.getFramePosition();
        double time = (double) currentFrame / format.getFrameRate();
        return time;
    }
    
    public int getFullDuration() {
    	int fullSec= (int) duration;
    	return fullSec;
    }
    
//    public void moveChange(int newProgress) {
//    		
//    		internalslider.setValue(newProgress);
//    		int newframe=(int) ((newProgress*(double) frameCount)/100);//importante
//    		clip.setFramePosition(newframe);
//            updateState();
//            this.play();
//    }

}
