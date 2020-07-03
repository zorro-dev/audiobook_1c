package com.app.audiobook.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.catalog.ShopCatalog;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.component.JSONManager;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.audiobook.audio.book.BookPrice.TYPE_DISCOUNT_PRICE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_FREE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_USUAL_PRICE;

public class PurchaseFragment extends BaseFragment {

    View v;
    AudioBook audioBook;
    UserCatalog userCatalog;
    ShopCatalog shopCatalog;


    private BillingClient mBillingClient;
    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();

    public PurchaseFragment(AudioBook audioBook, UserCatalog userCatalog, ShopCatalog shopCatalog) {
        this.audioBook = audioBook;
        this.userCatalog = userCatalog;
        this.shopCatalog = shopCatalog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_buy_book, container, false);

        initInterface();

        initBuyButton();

        initBillingClient();

        return v;
    }

    private void initInterface(){
        TextView title = v.findViewById(R.id.title);
        TextView author = v.findViewById(R.id.author);
        TextView count_parts = v.findViewById(R.id.count_parts);
        SelectableRoundedImageView cover = v.findViewById(R.id.book_cover);
        TextView price = v.findViewById(R.id.price);
        ImageView layout_color = v.findViewById(R.id.layout_color);
        ConstraintLayout discount_layout = v.findViewById(R.id.discount_layout);
        TextView discount= v.findViewById(R.id.discount);
        TextView description = v.findViewById(R.id.description);

        ImageView colorButton = v.findViewById(R.id.color_button);
        TextView textButton = v.findViewById(R.id.text_button);

        title.setText(audioBook.getTitle());
        author.setText(audioBook.getAuthor().getName());
        count_parts.setText(String.valueOf("Частей: " + audioBook.getChapterSize()));
        Glide.with(getContext())
                .load(audioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(cover);

        if (userCatalog.contains(audioBook)) {
            colorButton.setColorFilter(getResources().getColor(R.color.colorGray_1));
            textButton.setText("Уже в аудиотеке");
        } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
            price.setText("Бесплатно");
            colorButton.setColorFilter(getResources().getColor(R.color.colorFreePrice));
            textButton.setText("Забрать книгу");
        } else {
            price.setText(audioBook.getBookPrice().getPrice() + "$");
        }

        if (audioBook.getBookPrice().getDiscount() == 0) {
            discount_layout.setVisibility(View.GONE);
        } else {
            discount_layout.setVisibility(View.VISIBLE);
            price.setText(String.valueOf(Integer.parseInt(audioBook.getBookPrice().getPrice()) * audioBook.getBookPrice().getDiscount()) + "$");
            price.setTextColor(getResources().getColor(R.color.colorGreen_5));
            discount.setVisibility(View.VISIBLE);
            discount.setText(audioBook.getBookPrice().getPrice() + "$");
        }

        if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorFreePrice));
        } else if (audioBook.getBookPrice().getType().equals(TYPE_USUAL_PRICE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorUsualPrice));
        } else if (audioBook.getBookPrice().getType().equals(TYPE_DISCOUNT_PRICE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorDiscountPrice));
        }

        description.setText(audioBook.getDescription());

        ConstraintLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

            fragmentTransaction.addToBackStack("PurchaseFragment");
            fragmentTransaction.remove(this).commit();
        });
    }

    private void initBuyButton() {
        ConstraintLayout buy = v.findViewById(R.id.button);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCatalog.contains(audioBook)) {
                    Toast.makeText(getContext(), "Уже добавлено", Toast.LENGTH_SHORT).show();
                } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
                    addToUserCatalog();
                    initInterface();
                } else {
                    //показать фрагмент покупки
                    if (mSkuDetailsMap.containsKey(audioBook.getId())) {
                        Log.v("lol", "contains");
                        launchBilling(audioBook.getId());
                    } else {
                        Toast.makeText(getContext(), "Ошибка получения товара", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addToUserCatalog() {
        userCatalog.getCatalogList().add(audioBook);
        userCatalog.updateList();

        shopCatalog.updateList();

        FirebaseDatabase.getInstance().getReference("BookCatalog")
                .child("ByUsers").child(getAuthManager().getUser().getId())
                .child(audioBook.getId()).setValue(JSONManager.exportToJSON(audioBook));

    }

    private void initBillingClient() {
        mBillingClient = BillingClient.newBuilder(getContext())
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                            //сюда мы попадем когда будет осуществлена покупка

                            Purchase purchase = purchases.get(0);

                            addToUserCatalog();
                        }
                    }
                }).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //здесь мы можем запросить информацию о товарах и покупках
                    querySkuDetails();

                    //hideProgress();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //сюда мы попадем если что-то пойдет не так
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                //hideProgress();
            }
        });
    }

    private void querySkuDetails() {
        SkuDetailsParams.Builder skuDetailsParamsBuilder = SkuDetailsParams.newBuilder();
        List<String> skuList = new ArrayList<>();
        skuList.add(audioBook.getId());
        skuDetailsParamsBuilder.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(skuDetailsParamsBuilder.build(), new SkuDetailsResponseListener() {

            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                if (billingResult.getResponseCode() == 0) {
                    for (SkuDetails skuDetails : skuDetailsList) {

                        Log.v("lol", "id : " + skuDetails.getSku());

                        mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    }
                    //if (prepearedSkuId != null) {
                    //    launchBilling(prepearedSkuId);
                    //}
                }
            }

        });
    }

    public void launchBilling(String skuId) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap.get(skuId))
                .build();
        mBillingClient.launchBillingFlow(getActivity(), billingFlowParams);
    }

}
