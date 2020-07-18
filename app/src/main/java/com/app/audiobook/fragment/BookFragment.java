package com.app.audiobook.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.PreferenceUtil;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.catalog.ShopCatalog;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.ux.MainActivity;
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

public class BookFragment extends BaseFragment {

    View v;
    AudioBook audioBook;
    UserCatalog userCatalog;
    ShopCatalog shopCatalog;


    private BillingClient mBillingClient;
    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();

    public BookFragment(AudioBook audioBook, UserCatalog userCatalog, ShopCatalog shopCatalog) {
        this.audioBook = audioBook;
        this.userCatalog = userCatalog;
        this.shopCatalog = shopCatalog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_book, container, false);

        initInterface();

        initBuyButton();
        initRecyclerView();
        initBottomButton();

        initBillingClient();

        return v;
    }

    private void initInterface() {
        TextView title = v.findViewById(R.id.title);
        TextView author = v.findViewById(R.id.author);
        TextView count_parts = v.findViewById(R.id.count_parts);
        SelectableRoundedImageView cover = v.findViewById(R.id.book_cover);
        TextView text_button = v.findViewById(R.id.price);
        ImageView image_button = v.findViewById(R.id.image_button);
        ImageView background_button = v.findViewById(R.id.background_button);
        ConstraintLayout discount_layout = v.findViewById(R.id.discount_layout);
        TextView discount = v.findViewById(R.id.discount);
        TextView description = v.findViewById(R.id.text_description);

        ConstraintLayout isAdd = v.findViewById(R.id.isAdd);
        ImageView imageIsAdd = v.findViewById(R.id.imageIsAdd);
        TextView textIsAdd = v.findViewById(R.id.textIsAdd);

        title.setText(audioBook.getTitle());
        author.setText(audioBook.getAuthor().getName());
        count_parts.setText(String.valueOf("Частей: " + audioBook.getChapterSize()));
        Glide.with(getContext())
                .load(audioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(cover);

        if (userCatalog.contains(audioBook)) {

            isAdd.setVisibility(View.VISIBLE);
            textIsAdd.setText("Уже в аудиотеке");
            imageIsAdd.setImageResource(R.drawable.ic_check);
            imageIsAdd.setColorFilter(getResources().getColor(R.color.colorOrange));

            image_button.setColorFilter(getResources().getColor(R.color.colorWhite));
            background_button.setColorFilter(getResources().getColor(R.color.newColorOrange1));
            image_button.setImageResource(R.drawable.ic_play);
            text_button.setText("Слушать");

        } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {

            text_button.setText("Бесплатно");
            background_button.setColorFilter(getResources().getColor(R.color.colorFreePrice));
            text_button.setText("Забрать книгу");
            image_button.setImageResource(R.drawable.ic_shop_bag);

        } else if (audioBook.getBookPrice().getType().equals(TYPE_USUAL_PRICE)) {

            background_button.setColorFilter(getResources().getColor(R.color.colorUsualPrice));
            discount_layout.setVisibility(View.GONE);
            text_button.setText(audioBook.getBookPrice().getPrice() + "$");
            image_button.setImageResource(R.drawable.ic_shop_bag);

        } else if (audioBook.getBookPrice().getType().equals(TYPE_DISCOUNT_PRICE)) {

            background_button.setColorFilter(getResources().getColor(R.color.colorDiscountPrice));
            image_button.setImageResource(R.drawable.ic_shop_bag);
            discount_layout.setVisibility(View.VISIBLE);
            text_button.setText(String.valueOf(Integer.parseInt(audioBook.getBookPrice().getPrice()) * audioBook.getBookPrice().getDiscount()) + "$");
            text_button.setTextColor(getResources().getColor(R.color.colorGreen_7));
            discount.setVisibility(View.VISIBLE);
            discount.setText(audioBook.getBookPrice().getPrice() + "$");

        }

        description.setText(audioBook.getDescription());

        ConstraintLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            hide();
        });
    }

    private void initBuyButton() {
        ConstraintLayout button = v.findViewById(R.id.buttonListen);

        button.setOnClickListener(v1 -> {
            if (userCatalog.contains(audioBook)) {
                getParent().currentBook.setValue(audioBook);
                PreferenceUtil.setCurrentAudioBookId(getActivity(), audioBook.getId());

                getParent().viewPager.setCurrentItem(2);
                hide();
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
    });


}

    private void initBottomButton() {
        ConstraintLayout buttonDescription = v.findViewById(R.id.description);
        ConstraintLayout buttonChapters = v.findViewById(R.id.chapter);

        ImageView selector_description = v.findViewById(R.id.selector_description);
        ImageView selector_chapter = v.findViewById(R.id.selector_chapter);

        LinearLayout layout_description = v.findViewById(R.id.layout_description);
        RecyclerView layout_chapter = v.findViewById(R.id.recyclerViewBookActivity);

        buttonDescription.setOnClickListener(v1 -> {
            selector_description.setVisibility(View.VISIBLE);
            selector_chapter.setVisibility(View.INVISIBLE);
            //Открытие описания
            layout_description.setVisibility(View.VISIBLE);
            layout_chapter.setVisibility(View.INVISIBLE);
        });

        buttonChapters.setOnClickListener(v1 -> {
            selector_description.setVisibility(View.INVISIBLE);
            selector_chapter.setVisibility(View.VISIBLE);
            //Открытие глав
            layout_description.setVisibility(View.INVISIBLE);
            layout_chapter.setVisibility(View.VISIBLE);
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

    private void initRecyclerView() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBookActivity);
        ChapterAdapter adapter = new ChapterAdapter();

        adapter.setChapters(audioBook.getChapters());

        recyclerView.setAdapter(adapter);
    }

    private void hide() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

        fragmentTransaction.remove(this).commit();
    }

    public void launchBilling(String skuId) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap.get(skuId))
                .build();
        mBillingClient.launchBillingFlow(getActivity(), billingFlowParams);
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }
}
