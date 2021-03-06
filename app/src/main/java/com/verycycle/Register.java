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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.verycycle.databinding.ActivityRegisterBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.model.SignupModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import static com.verycycle.retrofit.Constant.emailPattern;

public class Register extends AppCompatActivity {
    public String TAG = "Register";
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;


    ActivityRegisterBinding binding;
    VeryCycleUserInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        SetupUI();
    }

    private void SetupUI() {
        binding.ccp.setCountryForPhoneCode(33);

        binding.regLi.setOnClickListener(v -> {
            validation();
        });

        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        binding.ivLogo.setOnClickListener(v ->{
            if(checkPermisssionForReadStorage ())
                showImageSelection();
        });
    }

    private void validation() {
        if (binding.etFname.getText().toString().equals("")) {
            binding.etFname.setError(getString(R.string.please_enter_name));
            binding.etFname.setFocusable(true);
        } else if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        } else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.please_enter_mobile_number));
            binding.etMobile.setFocusable(true);
        } else if (binding.etPassword.getText().toString().equals("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            binding.etPassword.setFocusable(true);
        } else {
            if (NetworkReceiver.isConnected()) {
                // registerUser();
                updateProfile();
            } else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        }
    }

        public void showImageSelection () {

            final Dialog dialog = new Dialog(Register.this);
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


        private void getPhotoFromGallary () {
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
                    Uri photoURI = FileProvider.getUriForFile(Register.this,
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
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Log.e("Result_code", requestCode + "");
                if (requestCode == SELECT_FILE) {
                    str_image_path = DataManager.getInstance().getRealPathFromURI(Register.this, data.getData());
                    Glide.with(Register.this)
                            .load(str_image_path)
                            .centerCrop()
                            .into(binding.ivLogo);

                } else if (requestCode == REQUEST_CAMERA) {
                    Glide.with(Register.this)
                            .load(str_image_path)
                            .centerCrop()
                            .into(binding.ivLogo);
                }

            }
        }


        //CHECKING FOR Camera STATUS
        public boolean checkPermisssionForReadStorage () {
            if (ContextCompat.checkSelfPermission(Register.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED

                    ||

                    ContextCompat.checkSelfPermission(Register.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    ||

                    ContextCompat.checkSelfPermission(Register.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
            ) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this,
                        Manifest.permission.CAMERA)

                        ||

                        ActivityCompat.shouldShowRequestPermissionRationale(Register.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(Register.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)


                ) {


                    ActivityCompat.requestPermissions(Register.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_CONSTANT);

                } else {

                    //explain("Please Allow Location Permission");
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Register.this,
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
        public void onRequestPermissionsResult ( int requestCode, String permissions[],
        int[] grantResults){
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
                            Toast.makeText(Register.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                    // return;
                }


            }
        }


        private void updateProfile () {
            DataManager.getInstance().showProgressMessage(Register.this, getString(R.string.please_wait));
            MultipartBody.Part filePart;
            if (!str_image_path.equalsIgnoreCase("")) {
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            } else {
                RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
                filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
            }
            RequestBody f_name = RequestBody.create(MediaType.parse("text/plain"), binding.etFname.getText().toString());
          //  RequestBody l_name = RequestBody.create(MediaType.parse("text/plain"), binding.etLname.getText().toString());
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString());
            RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), binding.etMobile.getText().toString());
            RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.getSelectedCountryCode());
            RequestBody password = RequestBody.create(MediaType.parse("text/plain"), binding.etPassword.getText().toString());
            RequestBody register_id = RequestBody.create(MediaType.parse("text/plain"), FirebaseInstanceId.getInstance().getToken());
            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "USER");

            Call<SignupModel> signupCall = apiInterface.signupUser(f_name, email, mobile,countryCode, password, register_id, type, filePart);
            signupCall.enqueue(new Callback<SignupModel>() {
                @Override
                public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        SignupModel data = response.body();
                        if (data.status.equals("1")) {
                            String dataResponse = new Gson().toJson(response.body());
                            Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                            //  SessionManager.writeString(Register.this, Constant.USER_INFO, dataResponse);
                            App.showToast(Register.this, getString(R.string.thank_you_for_signing_up), Toast.LENGTH_SHORT); //Toast.makeText(LoginAct.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();

                        } else if (data.status.equals("0")) {
                            App.showToast(Register.this, data.message, Toast.LENGTH_SHORT);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SignupModel> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }

            });
        }



        /*public void registerUser(){
        DataManager.getInstance().showProgressMessage(Register.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("username", binding.etFname.getText().toString());
        map.put("email", binding.etEmail.getText().toString());
        map.put("mobile", binding.etEmail.getText().toString());
        map.put("country_code", binding.ccp.getSelectedCountryCode());
        map.put("password", binding.etPassword.getText().toString());
        map.put("register_id", FirebaseInstanceId.getInstance().getToken());
        map.put("type", "User");
        Log.e(TAG, "Login Request " + map);
        Call<SignupModel> loginCall = apiInterface.signupUser(map);
        loginCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        //  SessionManager.writeString(Register.this, Constant.USER_INFO, responseString);
                        Toast.makeText(Register.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT); //Toast.makeText(LoginAct.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.status.equals("0")) {
                        App.showToast(Register.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }*/





    }








