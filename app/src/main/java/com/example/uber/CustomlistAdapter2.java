package com.example.uber;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomlistAdapter2 extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomlistAdapter2(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.viewcart, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);
       TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        Movie m = movieItems.get(position);

        String urlimage="http://192.168.43.78/www/html/trevormoha/";
        String suburl="upload/images/932222365_1576826998.jpeg";
        String Combined=urlimage.concat(m.getThumbnailUrl());

        Picasso.get().load(Combined).error(R.mipmap.ic_launcher).into(thumbNail);


        // thumbnail image
        // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText("Rating: " + String.valueOf(m.getRating()));

        // genre
        genre.setText("price per product" +m.getPrice());

        // release year
        year.setText(String.valueOf(m.getYear()));

        return convertView;
    }

}
