package com.project.recipe.adapter;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.recipe.db.DBManager;
import com.project.recipe.models.Drink;
import com.project.recipe.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {
    private List<Drink> drinkList;
    private Context mContext;
    DBManager dbManager;
    private String imageLocalPath;
    private ProgressDialog progressDialog;
    private Drink drinkModel;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_drink_name,txt_instructions;
        public ImageView imgFav;
        public CheckBox isAlcoholic;
        public CircleImageView imageThumbnail;

        //public SimpleRatingBar comp_rating;
        // public ImageView comp_img;

        public MyViewHolder(View view) {
            super(view);
            txt_drink_name = (TextView) view.findViewById(R.id.recipeTitle);
            txt_instructions=(TextView)view.findViewById(R.id.recipeDetails);
            imageThumbnail=(CircleImageView) view.findViewById(R.id.recipe_thumbnail);
            isAlcoholic=(CheckBox)view.findViewById(R.id.checkAlcoholic);
            imgFav=(ImageView)view.findViewById(R.id.fav);
        }
    }

    public DrinkAdapter(List<Drink> drinkList, Context context) {
        this.drinkList = drinkList;
        this.mContext=context;
        dbManager=new DBManager(context);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_recipe_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Drink drink = drinkList.get(position);
        holder.txt_drink_name.setText(drink.getStrDrink());
        holder.txt_instructions.setText(drink.getStrInstructions());
        if (drink.getStrAlcoholic().equals("Alcoholic"))
            holder.isAlcoholic.setChecked(true);
        else
            holder.isAlcoholic.setChecked(false);

        Glide.with(mContext).load(drink.getStrDrinkThumb()).into(holder.imageThumbnail);
        if(dbManager.isRecipeExists(drink.getIdDrink()))
            holder.imgFav.setImageResource(R.drawable.star_filled_24px);
        else
            holder.imgFav.setImageResource(R.drawable.star_50px);
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbManager.isRecipeExists(drink.getIdDrink())) {
                    dbManager.deleteRecipe(drink.getIdDrink());
                    holder.imgFav.setImageResource(R.drawable.star_50px);
                }
                else
                {
                    drinkModel=drink;
                    new Downloading().execute(drink.getStrDrinkThumb());
                    holder.imgFav.setImageResource(R.drawable.star_filled_24px);

                }



            }
        });
  

    }

   
    @Override
    public int getItemCount() {
        return drinkList.size();
    }
    public class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
            progressDialog=new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/Recipe");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/Recipe", date + ".jpg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            progressDialog.dismiss();
            imageLocalPath=s;
            dbManager.insertRecipeData(drinkModel.getIdDrink(),drinkModel.getStrDrink(),drinkModel.getStrInstructions(),drinkModel.getStrAlcoholic(),imageLocalPath);


             Toast.makeText(mContext, "Image Saved "+s, Toast.LENGTH_SHORT).show();
        }
    }
}

