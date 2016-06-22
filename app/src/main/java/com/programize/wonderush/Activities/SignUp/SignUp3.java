package com.programize.wonderush.Activities.SignUp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.Activities.Profile.Profile1;
import com.programize.wonderush.Activities.Settings.Billing1;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;
import com.programize.wonderush.Utilities.Functions.MyProgressDialog;
import com.programize.wonderush.Utilities.Functions.myActions;
import com.programize.wonderush.Utilities.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp3 extends Activity {

    private String TAG = "TAG - SIGNUP3 SCREEN - " ;
    private myActions mActions = new myActions();
    private Bundle extras ;
    private SharedPreferences prefs ;
    private ProgressDialog progressDialog ;

    private String name ;
    private String email ;
    private String password ;

    private TextView txt1 ;
    private TextView txt2 ;
    private ImageView avatar ;
    private Button button_continue ;
    private TextView txt_skip;

    int camera_id = 100;
    int gallery_id = 150;
    int permission_id_external = 500 ;
    int permission_id_camera = 600 ;

    private Bitmap avatar_bitmap = null;
    private String avatar_encoded = "" ;

    private Uri imageUri;
    private ContentValues values ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3_new);


        // Request permission WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(SignUp3.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUp3.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    permission_id_external);
        }



        progressDialog = MyProgressDialog.ctor(SignUp3.this);

        //TAKE VALUES FROM PREVIOUS SCREEN
        extras = getIntent().getExtras();
        name = extras.getString("name");
        email = extras.getString("email");
        password = extras.getString("password");



        initialize_widgets();

        set_up_font();

        set_up_listeners();


        if(getIntent().hasExtra("from_profile"))
        {
            if(extras.getString("image").equals(""))
            {
                Glide.with(this).load(R.drawable.male_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            else
            {
                Glide.with(this).load(Definitions.APIdomain + extras.getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }

            txt_skip.setVisibility(View.GONE);

        }
        else
        {
            Glide.with(this).load(R.drawable.icon_photograph).override(150, 150).into(avatar);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == camera_id && resultCode == RESULT_OK) {

            Bitmap decodedBitmap = (Bitmap) data.getExtras().get("data");
            avatar.setImageBitmap(decodedBitmap);

            //Convert image to base64 format
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //decodedBitmap is the bitmap object
            }
            catch (NullPointerException np) {
                decodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //decodedBitmap is the bitmap object
            }
            catch(Exception e){
//                Log.v("Exception caught - realPath:", realPath);
            }

            byte[] byteArrayImage = baos.toByteArray();
            avatar_encoded = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            avatar_bitmap = decodedBitmap;


        }
        //TAKE IMAGE FROM GALLERY
        else if (requestCode == gallery_id && resultCode == RESULT_OK)
        {
            Uri selectedImageUri = data.getData();

            String realPath = getRealPathFromURI(selectedImageUri);

            File imageFile = new File(realPath);
            Bitmap decodedBitmap = mActions.decodeFile(imageFile);

            if(decodedBitmap == null)
            {
                mActions.displayToast(SignUp3.this, "Could not fetch image from gallery");
            }
            else {
                //----FIX IMAGE ORIENTATION
                try {
                    ExifInterface exif = new ExifInterface(realPath);
                    String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    if (orientation.equals("3")) {//horizontal flipped
                        Log.v("Flip", "180 degrees");
                        Matrix matrix = new Matrix();
                        //matrix.postRotate(180);
                        matrix.preRotate(180);
                        decodedBitmap = Bitmap.createBitmap(decodedBitmap, 0, 0, decodedBitmap.getWidth(), decodedBitmap.getHeight(), matrix, true);
                        BitmapDrawable bmd = new BitmapDrawable(this.getResources(), decodedBitmap);
                        avatar.setImageDrawable(bmd);
                    } else if (orientation.equals("6")) {//horizontal normal
                        Log.v("Flip", "90 degrees");
                        Matrix matrix = new Matrix();
                        matrix.preRotate(90);
                        decodedBitmap = Bitmap.createBitmap(decodedBitmap, 0, 0, decodedBitmap.getWidth(), decodedBitmap.getHeight(), matrix, true);
                        BitmapDrawable bmd = new BitmapDrawable(this.getResources(), decodedBitmap);
                        avatar.setImageDrawable(bmd);
                    } else if (orientation.equals("8")) {//vertical normal
                        Log.v("Flip", "270 degrees");
                        Matrix matrix = new Matrix();
                        matrix.preRotate(270);
                        decodedBitmap = Bitmap.createBitmap(decodedBitmap, 0, 0, decodedBitmap.getWidth(), decodedBitmap.getHeight(), matrix, true);
                        BitmapDrawable bmd = new BitmapDrawable(this.getResources(), decodedBitmap);
                        avatar.setImageDrawable(bmd);
                    } else {
                        avatar.setImageBitmap(decodedBitmap);
                    }
                } catch (IOException e) {
                    Log.v("Exception", "...in getting Orientation");
                    e.printStackTrace();
                }
            }

            //Convert image to base64 format
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //decodedBitmap is the bitmap object
            }
            catch (NullPointerException np) {
                decodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //decodedBitmap is the bitmap object
            }
            catch(Exception e){
                Log.v("Exception - realPath:", realPath);
            }

            byte[] byteArrayImage = baos.toByteArray();
            avatar_encoded = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            avatar_bitmap = decodedBitmap;
        }
    }

    public String getRealPathFromURI(Uri ImageUri) {
//        String[] projection = { MediaStore.MediaColumns.DATA };
//        Cursor cursor = managedQuery(ImageUri, projection, null, null,
//                null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        cursor.moveToFirst();

        Cursor cursor = getContentResolver().query(ImageUri, null, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        return cursor.getString(column_index);
    }

    public String getRealPathFromURI2(Uri ImageUri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(ImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp3.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
//                    values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
//                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                    imageUri = getContentResolver().insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent, camera_id);
                    // Request permission WRITE_EXTERNAL_STORAGE
                    if (ContextCompat.checkSelfPermission(SignUp3.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SignUp3.this,
                                new String[]{Manifest.permission.CAMERA},
                                permission_id_camera);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            gallery_id);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void initialize_widgets()
    {

        //SET UP SHARED PREFERENCES
        prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);

        //INITIALIZE WIDGETS
        txt1 = (TextView)findViewById(R.id.signup4screen_textview_text1);
        txt2 = (TextView)findViewById(R.id.signup4screen_textview_text2);
        avatar = (ImageView)findViewById(R.id.signup3screen_imageview_avatar);
        button_continue = (Button)findViewById(R.id.signup3screen_button_continue);
        txt_skip = (TextView)findViewById(R.id.signup3bscreen_text_skip);
    }

    private void set_up_font()
    {
        //SETTING UP EXTERNAL FONT
        String fontPath = "fonts/ProximaNova-Reg.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);
        txt2.setTypeface(tf);
        button_continue.setTypeface(tf);
        txt_skip.setTypeface(tf);


        txt1.setText(Html.fromHtml("Hey, you should take"));
        txt2.setText(Html.fromHtml("a <b>selfie</b> for your profile!"));
    }

    private void set_up_listeners()
    {
        //AVATAR CLICK LISTENER
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUp3.this, "Please select Image", Toast.LENGTH_SHORT).show();
                selectImage();
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignUp3.this, SignUp4.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("name", name);
//                intent.putExtra("email", email);
//                intent.putExtra("password", password);
//                startActivity(intent);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
                if (getIntent().hasExtra("from_profile")) {
                    updateUser();
                } else
                {
                    registerUser();
                }
            }
        });

        //CONTINUE BUTTON CLICK LISTENER
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("from_profile")) {
                    updateUser();
                } else {
                    if (avatar_encoded.length()!=0) {
                        registerUser();
                    } else {
                        Toast.makeText(SignUp3.this, "Image is NOT selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateUser()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Definitions.sharedprefname, MODE_PRIVATE);
        if(!mActions.checkInternetConnection(SignUp3.this))
        {
            mActions.displayToast(SignUp3.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put("Content-Type", "application/json");
            headers.put( "X-User-Email",  prefs.getString("email", null) );
            headers.put("X-User-Token", prefs.getString("token", null));

            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();
            try {
                user.put("firstname", name);
                if(getIntent().hasExtra("password"))
                {
                    user.put("password", extras.getString("password"));
                    user.put("password_confirmation", extras.getString("confirm_password"));
                }
                if(avatar_encoded.length()!=0)
                {
                    user.put("user_image_data", avatar_encoded);
                }
                request.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            UserAPI.put_StringResp("members/" + extras.getString("id"), request, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            Intent intent = new Intent(SignUp3.this, Profile1.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            mActions.displayToast(SignUp3.this, "Error Occurred");
//                            NetworkResponse response = error.networkResponse;
//                            try {
//                                JSONObject jobj = new JSONObject(new String(response.data));
//                                if (jobj.has("email")) {
//                                    mActions.displayToast(SignUp3.this, "Email already exists");
//                                }
//                                else if (jobj.has("password_confirmation")) {
//                                    mActions.displayToast(SignUp3.this, "confirmations error");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
        }
    }

    private void registerUser()
    {
        if(!mActions.checkInternetConnection(SignUp3.this))
        {
            mActions.displayToast(SignUp3.this, "No Internet Connection");
        }
        else
        {
            progressDialog.show();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", "application/json" );
            headers.put("Content-Type", "application/json");

            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();
            try {
                user.put("firstname", name);
                user.put("email", email);
                user.put("password", password);
                if(avatar_encoded.length()!=0)
                {
                    user.put("user_image_data", avatar_encoded);
                }
                request.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v(TAG, request.toString());

            UserAPI.post_JsonResp("members/sign_up_step_1", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
//                            mActions.displayToast(SignUp4.this, "Register Finished");
//                            Log.v(TAG + "GET REGISTER RESPONSE ", response.toString());

                            store_values(response);

                            Intent intent = new Intent(SignUp3.this, Billing1.class);
                            intent.putExtra("from_sign_up", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                            mActions.displayToast(SignUp3.this, "Error Occurred");
                            NetworkResponse response = error.networkResponse;
                            try {
                                JSONObject jobj = new JSONObject(new String(response.data));
                                if (jobj.has("email")) {
                                    mActions.displayToast(SignUp3.this, "Email already exists !");
                                }
                                else
                                {
                                    mActions.displayToast(SignUp3.this, "Error Occurred");
                                    mActions.displayToast(SignUp3.this, jobj.toString());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void store_values(JSONObject response)
    {
        try {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", response.getString("authentication_token"));
            editor.putString("email", response.getString("email"));
            editor.putInt("id", response.getInt("id"));
            if(avatar_encoded.length()!=0)
            {
                editor.putString("avatar", response.getString("user_image"));
            }
            editor.putString("firstname",response.getString("firstname") );
            editor.putString("last_login", response.getString("created_at"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 500: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                } else {
                    if (getIntent().hasExtra("from_profile")) {
                        updateUser();
                    } else
                    {
                        registerUser();
                    }
                }
                return;
            }
            case 600: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(i, camera_id);

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
