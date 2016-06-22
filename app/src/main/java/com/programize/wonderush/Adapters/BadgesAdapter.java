package com.programize.wonderush.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.programize.wonderush.Activities.Browsing.Badge;
import com.programize.wonderush.R;
import com.programize.wonderush.Utilities.Definitions.Definitions;

import org.json.JSONArray;
import org.json.JSONException;


public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.MainViewHolder> {
    private JSONArray mDataset;
    private Context mContext ;

    //CONSTRUCTOR
    public BadgesAdapter(Context mContext, JSONArray dataset) {
        this.mContext = mContext ;
        mDataset = dataset ;

    }

    @Override
    public BadgesAdapter.MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v ;
        MainViewHolder vh ;
        if(i%5==0)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_badges1, viewGroup, false);
            vh = new ViewHolder1(v);
        }
        else if(i%5==3)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_badges2, viewGroup, false);
            vh = new ViewHolder2(v);
        }
        else
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_badges3, viewGroup, false);
            vh = new ViewHolder2(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final BadgesAdapter.MainViewHolder viewHolder, final int i) {
        if(i % 5 == 0)
        {
            final ViewHolder1 viewHolder1 = ( ViewHolder1 ) viewHolder;
            try {
                viewHolder1.img_1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Definitions.APIdomain + mDataset.getJSONObject(i).getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_1) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder1.img_1.setImageDrawable(circularBitmapDrawable);
                    }
                });

                if(mDataset.getJSONObject(i).getBoolean("locked"))
                {
                    viewHolder1.img_1b.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.grey_transparent2).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_1b) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder1.img_1b.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    viewHolder1.img_1.setBackgroundDrawable(null);
                    viewHolder1.img_1b.setBackgroundDrawable(null);
                }
                else
                {
                    viewHolder1.img_1b.setVisibility(View.GONE);
                }

                viewHolder1.img_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, Badge.class);
                        try {
                            intent.putExtra("id", mDataset.getJSONObject(i).getString("id"));
                            intent.putExtra("image",mDataset.getJSONObject(i).getString("image") );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                });

                if(i+1<mDataset.length())
                {
                    viewHolder1.img_2.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Definitions.APIdomain + mDataset.getJSONObject(i + 1).getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_2) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder1.img_2.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    if(mDataset.getJSONObject(i + 1).getBoolean("locked"))
                    {
                        viewHolder1.img_2b.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.grey_transparent2).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_2b) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder1.img_2b.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        viewHolder1.img_2.setBackgroundDrawable(null);
                        viewHolder1.img_2b.setBackgroundDrawable(null);
                    }
                    else
                    {
                        viewHolder1.img_2b.setVisibility(View.GONE);
                    }
                    viewHolder1.img_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, Badge.class);
                            try {
                                intent.putExtra("id", mDataset.getJSONObject(i + 1).getString("id"));
                                intent.putExtra("image",mDataset.getJSONObject(i + 1).getString("image") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }
                    });
                }

                if(i+2<mDataset.length())
                {
                    viewHolder1.img_3.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Definitions.APIdomain + mDataset.getJSONObject(i + 2).getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_3) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder1.img_3.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    if(mDataset.getJSONObject(i + 2).getBoolean("locked"))
                    {
                        viewHolder1.img_3b.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.grey_transparent2).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder1.img_3b) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder1.img_3b.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        viewHolder1.img_3.setBackgroundDrawable(null);
                        viewHolder1.img_3b.setBackgroundDrawable(null);
                    }
                    else
                    {
                        viewHolder1.img_3b.setVisibility(View.GONE);
                    }

                    viewHolder1.img_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, Badge.class);
                            try {
                                intent.putExtra("id", mDataset.getJSONObject(i + 2).getString("id"));
                                intent.putExtra("image",mDataset.getJSONObject(i + 2).getString("image") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(i % 5 == 3)
        {
            final ViewHolder2 viewHolder2 = ( ViewHolder2 ) viewHolder;
            try {
                viewHolder2.img_1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Definitions.APIdomain + mDataset.getJSONObject(i).getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder2.img_1) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder2.img_1.setImageDrawable(circularBitmapDrawable);
                    }
                });

                if(mDataset.getJSONObject(i).getBoolean("locked"))
                {
                    viewHolder2.img_1b.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.grey_transparent2).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder2.img_1b) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder2.img_1b.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    viewHolder2.img_1.setBackgroundDrawable(null);
                    viewHolder2.img_1b.setBackgroundDrawable(null);
                }
                else
                {
                    viewHolder2.img_1b.setVisibility(View.GONE);
                }

                viewHolder2.img_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, Badge.class);
                        try {
                            intent.putExtra("id", mDataset.getJSONObject(i).getString("id"));
                            intent.putExtra("image",mDataset.getJSONObject(i).getString("image") );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                });

                if(i+1<mDataset.length())
                {
                    viewHolder2.img_2.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(Definitions.APIdomain + mDataset.getJSONObject(i + 1).getString("image")).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder2.img_1b) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder2.img_2.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    if(mDataset.getJSONObject(i + 1).getBoolean("locked"))
                    {
                        viewHolder2.img_2b.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(R.drawable.grey_transparent2).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder2.img_2b) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder2.img_2b.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        viewHolder2.img_2.setBackgroundDrawable(null);
                        viewHolder2.img_2b.setBackgroundDrawable(null);
                    }
                    else
                    {
                        viewHolder2.img_2b.setVisibility(View.GONE);
                    }

                    viewHolder2.img_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, Badge.class);
                            try {
                                intent.putExtra("id", mDataset.getJSONObject(i + 1).getString("id"));
                                intent.putExtra("image",mDataset.getJSONObject(i + 1).getString("image") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_1;
        public ImageView img_2;
        public ImageView img_1b;
        public ImageView img_2b;

        public MainViewHolder(View v) {
            super(v);
            img_1 = (ImageView) v.findViewById(R.id.listview_badges1_1);
            img_2 = (ImageView) v.findViewById(R.id.listview_badges1_2);
            img_1b = (ImageView) v.findViewById(R.id.listview_badges1_1b);
            img_2b = (ImageView) v.findViewById(R.id.listview_badges1_2b);

        }
    }

    public class ViewHolder1 extends MainViewHolder {
        public ImageView img_3;
        public ImageView img_3b;

        public ViewHolder1(View v) {
            super(v);
            img_3 = (ImageView) v.findViewById(R.id.listview_badges1_3);
            img_3b = (ImageView) v.findViewById(R.id.listview_badges1_3b);
        }
    }

    public class ViewHolder2 extends MainViewHolder {
        public ViewHolder2(View v) {
            super(v);
        }
    }

}
