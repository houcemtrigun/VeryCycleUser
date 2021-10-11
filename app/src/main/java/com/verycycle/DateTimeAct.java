package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import com.verycycle.adapter.AdapterTimeSlot;
import com.verycycle.databinding.ActivityDateTimeBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.model.DateTimeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateTimeAct extends AppCompatActivity {
    ActivityDateTimeBinding binding;
    public static String date = "", time = "";
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String str_image_path = "", cycleId = "", problem = "", repair_image_path = "", address = "", type = "Normal repair", convrtDate = "";
    ;
    AdapterTimeSlot adapter;
    ArrayList<DateTimeModel.Result> arrayList;
    boolean first910 = false, first1011 = false, first1112 = false, first1201 = false, first0102 = false, first0203 = false, first0304 = false, first0405 = false, first0506 = false, first0607 = false, first0708 = false;

    boolean second910 = false, second1011 = false, second1112 = false, second1201 = false, second0102 = false, second0203 = false, second0304 = false, second0405 = false, second0506 = false, second0607 = false, second0708 = false;


    boolean three910 = false, three1011 = false, three1112 = false, three1201 = false, three0102 = false, three0203 = false, three0304 = false, three0405 = false, three0506 = false, three0607 = false, three0708 = false;

    int count = 1;
    String currentDate[];

    ArrayList<String> timeSlotList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date_time);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        timeSlotList = new ArrayList<>();
        if (!Places.isInitialized()) {
            Places.initialize(DateTimeAct.this, getString(R.string.place_api_key));
        }

        if (getIntent() != null) {
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
            problem = getIntent().getStringExtra("problem");
            repair_image_path = getIntent().getStringExtra("repairImage");
        }

        currentDate = DataManager.getCurrent1().split(" ");
        int i1, i2;
        i1 = Integer.parseInt(currentDate[1]) + 1;
        i2 = Integer.parseInt(currentDate[1]) + 2;
        binding.FirsttvDate.setText(DataManager.getCurrent1());
        binding.SecondtvDate.setText(currentDate[0] + " " + i1 + " " + currentDate[2]);
        binding.ThreetvDate.setText(currentDate[0] + " " + i2 + " " + currentDate[2]);

     //   translateTextToFrench(DataManager.getCurrent1(),1);
    //    translateTextToFrench(currentDate[0] + " " + i1 + " " + currentDate[2],2);
    //    translateTextToFrench(currentDate[0] + " " + i2 + " " + currentDate[2],3);




        binding.Firsttv910.setOnClickListener(v -> {
            if (binding.Firsttv910.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first910 == false) {
                        binding.Firsttv910.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv910.setTextColor(getResources().getColor(R.color.white));
                        first910 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv910.getText().toString());

                    } else {
                        binding.Firsttv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv910.setTextColor(getResources().getColor(R.color.black));
                        first910 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv910.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }

        });
        binding.Firsttv1011.setOnClickListener(v -> {
            if (binding.Firsttv1011.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first1011 == false) {
                        binding.Firsttv1011.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv1011.setTextColor(getResources().getColor(R.color.white));
                        first1011 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1011.getText().toString());

                    } else {
                        binding.Firsttv1011.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv1011.setTextColor(getResources().getColor(R.color.black));
                        first1011 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1011.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }

        });
        binding.Firsttv1112.setOnClickListener(v -> {
            if (binding.Firsttv1112.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first1112 == false) {
                        binding.Firsttv1112.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv1112.setTextColor(getResources().getColor(R.color.white));
                        first1112 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1112.getText().toString());

                    } else {
                        binding.Firsttv1112.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv1112.setTextColor(getResources().getColor(R.color.black));
                        first1112 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1112.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }

        });
        binding.Firsttv1201.setOnClickListener(v -> {
            if (binding.Firsttv1201.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first1201 == false) {
                        binding.Firsttv1201.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv1201.setTextColor(getResources().getColor(R.color.white));
                        first1201 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1201.getText().toString());

                    } else {
                        binding.Firsttv1201.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv1201.setTextColor(getResources().getColor(R.color.black));
                        first1201 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv1201.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0102.setOnClickListener(v -> {
            if (binding.Firsttv0102.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0102 == false) {
                        binding.Firsttv0102.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0102.setTextColor(getResources().getColor(R.color.white));
                        first0102 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0102.getText().toString());
                    } else {
                        binding.Firsttv0102.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0102.setTextColor(getResources().getColor(R.color.black));
                        first0102 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0102.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0203.setOnClickListener(v -> {
            if (binding.Firsttv0203.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0203 == false) {
                        binding.Firsttv0203.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0203.setTextColor(getResources().getColor(R.color.white));
                        first0203 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0203.getText().toString());

                    } else {
                        binding.Firsttv0203.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0203.setTextColor(getResources().getColor(R.color.black));
                        first0203 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0203.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0304.setOnClickListener(v -> {
            if (binding.Firsttv0304.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0304 == false) {
                        binding.Firsttv0304.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0304.setTextColor(getResources().getColor(R.color.white));
                        first0304 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0304.getText().toString());

                    } else {
                        binding.Firsttv0304.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0304.setTextColor(getResources().getColor(R.color.black));
                        first0304 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0304.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0405.setOnClickListener(v -> {
            if (binding.Firsttv0405.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0405 == false) {
                        binding.Firsttv0405.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0405.setTextColor(getResources().getColor(R.color.white));
                        first0405 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0405.getText().toString());

                    } else {
                        binding.Firsttv0405.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0405.setTextColor(getResources().getColor(R.color.black));
                        first0405 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0405.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0506.setOnClickListener(v -> {
            if (binding.Firsttv0506.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0506 == false) {
                        binding.Firsttv0506.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0506.setTextColor(getResources().getColor(R.color.white));
                        first0506 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0506.getText().toString());

                    } else {
                        binding.Firsttv0506.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0506.setTextColor(getResources().getColor(R.color.black));
                        first0506 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0506.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0607.setOnClickListener(v -> {
            if (binding.Firsttv0607.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0607 == false) {
                        binding.Firsttv0607.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0607.setTextColor(getResources().getColor(R.color.white));
                        first0607 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0607.getText().toString());

                    } else {
                        binding.Firsttv0607.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0607.setTextColor(getResources().getColor(R.color.black));
                        first0607 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0607.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Firsttv0708.setOnClickListener(v -> {
            if (binding.Firsttv0708.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (first0708 == false) {
                        binding.Firsttv0708.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Firsttv0708.setTextColor(getResources().getColor(R.color.white));
                        first0708 = true;
                        count++;
                        AddRemoveTime("add", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0708.getText().toString());

                    } else {
                        binding.Firsttv0708.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Firsttv0708.setTextColor(getResources().getColor(R.color.black));
                        first0708 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.FirsttvDate.getText().toString() + " " + binding.Firsttv0708.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });


        binding.Secondtv910.setOnClickListener(v -> {
            if (binding.Secondtv910.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second910 == false) {
                        binding.Secondtv910.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv910.setTextColor(getResources().getColor(R.color.white));
                        second910 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv910.getText().toString());

                    } else {
                        binding.Secondtv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv910.setTextColor(getResources().getColor(R.color.black));
                        second910 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv910.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv1011.setOnClickListener(v -> {
            if (binding.Firsttv1011.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second1011 == false) {
                        binding.Secondtv1011.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv1011.setTextColor(getResources().getColor(R.color.white));
                        second1011 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1011.getText().toString());

                    } else {
                        binding.Secondtv1011.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv1011.setTextColor(getResources().getColor(R.color.black));
                        second1011 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1011.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv1112.setOnClickListener(v -> {
            if (binding.Secondtv1112.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second1112 == false) {
                        binding.Secondtv1112.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv1112.setTextColor(getResources().getColor(R.color.white));
                        second1112 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1112.getText().toString());

                    } else {
                        binding.Secondtv1112.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv1112.setTextColor(getResources().getColor(R.color.black));
                        second1112 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1112.getText().toString());

                        }
                    }

                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }
        });
        binding.Secondtv1201.setOnClickListener(v -> {
            if (binding.Secondtv1201.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second1201 == false) {
                        binding.Secondtv1201.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv1201.setTextColor(getResources().getColor(R.color.white));
                        second1201 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1201.getText().toString());

                    } else {
                        binding.Secondtv1201.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv1201.setTextColor(getResources().getColor(R.color.black));
                        second1201 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv1201.getText().toString());
                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0102.setOnClickListener(v -> {
            if (binding.Secondtv0102.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0102 == false) {
                        binding.Secondtv0102.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0102.setTextColor(getResources().getColor(R.color.white));
                        second0102 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0102.getText().toString());
                    } else {
                        binding.Secondtv0102.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0102.setTextColor(getResources().getColor(R.color.black));
                        second0102 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0102.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0203.setOnClickListener(v -> {
            if (binding.Secondtv0203.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0203 == false) {
                        binding.Secondtv0203.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0203.setTextColor(getResources().getColor(R.color.white));
                        second0203 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0203.getText().toString());

                    } else {
                        binding.Secondtv0203.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0203.setTextColor(getResources().getColor(R.color.black));
                        second0203 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0203.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0304.setOnClickListener(v -> {
            if (binding.Secondtv0304.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0304 == false) {
                        binding.Secondtv0304.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0304.setTextColor(getResources().getColor(R.color.white));
                        second0304 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0304.getText().toString());

                    } else {
                        binding.Secondtv0304.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0304.setTextColor(getResources().getColor(R.color.black));
                        second0304 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0304.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0405.setOnClickListener(v -> {
            if (binding.Secondtv0405.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0405 == false) {
                        binding.Secondtv0405.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0405.setTextColor(getResources().getColor(R.color.white));
                        second0405 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0405.getText().toString());

                    } else {
                        binding.Secondtv0405.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0405.setTextColor(getResources().getColor(R.color.black));
                        second0405 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0405.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0506.setOnClickListener(v -> {
            if (binding.Secondtv0506.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0506 == false) {
                        binding.Secondtv0506.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0506.setTextColor(getResources().getColor(R.color.white));
                        second0506 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0607.getText().toString());

                    } else {
                        binding.Secondtv0506.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0506.setTextColor(getResources().getColor(R.color.black));
                        second0506 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0506.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0607.setOnClickListener(v -> {
            if (binding.Secondtv0607.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0607 == false) {
                        binding.Secondtv0607.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0607.setTextColor(getResources().getColor(R.color.white));
                        second0607 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0607.getText().toString());

                    } else {
                        binding.Secondtv0607.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0607.setTextColor(getResources().getColor(R.color.black));
                        second0607 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0607.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });
        binding.Secondtv0708.setOnClickListener(v -> {
            if (binding.Secondtv0708.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (second0708 == false) {
                        binding.Secondtv0708.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Secondtv0708.setTextColor(getResources().getColor(R.color.white));
                        second0708 = true;
                        count++;
                        AddRemoveTime("add", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0708.getText().toString());

                    } else {
                        binding.Secondtv0708.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Secondtv0708.setTextColor(getResources().getColor(R.color.black));
                        second0708 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.SecondtvDate.getText().toString() + " " + binding.Secondtv0708.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }

        });


        binding.Threetv910.setOnClickListener(v -> {
            if (binding.Threetv910.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three910 == false) {
                        binding.Threetv910.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv910.setTextColor(getResources().getColor(R.color.white));
                        three910 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv910.getText().toString());


                    } else {
                        binding.Threetv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv910.setTextColor(getResources().getColor(R.color.black));
                        three910 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv910.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }

            }
            Log.e("SelectedTab====", count + "");
        });
        binding.Threetv1011.setOnClickListener(v -> {
            if (binding.Threetv1011.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three1011 == false) {
                        binding.Threetv1011.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv1011.setTextColor(getResources().getColor(R.color.white));
                        three1011 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1011.getText().toString());

                    } else {
                        binding.Threetv1011.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv1011.setTextColor(getResources().getColor(R.color.black));
                        three1011 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1011.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }

            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv1112.setOnClickListener(v -> {
            if (binding.Threetv1112.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three1112 == false) {
                        binding.Threetv1112.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv1112.setTextColor(getResources().getColor(R.color.white));
                        three1112 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1112.getText().toString());
                    } else {
                        binding.Threetv1112.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv1112.setTextColor(getResources().getColor(R.color.black));
                        three1112 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1112.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv1201.setOnClickListener(v -> {
            if (binding.Threetv1201.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three1201 == false) {
                        binding.Threetv1201.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv1201.setTextColor(getResources().getColor(R.color.white));
                        three1201 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1201.getText().toString());

                    } else {
                        binding.Threetv1201.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv1201.setTextColor(getResources().getColor(R.color.black));
                        three1201 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv1201.getText().toString());
                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0102.setOnClickListener(v -> {
            if (binding.Threetv0102.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0102 == false) {
                        binding.Threetv0102.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0102.setTextColor(getResources().getColor(R.color.white));
                        three0102 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0102.getText().toString());

                    } else {
                        binding.Threetv0102.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0102.setTextColor(getResources().getColor(R.color.black));
                        three0102 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0102.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0203.setOnClickListener(v -> {
            if (binding.Threetv0203.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0203 == false) {
                        binding.Threetv0203.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0203.setTextColor(getResources().getColor(R.color.white));
                        three0203 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0203.getText().toString());

                    } else {
                        binding.Threetv0203.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0203.setTextColor(getResources().getColor(R.color.black));
                        three0203 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0203.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }

            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0304.setOnClickListener(v -> {
            if (binding.Threetv0304.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0304 == false) {
                        binding.Threetv0304.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0304.setTextColor(getResources().getColor(R.color.white));
                        three0304 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0304.getText().toString());

                    } else {
                        binding.Threetv0304.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0304.setTextColor(getResources().getColor(R.color.black));
                        three0304 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0304.getText().toString());

                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0405.setOnClickListener(v -> {
            if (binding.Threetv0405.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0405 == false) {
                        binding.Threetv0405.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0405.setTextColor(getResources().getColor(R.color.white));
                        three0405 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0405.getText().toString());

                    } else {
                        binding.Threetv0405.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0405.setTextColor(getResources().getColor(R.color.black));
                        three0405 = false;
                        if (count > 1) {
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0405.getText().toString());
                            count--;
                        }

                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0506.setOnClickListener(v -> {
            if (binding.Threetv0506.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0506 == false) {
                        binding.Threetv0506.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0506.setTextColor(getResources().getColor(R.color.white));
                        three0506 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0506.getText().toString());
                    } else {
                        binding.Threetv0506.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0506.setTextColor(getResources().getColor(R.color.black));
                        three0506 = false;
                        if (count > 1) {
                            count--;
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0506.getText().toString());
                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0607.setOnClickListener(v -> {
            if (binding.Threetv0607.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0607 == false) {
                        binding.Threetv0607.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0607.setTextColor(getResources().getColor(R.color.white));
                        three0607 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0607.getText().toString());
                    } else {
                        binding.Threetv0607.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0607.setTextColor(getResources().getColor(R.color.black));
                        three0607 = false;
                        if (count > 1) {
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0607.getText().toString());
                            count--;
                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);

                }
            }
            Log.e("SelectedTab====", count + "");

        });
        binding.Threetv0708.setOnClickListener(v -> {
            if (binding.Threetv0708.getText().toString().equals("full")) {

            } else {
                if (count <= 5) {
                    if (three0708 == false) {
                        binding.Threetv0708.setBackgroundResource(R.drawable.rounded_yellow_bg_5);
                        binding.Threetv0708.setTextColor(getResources().getColor(R.color.white));
                        three0708 = true;
                        count++;
                        AddRemoveTime("add", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0708.getText().toString());
                    } else {
                        binding.Threetv0708.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                        binding.Threetv0708.setTextColor(getResources().getColor(R.color.black));
                        three0708 = false;
                        if (count > 1) {
                            AddRemoveTime("remove", binding.ThreetvDate.getText().toString() + " " + binding.Threetv0708.getText().toString());
                            count--;
                        }
                    }
                } else {
                    App.showToast(DateTimeAct.this, getString(R.string.add_only_5_time_slot), Toast.LENGTH_LONG);
                }
            }
            Log.e("SelectedTab====", count + "");

        });


        // binding.tvDate.setOnClickListener(v -> { DataManager.DatePicker(DateTimeAct.this,binding.tvDate::setText);});
        //   binding.tvTime.setOnClickListener(v -> { DataManager.TimePicker(DateTimeAct.this,binding.tvTime::setText);});

/*
        binding.btnContinue.setOnClickListener(v -> {
            if(date.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_date), Toast.LENGTH_LONG);
            }

           else if(time.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_time), Toast.LENGTH_LONG);
            }

           else if(address.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_address), Toast.LENGTH_LONG);
            }
           else {
                startActivity(new Intent(this, ProviderListAct.class).putExtra("cycleModel",cycleId)
                      .putExtra("cycleImage",str_image_path).putExtra("problem",problem)
                        .putExtra("repairImage",repair_image_path)
                        .putExtra("date",date)
                        .putExtra("time",time)
                        .putExtra("address",address)
                        .putExtra("lat",latitude+"")
                        .putExtra("lon",longitude+"")
                        .putExtra("serviceType",type));
            }
        });
*/



/*
        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(DateTimeAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });
*/
        //   adapter = new AdapterTimeSlot(DateTimeAct.this);
        //    binding.rvdateTime.setAdapter(adapter);


        binding.btnContinue.setOnClickListener(v -> {

            Log.e("Comma Saprated Value===", addCommaSeprtValue());

            startActivity(new Intent(this, SelectAddressAct.class).putExtra("cycleModel", cycleId)
                    .putExtra("cycleImage", str_image_path).putExtra("problem", problem)
                    .putExtra("repairImage", repair_image_path)
                    //.putExtra("date",date)
                    .putExtra("time", addCommaSeprtValue())
                    .putExtra("address", address)
                    .putExtra("lat", latitude + "")
                    .putExtra("lon", longitude + "")
                    .putExtra("serviceType", type));

            for (int i = 0; i < timeSlotList.size(); i++) {
                Log.e("Date Tme Slot===", i + " " + timeSlotList.get(i));
            }

        });


    }

    public String addCommaSeprtValue() {
        StringBuilder buff = new StringBuilder();
        String sep = "";
        for (String str : timeSlotList) {
            buff.append(sep);
            buff.append(str);
            sep = ",";
        }
        return buff.toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    Log.e("addressStreet====", place.getAddress());
                    address = place.getAddress();
                    binding.tvAddress.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }

        }

    }

    public void AddRemoveTime(String chk, String slot) {
        if (chk.equals("add")) {
            if (arrayList.size() == 0) {
                timeSlotList.add(slot);
            } else {
                for (int i = 0; i < timeSlotList.size(); i++) {
                    if (timeSlotList.get(i).equals(slot))
                        Toast.makeText(this, getString(R.string.already_added), Toast.LENGTH_SHORT).show();
                    else timeSlotList.add(slot);

                }
            }


        } else {
            if (timeSlotList.size() != 0) {
                for (int i = 0; i < timeSlotList.size(); i++) {
                    if (timeSlotList.get(i).equals(slot)) timeSlotList.remove(i);
                }
            }
        }
    }


  /*  public String covertIntoFrench(String date) {

        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.ES)
                        .setTargetLanguage(FirebaseTranslateLanguage.FR)
                        .build();
        final FirebaseTranslator Translator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);


        Translator.translate(date)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                //do something with the translated text
                                convrtDate = translatedText;
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //do somthing in failure scenario
                            }
                        });

        return convrtDate;

    }*/




 /*   public void translateText(FirebaseTranslator langTranslator,String text,int i) {
        //translate source text to english
        langTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                               if(i==1) binding.FirsttvDate.setText(translatedText);
                                   else if(i==2) binding.SecondtvDate.setText(translatedText);
                               else if(i==3) binding.ThreetvDate.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DateTimeAct.this,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

    }

    public void downloadTranslatorAndTranslate(String langCode,String text,int i) {
        //get source language id from bcp code
        int sourceLanguage = FirebaseTranslateLanguage
                .languageForLanguageCode(langCode);

        //create translator for source and target languages
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage)
                        .setTargetLanguage(FirebaseTranslateLanguage.FR)
                        .build();
        final FirebaseTranslator langTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //download language models if needed
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        langTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.d("translator", "downloaded lang  model");
                                //after making sure language models are available
                                //perform translation
                                translateText(langTranslator,text, i);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DateTimeAct.this,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void translateTextToFrench(String text,int i) {
        //First identify the language of the entered text
        FirebaseLanguageIdentification languageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != "und") {
                                    Log.d("translator", "lang "+languageCode);
                                    //download translator for the identified language
                                    // and translate the entered text into english
                                    downloadTranslatorAndTranslate(languageCode, text,i);
                                } else {
                                    Toast.makeText(DateTimeAct.this,
                                            "Could not identify language of the text entered",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DateTimeAct.this,
                                        "Problem in identifying language of the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }*/



}
