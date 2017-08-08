
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author SVSTEM
 */
public class audio2 {
    private static boolean call = false;
    private static InetAddress customerIP;
    private int sendPort = 8181; //the port to which the agent wishes to send
    private int receivePort = 9191; //the agent own port, to listen on for traffic
    private DatagramSocket receiveSocket; //the socket to receive on
    private DatagramSocket sendSocket; //the socket to send on
    private static boolean stopCapture;
    private static boolean stopPlay;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;

    public audio2() throws UnknownHostException{
        this.customerIP = InetAddress.getByName("127.0.0.1");
    }
    
  /*      void setStopCapture(boolean stopCapture){
        this.stopCapture = stopCapture;
    }*/

    /*
     * This method creates and returns an AudioFormat object for a given set
     * of format parameters.
     */
    private AudioFormat getAudioFormat() {
        //8000,11025,16000,22050,44100

    float sampleRate = 16000.0F;
    int sampleInbits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = false;
    return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
}
    

    /* 
     * This method captures audio input
     * from a microphone and sends it as a UDP packet.
     */
    class CaptureAudio implements Runnable	{

        private byte[] tempBuffer;
        private DatagramSocket socket;
        private InetAddress ip;
        private int port;

        public CaptureAudio(DatagramSocket socket, InetAddress ip, int port)	{
            this.socket = socket;
            this.ip = ip;
            this.port = port;
            this.tempBuffer = new byte[14000];
        }

        public void run()	{  
            try {

                stopCapture = false;

                //Get everything set up for capture
                audioFormat = getAudioFormat();
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine)
                AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                try{
                    //Loop until stopCapture is set by another thread.
                    while(!stopCapture){
                        //Read data from the internal buffer of the data line.
                        int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                        if(cnt > 0){
                            DatagramPacket outPacket = new DatagramPacket(tempBuffer, tempBuffer.length, this.ip, this.port);
                            this.socket.send(outPacket);
                        }
                    }
                }catch (Exception e) {}

            } catch (Exception e) {}
        }
    }

    /* This method receives audio input
     * from a UDP packet and plays it.
     */
    class PlayAudio implements Runnable {

        private DatagramSocket socket;
        private byte[] tempBuffer;

        public PlayAudio(DatagramSocket socket)	{
            this.socket = socket;
            this.tempBuffer = new byte[14000];
        }

        public void run()	{
            try{

                DatagramPacket inPacket;
                stopPlay = false;

                //Loop until stopPlay is set by another thread.
                while (!stopPlay)	{

                    //Put received data into a byte array object
                    inPacket = new DatagramPacket(tempBuffer, tempBuffer.length);
                    this.socket.receive(inPacket);

                    byte[] audioData = inPacket.getData();

                    //Get an input stream on the byte array containing the data
                    InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
                    AudioFormat audioFormat = getAudioFormat();
                    audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length/audioFormat.getFrameSize());
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                    sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
                    sourceDataLine.open(audioFormat);
                    sourceDataLine.start();

                    try { 
                        int cnt;
                        //Keep looping until the input read method returns -1 for empty stream.
                        while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
                            if(cnt > 0){
                                //Write data to the internal buffer of the data line where it will be delivered to the speaker.
                                sourceDataLine.write(tempBuffer, 0, cnt);
                            }
                        }
                        //Block and wait for internal buffer of the data line to empty.
                        sourceDataLine.drain();
                        sourceDataLine.close();
                    }catch (Exception e) {}
                }
            } catch (Exception e) {}
        }

    }

    /*
     * Initiates a call between two clients.
     */
    public void callConnect()	{

        //Initiate sockets to use for audio streaming
        try {
            receiveSocket = new DatagramSocket(receivePort);
            sendSocket = new DatagramSocket(sendPort);
        } catch (Exception e)	{}

        Thread CaptureAudio = new Thread(new CaptureAudio(sendSocket, customerIP, receivePort));
        CaptureAudio.start();

        Thread PlayAudio = new Thread(new PlayAudio(receiveSocket));
        PlayAudio.start();
    }

    /*
     * Disconnects the client from the current call.
     */
    public void callDisconnect()	{

        try	{
                //stop the threads
                stopCapture = true;
                stopPlay = true;

                //wait for threads to terminate
                try {
                    Thread.sleep(200);
                } catch (Exception e) {}

                //close the sockets
                try {
                    sendSocket.close();
                    receiveSocket.close();
                } catch (Exception e) {}
        } catch (Exception e){}
    }  
}
