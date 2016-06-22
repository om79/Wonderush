package com.programize.wonderush.Utilities.Functions;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.programize.wonderush.Activities.Booking.Tickets;
import com.programize.wonderush.Activities.Browsing.Browse;
import com.programize.wonderush.Activities.Profile.Profile1;
import com.programize.wonderush.Activities.Settings.Settings1;
import com.programize.wonderush.Fragments.SearchFragment;
import com.programize.wonderush.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class myActions {

    public void displayToast(Context ctx,String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }


    public boolean checkInternetConnection(Context ctx)
    {
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected ;
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        String expression = "\\A([\\w+\\-].?)+@[a-z\\d\\-]+(\\.[a-z]+)*\\.[a-z]+\\z" ;
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public int getCategorySmallImage2(String category_name)
    {
        if( category_name.contains("All")) {
            return (R.drawable.categories_all);
        }
        else if(category_name.contains("Fitness"))
        {
            return(R.drawable.categories_weight);
        }
        else if(category_name.contains("Music"))
        {
            return(R.drawable.categories_nota);
        }
        else if(category_name.contains("Beauty"))
        {
            return(R.drawable.categories_kalogeros);
        }
        else if(category_name.contains("Food"))
        {
            return(R.drawable.categories_knife_fork);
        }
        else if(category_name.contains("Craft"))
        {
            return(R.drawable.categories_pallete);
        }
        else if(category_name.contains("Knowledge"))
        {
            return(R.drawable.categories_open_book);
        }
        else if(category_name.contains("Dance"))
        {
            return(R.drawable.categories_jumping_dancer);
        }
        else if(category_name.contains("Entertainment"))
        {
            return(R.drawable.categories_jumping_dancer);
        }
        else if(category_name.contains("Health"))
        {
            return(R.drawable.categories_heart_beat);
        }
        else if(category_name.contains("Sports"))
        {
            return(R.drawable.categories_weight);
        }
        else
        {
            return(R.drawable.categories_entertainment);
        }
    }


    public int getCategoryLargeImage2(String category_name)
    {
        if( category_name.equals("All")) {
            return (R.drawable.categories_all);
        }
        else if(category_name.contains("Fitness"))
        {
            return(R.drawable.categories_fitness_and_wellbeing_new);
        }
        else if(category_name.equals("Music"))
        {
            return(R.drawable.categories_music);
        }
        else if(category_name.contains("Beauty"))
        {
            return(R.drawable.categories_beauty);
        }
        else if(category_name.contains("Food"))
        {
            return(R.drawable.categories_food_drink);
        }
        else if(category_name.equals("Arts & Crafts"))
        {
            return(R.drawable.categories_arts_crafts);
        }
        else if(category_name.contains("Knowledge"))
        {
            return(R.drawable.categories_knowledge);
        }
        else if(category_name.equals("Dance"))
        {
            return(R.drawable.categories_dance);
        }
        else if(category_name.equals("Entertainment"))
        {
            return(R.drawable.categories_entertainment);
        }
        else if(category_name.equals("Music & Dance"))
        {
            return(R.drawable.categories_music_dance);
        }
        else if(category_name.equals("Sports & Fitness"))
        {
            return(R.drawable.categories_sports_and_fitness);
        }
        else if(category_name.contains("Health"))
        {
            return(R.drawable.categories_health);
        }
        else
        {
            return(R.drawable.categories_entertainment);
        }
    }

    public int getCategoryPinkImage(String category_name) {
        if (category_name.contains("All")) {
            return (R.drawable.categories_all);
        } else if (category_name.contains("Fitness")) {
            return (R.drawable.pink_weight);
        } else if (category_name.contains("Music")) {
            return (R.drawable.pink_music);
        } else if (category_name.contains("Beauty")) {
            return (R.drawable.pink_clothes);
        } else if (category_name.contains("Food")) {
            return (R.drawable.pink_food);
        } else if (category_name.contains("Craft")) {
            return (R.drawable.pink_pallete);
        } else if (category_name.contains("Knowledge")) {
            return (R.drawable.pink_book);
        } else if (category_name.contains("Dance")) {
            return (R.drawable.pink_human_jump);
        } else if (category_name.contains("Entertainment")) {
            return (R.drawable.pink_tickets);
        } else if (category_name.contains("Sports")) {
            return (R.drawable.pink_weight);
        } else if (category_name.contains("Health")) {
            return (R.drawable.pink_heart);
        } else {
            return (R.drawable.pink_tickets);
        }
    }

    public String getDays(JSONArray days_array)
    {
        String days_string = "";
        int days_int = 0 ;
        for(int i = 0; i<days_array.length();i++)
        {
            try {
                days_int = days_array.getInt(i) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (days_int)
            {
                case 0 :
                    days_string = days_string + ", SUN";
                    break ;
                case 1 :
                    days_string = days_string + ", MON";
                    break ;
                case 2 :
                    days_string = days_string + ", TUE";
                    break ;
                case 3 :
                    days_string = days_string + ", WED";
                    break ;
                case 4 :
                    days_string = days_string + ", THU";
                    break ;
                case 5 :
                    days_string = days_string + ", FRI";
                    break ;
                case 6 :
                    days_string = days_string + ", SAT";
                    break ;
            }
        }
        if(days_array.length()!=0)
        {
            days_string = days_string.substring(2);
        }
        else
        {
            days_string = "" ;
        }


        return days_string ;
    }

    public String getMonthString(int month)
    {
        switch (month)
        {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return " ";
        }
    }

    /**
     * Decodes image and scales it to reduce memory consumption
     *
     * @param f The image-file to decode
     * @return The decoded and scaled image
     */
    public Bitmap decodeFile(File f) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=250;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public FloatingActionMenu set_up_menu(final Context context)
    {
        // Set up the large red button on the center right side
        // With custom button and content sizes and margins
        int redActionButtonSize = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = context.getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = context.getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = context.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = context.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final ImageView fabIconStar = new ImageView(context);
        fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_icon, null));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(context)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(context);
        lCSubBuilder.setBackgroundDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_action_red_selector, null));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        final ImageView lcIcon1 = new ImageView(context);
        ImageView lcIcon2 = new ImageView(context);
        ImageView lcIcon3 = new ImageView(context);
        ImageView lcIcon4 = new ImageView(context);
        ImageView lcIcon5 = new ImageView(context);

        lcIcon1.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_option1, null));
        lcIcon2.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_option2, null));
        lcIcon3.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_option3, null));
        lcIcon4.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_option4, null));
        lcIcon5.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_option5, null));

        // Build another menu with custom options
        final FloatingActionMenu leftCenterMenu = new FloatingActionMenu.Builder(context)
                .addSubActionView(lCSubBuilder.setContentView(lcIcon5, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon4, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon3, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon2, blueContentParams).build())
                .addSubActionView(lCSubBuilder.setContentView(lcIcon1, blueContentParams).build())
                .setRadius(redActionMenuRadius)
                .setStartAngle(10)
                .setEndAngle(-100)
                .attachTo(leftCenterButton)
                .build();



        // Listen menu open and close events to animate the button content view
        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_close, null));
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconStar.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.menu_icon, null));
            }
        });


        lcIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftCenterMenu.close(true);
                leftCenterMenu.removeViewFromCurrentContainer(leftCenterButton);
                Intent new_intent = new Intent(context, Profile1.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(new_intent);
            }
        });

        lcIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftCenterMenu.close(true);
                leftCenterMenu.removeViewFromCurrentContainer(leftCenterButton);
                Intent new_intent = new Intent(context, Browse.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(new_intent);
            }
        });

        lcIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftCenterMenu.close(true);
                leftCenterMenu.removeViewFromCurrentContainer(leftCenterButton);
                Intent new_intent = new Intent(context, Tickets.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(new_intent);
            }
        });

        lcIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftCenterMenu.close(true);
                leftCenterMenu.removeViewFromCurrentContainer(leftCenterButton);
                Intent new_intent = new Intent(context, Settings1.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(new_intent);
            }
        });

        lcIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment dialogFragment = SearchFragment.newInstance();
                dialogFragment.show(((Activity)context).getFragmentManager(), "Sample Fragment");
            }
        });

        return leftCenterMenu;

    }
}
