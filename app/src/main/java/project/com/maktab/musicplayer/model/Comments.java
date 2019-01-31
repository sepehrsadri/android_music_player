package project.com.maktab.musicplayer.model;

public class Comments {

    /*
    public List<Song> getSearchedSongList(Activity activity, String searchText) {
        List<Song> songList = new ArrayList<>();

        String where = MediaStore.Audio.Media.TITLE + " LIKE ? ";
        String param = "%" + searchText + "%";
        String[] params = new String[]{param};
        Cursor cursor = activity.getContentResolver().query(uri,
                null, where, params, null);

        try {
            if (cursor.getCount() <= 0) return null;

            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String artist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));


                Bitmap bitmap = generateBitmap(activity, albumId);


                Song song = new Song();
                song.setId(id);
                song.setTitle(track);
                song.setArtist(artist);
                song.setData(data);
                song.setBitmap(bitmap);

                songList.add(song);


            }
        } finally {
            cursor.close();
        }

        return songList;
    }*/

/*    public Song getSong(Activity activity, Long id) {
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media._ID + "=" + String.valueOf(id);
        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);

        Song song;
        try {
            if (cursor.getCount() <= 0) return null;

            cursor.moveToFirst();
            String artistName = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            Long albumId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Bitmap bitmap = generateBitmap(activity, albumId);


            song = new Song();
            song.setArtist(artistName);
            song.setId(id);
            song.setTitle(track);
            song.setData(data);
            song.setDuration(duration);
            song.setBitmap(bitmap);
        } finally {
            cursor.close();
        }


        return song;
    }*/


   /* public List<Song> getSongListByArtist(Activity activity, Long artistiD) {

        List<Song> result = new ArrayList<>();
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media.ARTIST_ID + "=" + String.valueOf(artistiD);
        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);
        try {
            if (cursor.getCount() <= 0)
                return null;

            while (cursor.moveToNext()) {
                String artistName = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Bitmap bitmap = generateBitmap(activity, albumId);


                Song song = new Song();
                song.setArtist(artistName);
                song.setId(id);
                song.setTitle(track);
                song.setData(data);
                song.setDuration(duration);
                song.setBitmap(bitmap);

                result.add(song);
            }
        } finally {
            cursor.close();
        }


        return result;
    }*/


  /*  public List<Song> getSongListByAlbum(Activity activity, Long albumId) {
        List<Song> result = new ArrayList<>();
*//*        String where = MediaStore.Audio.Media.IS_MUSIC + "!= 0 " + " AND " + "cast(" +
                MediaStore.Audio.Media.ALBUM_ID + "as text) == " + String.valueOf(albumId);*//*
        String where = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " + MediaStore.Audio.Media.ALBUM_ID + "=" + String.valueOf(albumId);

        final Cursor cursor = activity.getContentResolver().query(uri, null, where, null, null);
        try {
            if (cursor.getCount() <= 0)
                return null;

            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String artist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Bitmap bitmap = generateBitmap(activity, albumId);


                Song song = new Song();
                song.setArtist(artist);
                song.setId(id);
                song.setBitmap(bitmap);
                song.setTitle(track);
                song.setData(data);
                song.setDuration(duration);
                song.setBitmap(bitmap);

                result.add(song);
            }
        } finally {
            cursor.close();
        }


        return result;
    }*/

}
