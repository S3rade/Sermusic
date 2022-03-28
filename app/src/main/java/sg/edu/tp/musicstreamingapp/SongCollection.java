package sg.edu.tp.musicstreamingapp;


public class SongCollection {
    private Song[] songs = new Song[4];
    public SongCollection()
    {
        prepareSongs();
    }
    private void prepareSongs()
    {
        Song theWayYouLookTonight = new Song(
                "S1001",
                "The Way You Look Tonight",
                "Michael Buble",
                "a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                4.66,
                "michael_buble_collection");
        Song billieJean = new Song(
                "S1002",
                "Billie Jean",
                "Michael Jackson",
                "f504e6b8e037771318656394f532dede4f9bcaea?cid=2afe87a64b0042dabf51f37318616965",
                4.9,
                "billie_jean");
        Song littledidyouknow = new Song(
                "S1003",
                "Little Did You Know",
                "Alex & Sierra",
                "8ef3d9b2e67d48f3d7f5074a99762c56a959aacc?cid=2afe87a64b0042dabf51f37318616965",
                3.09,
                "littledidyouknow");
        Song risinghope = new Song(
                "S1004",
                "Rising Hope",
                "LiSA",
                "f471fd15b70529d9fed7afe709a838b69fed80f7?cid=2afe87a64b0042dabf51f37318616965",
                4.17,
                "lisa");

        songs[0]=theWayYouLookTonight;
        songs[1]=billieJean;
        songs[2]=littledidyouknow;
        songs[3]=risinghope;
    }

    //method searchById
    public Song searchById(String id)
    {
        Song s = null;
        for (int i =0;i<songs.length;i++)
        {
            s = songs[i];
            if(s.getId().equals(id))
            {
                return s;
            }
        }
        return null;
    }
    public Song searchByTitle(String title)
    {
        Song s = null;
        for(int i = 0; i < songs.length; i++)
        {
            s = songs[i];

            if(s.getTitle().equals(title))
            {
                return s;
            }
        }
        return null;
    }
    public Song getSongbyIndex (int index){return songs[index];}

    public Song getNextSong(String currentSongId)
    {
        Song song = null;
        for(int index = 0;index < songs.length;index++)
        {
            if (songs[index].getId().equals(currentSongId)&&(index<songs.length -1 ))
            {
                song = songs[index + 1];
                break;
            }
            else if (songs[index].getId().equals(currentSongId)&&(index == songs.length - 1))
            {
                song= songs[index =0];
                break;
            }

        }
        return song;
    }
    public Song getprevSong(String currentSongId)
    {
        Song song = null;
        for(int prev = 0;prev < songs.length;prev++)
        {
            if (songs[prev].getId().equals(currentSongId)&&(prev>0))
            {
                song = songs[prev - 1];
                break;
            }
            else if (songs[prev].getId().equals(currentSongId)&&( prev == 0))
            {
                song= songs[songs.length- 1];
                break;
            }
        }
        return song;
    }

}
