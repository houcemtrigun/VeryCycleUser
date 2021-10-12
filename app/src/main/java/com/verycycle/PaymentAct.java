package com.verycycle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentAuthConfig;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.verycycle.adapter.ShowCardAdapter;
import com.verycycle.databinding.ActivityPayBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.GetCardModel;
import com.verycycle.model.PaymentModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAct  extends AppCompatActivity implements OnItemPositionListener {
    ActivityPayBinding binding;
    String amount = "", requestId = "", cardNumber = "", expiryMonth = "", expiryDate = "", cvvv = "";

    AlertDialog.Builder alertBuilder;
    VeryCycleUserInterface apiInterface;
    ArrayList<GetCardModel.Result> arrayList;
    boolean selectCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pay);
        initViews();

        if (NetworkReceiver.isConnected()) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }


    private void initViews() {
        arrayList =  new ArrayList<>();
        binding.header1.tvTitle.setText(getString(R.string.payment));
        if (getIntent() != null) {
            amount = SessionManager.readString(PaymentAct.this,"price","");
            requestId = getIntent().getStringExtra("request_id");
            // DecimalFormat df = new DecimalFormat("0.00");
            binding.btMakePayment.setText("€" + String.format("%.2f", Double.parseDouble(amount)) + " " + getString(R.string.pay));
            binding.payment.setText("€" + String.format("%.2f", Double.parseDouble(amount)) + " " +   getString(R.string.pay));
        }

        binding.btMakePayment.setOnClickListener(v -> {
            if (binding.cardForm.isValid())
                paymentConfirmDialog();
            else Toast.makeText(this, getText(R.string.please_complete_form), Toast.LENGTH_LONG).show();
        });
        binding.payment.setOnClickListener(v -> {

            if(selectCheck== true)   PayConfrm();
        else Toast.makeText(this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();

        });

        binding.header1.ivBack1.setOnClickListener(v -> onBackPressed());


        cardInit();
    }

    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentAct.this);
        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void cardPayment() {

        Card.Builder card = new Card.Builder(cardNumber,
                Integer.valueOf(expiryMonth),
                Integer.valueOf(expiryDate),
                cvvv);

        if (!card.build().validateCard()) {
            Toast.makeText(PaymentAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
            return;
        }
           Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_TEST_KEY);
       // Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        stripe.createCardToken(
                card.build(), new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                        DataManager.getInstance().hideProgressMessage();
                        Log.e("Stripe Token===", token.getId());
                        // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                        // charge(token);
                        if (NetworkReceiver.isConnected())
                            PayProvider1(amount, requestId, token.getId());
                        else
                            Toast.makeText(PaymentAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        DataManager.getInstance().hideProgressMessage();
                        e.printStackTrace();
                        Toast.makeText(PaymentAct.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void paymentConfirmDialog() {
        alertBuilder = new AlertDialog.Builder(PaymentAct.this);
        alertBuilder.setTitle(getString(R.string.confirm_before_pay));
        alertBuilder.setMessage(getString(R.string.card_number)+ binding.cardForm.getCardNumber() + "\n" +
                        getString(R.string.expiry_date) + binding.cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                        getString(R.string.card_cvv)+ binding.cardForm.getCvv() + "\n"
                // "Postal code: " + binding.cardForm.getPostalCode() + "\n" +
                /* "Phone number: " + binding.cardForm.getMobileNumber() */);
        alertBuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                        Integer.valueOf(binding.cardForm.getExpirationMonth()),
                        Integer.valueOf(binding.cardForm.getExpirationYear()),
                        binding.cardForm.getCvv());

                if (!card.build().validateCard()) {
                    Toast.makeText(PaymentAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                   Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_TEST_KEY);
              //  Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

                DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
                stripe.createCardToken(
                        card.build(), new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(Token token) {
                                DataManager.getInstance().hideProgressMessage();
                                Log.e("Stripe Token===", token.getId());
                                // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                // charge(token);
                                if (NetworkReceiver.isConnected())
                                    PayProvider(amount, requestId, token.getId());
                                else
                                    Toast.makeText(PaymentAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                            }

                            @Override
                            public void onError(@NotNull Exception e) {
                                DataManager.getInstance().hideProgressMessage();
                                e.printStackTrace();
                                Toast.makeText(PaymentAct.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        alertBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }


    private void PayProvider(String amount, String requestId, String token) {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
        map.put("request_id", requestId);
        map.put("payment_method", "Stripe");
        map.put("total_amount", amount);
        map.put("token", token);
        map.put("currency", "EURO");
        Log.e("MapMap", "PAYMENT REQUEST" + map);
        Call<PaymentModel> payCall = apiInterface.payment(map);
        payCall.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    PaymentModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        //String dataResponse = new Gson().toJson(response.body());
                        // Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);
                     //   addCard(cardForm.getCardNumber(),
                           //     cardForm.getExpirationMonth(), cardForm.getExpirationYear(), cardForm.getCvv());

                        Toast.makeText(PaymentAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentAct.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.status.equals("0")) {
                        Toast.makeText(PaymentAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }

    private void PayProvider1(String amount, String requestId, String token) {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
        map.put("request_id", requestId);
        map.put("payment_method", "Stripe");
        map.put("total_amount", amount);
        map.put("token", token);
        map.put("currency", "USD");
        Log.e("MapMap", "PAYMENT REQUEST" + map);
        Call<PaymentModel> payCall = apiInterface.payment(map);
        payCall.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    PaymentModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        //String dataResponse = new Gson().toJson(response.body());
                        // Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);

                        Toast.makeText(PaymentAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentAct.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.status.equals("0")) {
                        Toast.makeText(PaymentAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


    private void getCardList() {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
        Log.e("MapMap", "GET CARD REQUEST" + map);
        Call<GetCardModel> payCall = apiInterface.getCardList(map);
        payCall.enqueue(new Callback<GetCardModel>() {
            @Override
            public void onResponse(Call<GetCardModel> call, Response<GetCardModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    GetCardModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "GET CARD RESPONSE" + dataResponse);
                    arrayList.clear();
                    if (data.status.equals("1")) {
                        arrayList.addAll(data.result);
                        Log.e("dhhdhdhdhd", "jjdfjfdjdjdj====");
                        if (arrayList.size() != 0) {
                            binding.layoutRv.setVisibility(View.VISIBLE);
                            binding.ViewScroll.setVisibility(View.GONE);
                            binding.payment.setText("€" + String.format("%.2f", Double.parseDouble(amount)) + " "+getString(R.string.pay));
                            binding.rvCard.setAdapter(new ShowCardAdapter(PaymentAct.this, PaymentAct.this, arrayList));
                        } else {
                            Log.e("dhhdhdhdhd", "jjdfjfdjdjdj====");
                            binding.layoutRv.setVisibility(View.GONE);
                            binding.ViewScroll.setVisibility(View.VISIBLE);
                        }

                    } else if (data.status.equals("0")) {
                        binding.layoutRv.setVisibility(View.GONE);
                        binding.ViewScroll.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<GetCardModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }



    @Override
    public void onPosition(int position) {
        cardNumber = arrayList.get(position).cardNumber;
        expiryMonth = arrayList.get(position).expiryMonth;
        expiryDate = arrayList.get(position).expiryDate;
        cvvv = arrayList.get(position).cvcCode;
        selectCheck = true;
    }



    private void PayConfrm() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentAct.this);
        String input = cardNumber;
        String lastFourDigits = "";     //substring containing last 4 characters
        if (input.length() > 4) {
            lastFourDigits = input.substring(input.length() - 4);
        } else {
            lastFourDigits = input;
        }
        builder1.setTitle(getString(R.string.confirm_before_pay));
        builder1.setMessage(getString(R.string.card_number)+ "XXXXXXXXXXXX" + lastFourDigits);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        cardPayment();
                    }
                });

        builder1.setNegativeButton(
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void Auth(){

        final PaymentAuthConfig.Stripe3ds2UiCustomization uiCustomization =
                new PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
                        .setLabelCustomization(
                                new PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder()
                                        .setTextFontSize(12)
                                        .build())
                        .build();
        PaymentAuthConfig.init(new PaymentAuthConfig.Builder()
                .set3ds2Config(new PaymentAuthConfig.Stripe3ds2Config.Builder()
                        .setTimeout(5)
                        .setUiCustomization(uiCustomization)
                        .build())
                .build());
    }


}
