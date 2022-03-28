package sg.edu.tp.musicstreamingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;

import sg.edu.tp.musicstreamingapp.Apputil.AppUtil;

public class MainActivity extends AppCompatActivity {
    private SongCollection songCollection = new SongCollection();
    private Handler Handler = new Handler();
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
    }
    public void handleSelection(View view)
    {
        //1. Get the ID of the Selected song
        String resourceId = AppUtil.getResourceId(this,view);
        //2.Search By ID method
        Song selectedSong = songCollection.searchById(resourceId);
        //3.Popup a message on teh screen showing the title of the song
        //AppUtil.popMessage(this,"Streaming Song: "+ selectedSong.getTitle());
        //4.Send the songs dtat to the player screen to be played.
        sendDataToActivity(selectedSong);
    }
    public void sendDataToActivity(Song song)
    {
        Intent intent = new Intent(this,PlaySongActivity.class);
        intent.putExtra("id",song.getId());
        intent.putExtra("title",song.getTitle());
        intent.putExtra("artist",song.getArtist());
        intent.putExtra("fileLink",song.getFileLink());
        intent.putExtra("coverArt", song.getCoverArt());
        intent.putExtra("songLength",song.getsongLength());
        startActivity(intent);
    }
    public void logout (View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void SearchByTitle(View view) //search song
    {
        EditText SongTitle = (EditText) findViewById(R.id.txtSearch);
        String t = (SongTitle.getText().toString());
        Song selectedSong = songCollection.searchByTitle(t);
        sendDataToActivity(selectedSong);

    }

}
