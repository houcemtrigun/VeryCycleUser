package com.verycycle.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.verycycle.MainActivity;
import com.verycycle.PaymentAct;
import com.verycycle.R;
import com.verycycle.adapter.ShowCardAdapter;
import com.verycycle.databinding.FragmentFinalPayBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.listener.FinalPay;
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

public class PayFinalFramentDialog extends BottomSheetDialogFragment implements OnItemPositionListener {
    public String TAG = "PayFinalFramentDialog";
    FragmentFinalPayBinding binding;
    VeryCycleUserInterface apiInterface;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    ArrayList<GetCardModel.Result> arrayList;
    boolean selectCheck = false;
    String amount = "", amount2="", requestId = "", cardNumber = "", expiryMonth = "", expiryDate = "", cvvv = "";
    FinalPay listener;

    public PayFinalFramentDialog(String amount) {
        this.amount = amount;
    }

    public PayFinalFramentDialog callBack(FinalPay listener) {
        this.listener = listener;

        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_final_pay, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        initBinding();
        return  dialog;
    }

    private void initBinding() {
        arrayList =  new ArrayList<>();

        if (NetworkReceiver.isConnected()) getCardList();
        else Toast.makeText(getActivity(), getActivity().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.btMakePayment.setOnClickListener(v -> {
            if (binding.cardForm.isValid()){
                Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                        Integer.valueOf(binding.cardForm.getExpirationMonth()),
                        Integer.valueOf(binding.cardForm.getExpirationYear()),
                        binding.cardForm.getCvv());

                if (!card.build().validateCard()) {
                    Toast.makeText(getActivity(), getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                Stripe stripe = new Stripe(getActivity(), Constant.STRIPE_TEST_KEY);
                //  Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

                DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
                stripe.createCardToken(
                        card.build(), new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(Token token) {
                                DataManager.getInstance().hideProgressMessage();
                                Log.e("Stripe Token===", token.getId());
                                // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                // charge(token);
                                if (NetworkReceiver.isConnected()){
                                    listener.pay(token.getId());
                                    dismiss();
                                }
                                  //  PayProvider(amount, requestId, token.getId());

                                else
                                    Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                            }

                            @Override
                            public void onError(@NotNull Exception e) {
                                DataManager.getInstance().hideProgressMessage();
                                e.printStackTrace();
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // paymentConfirmDialog();
            }

            else Toast.makeText(getActivity(), getActivity().getText(R.string.please_complete_form), Toast.LENGTH_LONG).show();
        });
        binding.payment.setOnClickListener(v -> {

            if(selectCheck== true)   cardPayment(); //PayConfrm();
            else Toast.makeText(getActivity(), getActivity().getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();

        });

        cardInit();
    }



    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup((AppCompatActivity) getActivity());
        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void cardPayment() {

        Card.Builder card = new Card.Builder(cardNumber,
                Integer.valueOf(expiryMonth),
                Integer.valueOf(expiryDate),
                cvvv);

        if (!card.build().validateCard()) {
            Toast.makeText(getActivity(), getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
            return;
        }
        Stripe stripe = new Stripe(getActivity(), Constant.STRIPE_TEST_KEY);
        // Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        stripe.createCardToken(
                card.build(), new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                        DataManager.getInstance().hideProgressMessage();
                        Log.e("Stripe Token===", token.getId());
                        // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                        // charge(token);
                        if (NetworkReceiver.isConnected()) {
                            listener.pay(token.getId());
                            dismiss();
                        }
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        DataManager.getInstance().hideProgressMessage();
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getCardList() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        Log.e(TAG, "GET CARD REQUEST" + map);
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
                            binding.rvCard.setAdapter(new ShowCardAdapter(getActivity(), PayFinalFramentDialog.this, arrayList));
                        } else {
                            Log.e("dhhdhdhdhd", "jjdfjfdjdjdj====");
                            binding.layoutRv.setVisibility(View.GONE);
                            binding.ViewScroll.setVisibility(View.VISIBLE);
                            binding.btMakePayment.setText("€" + String.format("%.2f", Double.parseDouble(amount)) + " "+getString(R.string.pay));
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





}
