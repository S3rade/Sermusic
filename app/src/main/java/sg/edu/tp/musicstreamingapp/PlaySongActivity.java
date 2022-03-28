package sg.edu.tp.musicstreamingapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

import sg.edu.tp.musicstreamingapp.Apputil.AppUtil;


public class PlaySongActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://p.scdn.co/mp3-preview/";
    private String songId = "";
    private String title = "";
    private String artist = "";
    private String fileLink = "";
    private String coverArt = "";
    private String url = "";
    private MediaPlayer player = null;
    private int musicPosition = 0;
    private ImageButton btnPlayPause = null;
    private Random rand;
    private SeekBar seekbar;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    private TextView current;
    private android.os.Handler Handler = new Handler();
    private int loopedflag =0;
    private Button btnShuffle = null;
    boolean Boolean1 = false;
    SongCollection songCollection = new SongCollection();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        btnPlayPause = (ImageButton)findViewById(R.id.imagebtnPlayPause);
        retrieveData();
        displaySong(title, artist, coverArt);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        current = (TextView)findViewById(R.id.current);

        seekbar.setMax(30); //set to the max
        seekbar.setProgress(00); //starting
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(fromUser)
                {
                    player.seekTo(progress);
                    seekBar.setProgress(progress);
                }

                int minutes = progress /1000/60;
                int seconds = (progress /1000)% 60;
                String time = String.format("%02d:%02d", minutes, seconds);
                current.setText(time);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void retrieveData() {
        Bundle songData = this.getIntent().getExtras();
        songId = songData.getString("id");
        title = songData.getString("title");
        artist = songData.getString("artist");
        fileLink = songData.getString("fileLink");
        AppUtil.popMessage(this,"Streaming: "+ title);
        coverArt = songData.getString("coverArt");
        url = BASE_URL+fileLink;

    }

    private void displaySong(String title, String artist, String coverArt) {
        TextView txtTitle = (TextView) findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        TextView txtArtist = (TextView) findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        int imageId = AppUtil.getImageIdFromDrawable(this, coverArt);
        ImageView ivCoverArt = (ImageView) findViewById(R.id.imgCoverArt);
        ivCoverArt.setImageResource(imageId);
    }

    public void preparePlayer() {
        player = new MediaPlayer();
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playOrPauseMusic(View view)
    {
        if (player == null)
            preparePlayer();
        finalTime = player.getDuration();
        startTime = player.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        seekbar.setProgress((int) startTime);
        Handler.postDelayed(UpdateSongTime, 100);
        if (!player.isPlaying())
        {
            if (musicPosition > 0)
            {
                player.seekTo(musicPosition);
            }
            player.start();
            btnPlayPause.setBackgroundResource(R.drawable.pause);
            setTitle("Now Playing: " + title + " - " + artist);
            gracefullyStopWhenMusicEnds();
        }
        else
            pauseMusic();

    }
    public void LoopOnOff(View view)
    {

        if(player != null && loopedflag ==0)
        {
        player.setLooping(true);
        player.start();
        loopedflag = 1;
        AppUtil.popMessage(this,"Loop On");
        view.setBackgroundResource(R.drawable.loop);
        }
        else if (player !=null && loopedflag ==1)
        {
            player.setLooping(false);
          loopedflag=0;
            AppUtil.popMessage(this,"Loop Off");
            view.setBackgroundResource(R.drawable.loopoff);
        }
        else if(!player.isPlaying())
        {
            player.seekTo(musicPosition);
            musicPosition = player.getCurrentPosition();
        }

    }
    private void pauseMusic()
    {
        player.pause();
        musicPosition=player.getCurrentPosition();
        btnPlayPause.setBackgroundResource(R.drawable.play);
    }

    private void gracefullyStopWhenMusicEnds()
    {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {@Override
        public void onCompletion(MediaPlayer mediaplayer)
        {
            stopActivities();
        }
        });
    }
    private void stopseek()
    {
        if(musicPosition > 0)
        {
            player.seekTo(musicPosition);

            player.start();
            btnPlayPause.setBackgroundResource(R.drawable.pause);
            setTitle("Now Playing: " + title + " - " + artist);
            gracefullyStopWhenMusicEnds();
        }
        else
            {
        pauseMusic();
              }
    }
    public void stopActivities()
    {
        if(player != null)
        {
            musicPosition=0;
            setTitle(" ");
            player.stop();
            player.release();
            player=null;
        }
    }

    public void playPrevious (View view)
    {
        Song prevSong = songCollection.getprevSong(songId);
        if ( prevSong !=null)
        {
            songId = prevSong.getId();
            title = prevSong.getTitle();
            artist=prevSong.getArtist();
            fileLink= prevSong.getFileLink();
            coverArt= prevSong.getCoverArt();
            url = BASE_URL + fileLink ;
            displaySong(title,artist,coverArt);
            stopActivities();
            playOrPauseMusic(view);
        }
    }

    public void playNext (View view)
    {
        Song nextSong = songCollection.getNextSong(songId);
        if (nextSong != null ) {

            songId = nextSong.getId();
            title = nextSong.getTitle();
            artist = nextSong.getArtist();
            fileLink = nextSong.getFileLink();
            coverArt = nextSong.getCoverArt();
            url = BASE_URL + fileLink;
            displaySong(title, artist, coverArt);
            stopActivities();
            playOrPauseMusic(view);
        }
    }
    public void playRandom(View view)
    {
        int random = (int)(Math.random()*4);
        Song s = songCollection.getSongbyIndex(random);
        if(player != null)
        {
            songId = s.getId();
            title = s.getTitle();
            artist = s.getArtist();
            fileLink = s.getFileLink();
            coverArt = s.getCoverArt();
            url = BASE_URL + fileLink;
            displaySong(title, artist, coverArt);
            stopActivities();
            playOrPauseMusic(view);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        player.release();
        player = null;
        seekbar.setProgress(0);
        startTime = 0;
        finalTime = 0;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (player!=null)
            {
                startTime = player.getCurrentPosition();
                seekbar.setProgress((int) startTime);
                Handler.postDelayed(this, 100);
            }
        }
    };
    public void playShuffle(View view) {
        if (!Boolean1) {
           Boolean1 = true;
            AppUtil.popMessage(this, "Shuffle: On");
        }

        else
        {
            Boolean1 = false;
            AppUtil.popMessage(this,"Shuffle: Off");
        }
    }
    public Song getShuffleSong()
    {
        Song song = null;
        Song[] Songs = new Song[4];
        Random random = new Random();
        int numRand = random.nextInt(Songs.length);

        song = Songs[numRand];
        return song;

    }
}