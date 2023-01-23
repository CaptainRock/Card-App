package com.bj.enterprise.simple.simple;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Application;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.bj.enterprise.simple.simple.model.Crystal;
import com.bj.enterprise.simple.simple.utils.ListViewAdapter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
//import android.widget.SearchView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.bj.enterprise.simple.simple.cards.SliderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = " CrystalMain";
    private ArrayList<Crystal> crystalList;
    private int crystalCount = 0;
    private SliderAdapter mAdapter;
    private final int[][] dotCoords = new int[5][2];
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    Parcelable state;
    private String[] descriptions;
    private ArrayList<String> descriptions_from_firestore = new ArrayList<>();
    public String[] countries = {};
    public ArrayList<String> countries_from_firestore = new ArrayList<String>();
    private final String[] temperatures = {"21°C", "19°C", "17°C", "23°C", "20°C", "15°C"};
    private final String[] times = {"Aug 1 - Dec 15    7:00-18:00", "Sep 5 - Nov 10    8:00-16:00", "Mar 8 - May 21    7:00-18:00"};
    final String RESTAURANT_URL1_BIG = "https://firebasestorage.googleapis.com/v0/b/cardslider-android-maste-d32b4.appspot.com/o/p10_big.jpg?alt=media";
    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private TextSwitcher descriptionsSwitcher;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ArrayList<String> arraylist;
    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;
    private ListView list;
    private ListViewAdapter adapter;
    StorageReference databaseRef;


    String crystal = null;
    //ArrayList<String> images_list;
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Change the visibility here
            //  list.setVisibility(View.GONE);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            // list.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (list.getVisibility() != View.VISIBLE)
                list.setVisibility(View.VISIBLE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerActivityLifecycleCallbacks(new AppLifecycleTracker());

        /****
         * Moved startup logic to OnActivityStart in the AppLifeCycleTracker
         */


    }



    public void setup_listview() {


        list = findViewById(R.id.listview);
        //arraylist = new ArrayList<String>(crystalList.size());

        // Binds all strings into an array
        arraylist = new ArrayList<>(crystalList.stream().map(Crystal::getName).collect(Collectors.toList()));
        ArrayList<String> crystals = new ArrayList<>(crystalList.stream().map(Crystal::getName).collect(Collectors.toList()));
        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setVisibility(View.GONE);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext() ,LinearLayoutManager.VERTICAL, false);
        //list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                RelativeLayout rl = ((RelativeLayout) view);
                TextView tv = rl.findViewById(R.id.name);

                CharSequence s = tv.getText().toString();
                list.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this,s, Toast.LENGTH_LONG).show();
                int clickedPosition = -1;
                Log.e("Arraylist size Checked", String.valueOf(crystals.size()));
                for (int i = 0; i < crystals.size(); i++) {

                    if (crystals.get(i).toLowerCase().startsWith(s.toString().toLowerCase())) {

                        clickedPosition = i;

                    }
                }
                    if (clickedPosition == -1) {


                        clickedPosition = 1;

                    }





                int d = recyclerView.getLayoutDirection();

                if (clickedPosition < currentPosition) {

                    //CardSliderLayoutManager mLinearLayoutManager = new CardSliderLayoutManager(this);
                    //recyclerView.setLayoutManager(mLinearLayoutManager);
                    recyclerView.getLayoutManager().scrollToPosition(clickedPosition);
                    //mLinearLayoutManager.scrollToPosition(clickedPosition);
                    onActiveCardChange(clickedPosition);
                } else {

                    //recyclerView.smoothScrollToPosition(clickedPosition);
                    //recyclerView.smoothScrollToPosition(clickedPosition);
                    recyclerView.scrollToPosition(clickedPosition);
                    onActiveCardChange(clickedPosition);
                }


            }
        });


    }


    public void read_countries() {

        countries = crystalList.stream().map(Crystal::getName).toArray(String[]::new);
        descriptions = crystalList.stream().map(Crystal::getDesc).toArray(String[]::new);
    }


    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new SliderAdapter(crystalList, this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        recyclerView.setOnFlingListener(null);
        new CardSnapHelper().attachToRecyclerView(recyclerView);


    }


    @Override
    public void onStart() {
        super.onStart();

        Log.e("ON_START", "Inside Onstart");

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initSwitchers() {

        descriptionsSwitcher = (TextSwitcher) findViewById(R.id.ts_description);
        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
//        descriptionsSwitcher.setCurrentText(descriptions[0]);

    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView = (TextView) findViewById(R.id.tv_country_1);
        country2TextView = (TextView) findViewById(R.id.tv_country_2);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        country1TextView.setText(countries[0]);
        country2TextView.setAlpha(0f);

        country1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        country2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    }

    private void initCountryText(int currentPosition) {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView = (TextView) findViewById(R.id.tv_country_1);
        country2TextView = (TextView) findViewById(R.id.tv_country_2);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        country1TextView.setText(countries[currentPosition]);
        country2TextView.setAlpha(0f);

        country1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
        country2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    }




    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    public void setDescription(int pos){

        SpannableStringBuilder sb = new SpannableStringBuilder();
        String desc = descriptions[pos % descriptions.length];
        sb.append(desc);
        //Color blue = Color.valueOf(Color.BLUE);
        sb.setSpan(new ForegroundColorSpan(0xff92A1CF), 0, desc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
        sb.setSpan(italicSpan, 0, desc.length(), 0); // set characters 4 to 7 to red
        // .setText(sb, TextView.BufferType.SPANNABLE);

        //descriptionsSwitcher.setText(sb);

        final TextView t = (TextView) descriptionsSwitcher.getNextView();
        t.scrollTo(0,0);
        t.setText(sb, TextView.BufferType.SPANNABLE);
        t.setVerticalScrollBarEnabled(true);
        t.setLines(10);
        t.setMovementMethod(new ScrollingMovementMethod());
        descriptionsSwitcher.showNext();
        //descriptionsSwitcher.setText(sb);



    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos > currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCountryText(countries[pos % countries.length], left2right);

       setDescription(pos);


        currentPosition = pos;
    }



    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    public boolean isExist(List<String> myList, String str) {

        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).equals(str)) {
                return true;
            }
        }

        return false;
    }

    private class TextViewFactory implements ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(MainActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainActivity.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }


    }


    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm = (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_SMALLRES_LOC, RESTAURANT_URL1_BIG);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
                    startActivity(intent, options.toBundle());
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

    class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks, SearchView.OnQueryTextListener {

        private static final String TAG = "AppLifecycleTracker";
        private int numberActivitiesStart = 0;
        private int previousPosition = -1;
        private boolean startup = true;
        private DatabaseReference mRef;


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {


        }

        @Override
        public void onActivityStarted(Activity activity) {


            if (numberActivitiesStart == 0) {
                // The application come from background to foreground
                Log.d(TAG, "AppController status > onActivityStarted:  app went to foreground");

                BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // Get extra data included in the Intent
                        String crystal = intent.getStringExtra("crystal");
                        String desc = intent.getStringExtra("desc");
                        //Log.e("RECEIVED", crystal);
                        //Log.e("RECEIVED", desc);
                 /*
                        if (!isExist(countries_from_firestore, crystal)) {
                            countries_from_firestore.add(crystal);
                        }
                        if (!isExist(descriptions_from_firestore, desc)) {
                            descriptions_from_firestore.add(desc);
                        }
                   */
                        //Log.e("PUT_IN_COLLECTION", countries_from_firestore.get(0));



                        if (startup) {
                            initCountryText();
                            setDescription(0);
                            startup=false;
                        }
                        ;
                    }

                };


                // Enable Firestore logging
                FirebaseFirestore.setLoggingEnabled(true);
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Crystals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            crystalList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    crystalList.add(document.toObject(Crystal.class));
                            }

                            if(startup) {

                                initRecyclerView();
                                initSwitchers();
                                }
                            setup_listview();
                            read_countries();


                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });



                LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mMessageReceiver,
                        new IntentFilter("custom-message"));

                setContentView(R.layout.activity_main);

        /*
        MaterialFavoriteButton materialFavoriteButtonNice =
                (MaterialFavoriteButton) findViewById(R.id.favorite_nice);
        materialFavoriteButtonNice.setFavorite(true, false);
        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                           Toast.makeText(getApplicationContext(),"Favorite", Toast.LENGTH_LONG).show(); ;
                        } else {
                            Toast.makeText(getApplicationContext(),"Not Favorite", Toast.LENGTH_LONG).show(); ;
                        }
                    }
                });
        materialFavoriteButtonNice.setOnFavoriteAnimationEndListener(
                new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                        Toast.makeText(getApplicationContext(),"Animation Favorite", Toast.LENGTH_LONG).show(); ;

                    }
                });


*/
                // TODO SEARCHVIEW LATER

                SearchView editsearch = findViewById(R.id.search);
                editsearch.setOnQueryTextListener(this);


                //editsearch.addTextChangedListener(filterTextWatcher);


                //    Toast.makeText(getApplicationContext(), "Previous Position "+ previousPosition, Toast.LENGTH_SHORT).show();

            if(!startup) {
                initRecyclerView();
                initSwitchers();
            }

            }
            Log.e("ON_ACTIVITY_STARTED", "Inside OnActivityStarted");
            numberActivitiesStart++;
        }

        @Override
        public void onActivityResumed(Activity activity) {


            if (recyclerView != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(state);
                if (previousPosition != -1) {



                   setDescription(previousPosition);
             //       descriptionsSwitcher.setText(descriptions[previousPosition]);
                    initCountryText(previousPosition);
                }

            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            previousPosition = layoutManger.getActiveCardPosition();
            state = recyclerView.getLayoutManager().onSaveInstanceState();
        }

        @Override
        public void onActivityStopped(Activity activity) {
            numberActivitiesStart--;
            if (numberActivitiesStart == 0) {
                // The application go from foreground to background
                Log.d(TAG, "AppController status > onActivityStopped: app went to background");
                previousPosition = layoutManger.getActiveCardPosition();
                //  countries_from_firestore = null;
                //  descriptions_from_firestore = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }


        @Override
        public boolean onQueryTextChange(String newText) {

            String text = newText;
            list.setVisibility(View.VISIBLE);
            adapter.filter(text);


            return false;

        }


        @Override
        public boolean onQueryTextSubmit(String query) {

            // String text = newText;
            filter(query);

            return false;
        }

        private void filter(String text) {

            // Toast.makeText(this,"Hi There", Toast.LENGTH_LONG);

            int clickedPosition = -1;

            for (int i = 0; i < descriptions.length; i++) {

                if (descriptions[i].toLowerCase().contains(text.toLowerCase())) {

                    clickedPosition = i;

                    Log.e("SEARCH_SUCCESS", "Text is " + text);

                }

                if (clickedPosition == -1) {


                    clickedPosition = 1;
                    Log.e("SEARCH_ERROR", "Text is " + text);

                }


                int d = recyclerView.getLayoutDirection();

                if (clickedPosition < currentPosition) {

                    //CardSliderLayoutManager mLinearLayoutManager = new CardSliderLayoutManager(this);
                    //recyclerView.setLayoutManager(mLinearLayoutManager);
                    recyclerView.getLayoutManager().scrollToPosition(clickedPosition);
                    //mLinearLayoutManager.scrollToPosition(clickedPosition);
                    onActiveCardChange(clickedPosition);
                } else {

                    //recyclerView.smoothScrollToPosition(clickedPosition);
                    //recyclerView.smoothScrollToPosition(clickedPosition);
                    recyclerView.scrollToPosition(clickedPosition);
                    onActiveCardChange(clickedPosition);
                }


            }

            }


        }


    }


