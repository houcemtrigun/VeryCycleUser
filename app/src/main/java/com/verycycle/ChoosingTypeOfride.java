package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.verycycle.adapter.AdapterCycleModel;
import com.verycycle.databinding.ActivityChoosingTypeOfrideBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.model.CycleModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosingTypeOfride extends AppCompatActivity {
    public String TAG = "ChoosingTypeOfride";
    ActivityChoosingTypeOfrideBinding binding;
    String str_image_path = "",cycleId="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    ArrayList<CycleModel.Result> arrayList;
    AdapterCycleModel adapter;
    VeryCycleUserInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choosing_type_ofride);

        SetuiUI();
    }

    private void SetuiUI() {
        arrayList = new ArrayList<>();
        
        adapter = new AdapterCycleModel(ChoosingTypeOfride.this,arrayList);
        binding.spinnerModel.setAdapter(adapter);


        binding.linerCycle.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });

        binding.ivBack.setOnClickListener(v -> { finish(); });

        binding.btnNext.setOnClickListener(v -> { validation(); });
   
        getCycleModel();


        binding.spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cycleId = arrayList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void validation() {
        if(cycleId.equals("")){
            App.showToast(ChoosingTypeOfride.this,getString(R.string.please_select_cycle_model),Toast.LENGTH_LONG);
        }
        else if(str_image_path.equals("")){
            App.showToast(ChoosingTypeOfride.this,getString(R.string.please_upload_cycle_image),Toast.LENGTH_LONG);
        }
        else {
            startActivity(new Intent(this, Problam.class).putExtra("cycleModel",cycleId)
                    .putExtra("cycleImage",str_image_path));
        }
    }

    private void getCycleModel() {
        Call<CycleModel> signupCall = apiInterface.getCycleList();
        signupCall.enqueue(new Callback<CycleModel>() {
            @Override
            public void onResponse(Call<CycleModel> call, Response<CycleModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    CycleModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Cycle Model Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                        binding.spinnerModel.setSelection(0);
                        // car_id =  carArrayList.get(0).id;


                    } else if (data.status.equals("0")) {
                        App.showToast(ChoosingTypeOfride.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CycleModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(ChoosingTypeOfride.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera () {

          /*  File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycle/Images/");

            if (!dirtostoreFile.exists()) {
                dirtostoreFile.mkdirs();
            }

            String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

            File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycle/Images/" + "IMG_" + timestr + ".jpg");

            str_image_path = tostoreFile.getPath();

            uriSavedImage = FileProvider.getUriForFile(Register.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    tostoreFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivityForResult(intent, REQUEST_CAMERA);*/



        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ChoosingTypeOfride.this,
                        "com.verycycle.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" /*+ timeStamp + "_"*/;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        str_image_path = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            binding.ivCamera.setVisibility(View.GONE);
            binding.ivDemo.setVisibility(View.GONE);
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(ChoosingTypeOfride.this, data.getData());
                Glide.with(ChoosingTypeOfride.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivCycle);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(ChoosingTypeOfride.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivCycle);
            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(ChoosingTypeOfride.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(ChoosingTypeOfride.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(ChoosingTypeOfride.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ChoosingTypeOfride.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(ChoosingTypeOfride.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(ChoosingTypeOfride.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(ChoosingTypeOfride.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ChoosingTypeOfride.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(ChoosingTypeOfride.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChoosingTypeOfride.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }





}