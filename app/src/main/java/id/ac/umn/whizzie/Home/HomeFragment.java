package id.ac.umn.whizzie.Home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.ac.umn.whizzie.Activity.WisherActivity;
import id.ac.umn.whizzie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView home_middle_category, home_bottom_grid;

    private List<CategoryCard> ccList;
    private List<FeaturedGenieCard> fgList;

    public HomeFragment() {
        // Required empty public constructor
    }

    // Banner Slider //
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    // Banner Slider //

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        home_middle_category = view.findViewById(R.id.home_middle_category);
        home_bottom_grid = view.findViewById(R.id.home_bottom_featured_genies);
        // Button onClick for setting

        ((WisherActivity) getActivity()).showActionBar();

        // Banner Slider //
        bannerSliderViewPager =  view.findViewById(R.id.banner_slider_view_pager);
        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.landscapes));
        sliderModelList.add(new SliderModel(R.mipmap.landscapes4));

        sliderModelList.add(new SliderModel(R.mipmap.landscapes3));
        sliderModelList.add(new SliderModel(R.mipmap.landscapes2));

        sliderModelList.add(new SliderModel(R.mipmap.landscapes3));
        sliderModelList.add(new SliderModel(R.mipmap.landscapes4));

        sliderModelList.add(new SliderModel(R.mipmap.landscapes));
        sliderModelList.add(new SliderModel(R.mipmap.landscapes2));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE){
                    pageLooper();
                }
            }
        };
        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

        startBannerSlideShow();
        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSlideShow();
                if (event.getAction() == MotionEvent.ACTION_UP){
                    startBannerSlideShow();
                }
                return false;
            }
        });
        // Banner Slider //

        return view;
    }

    // Banner Slider //
    private void pageLooper(){
        if (currentPage == sliderModelList.size() - 2){
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
        if (currentPage == 1){
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage,false);
        }
    }

    private void startBannerSlideShow(){
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()){
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }
    private void stopBannerSlideShow(){
        timer.cancel();
    }
    // Banner Slider //

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Middle Category List Data Load
        home_middle_category.setHasFixedSize(true);

        home_middle_category.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        ccList = new ArrayList<>();

        //Testing Fetch Data
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();

        Log.d("R ID", "FASHION : " + R.drawable.fashion);

        // Query Must be done using Event Listeners
        dbRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadCategoryCard(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ccList.add(new CategoryCard("Furniture"));
//        ccList.add(new CategoryCard("Collectibles"));
//        ccList.add(new CategoryCard("Artwork"));
//        ccList.add(new CategoryCard("Machinery"));
//        ccList.add(new CategoryCard("Tools"));
//        ccList.add(new CategoryCard("Software"));
//        ccList.add(new CategoryCard("Fashion"));

        // TODO: Load Featured Genies Grid
        // Middle Category List Data Load
        home_bottom_grid.setHasFixedSize(true);

        home_bottom_grid.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        fgList = new ArrayList<>();

        fgList.add(new FeaturedGenieCard("Alexander"));
        fgList.add(new FeaturedGenieCard("Ellianto"));
        fgList.add(new FeaturedGenieCard("Karissa"));
        fgList.add(new FeaturedGenieCard("Leonardo"));

        FeaturedGenieCardAdapter fgAdapter = new FeaturedGenieCardAdapter(this.getContext(), fgList);
        home_bottom_grid.setAdapter(fgAdapter);

        // TODO : Set on click listener of Search


    }

    private void loadCategoryCard(DataSnapshot dataSS){
        for(DataSnapshot temp : dataSS.getChildren()) {
            String catName = temp.getKey();
            int imageID = 0;

            for(DataSnapshot child : temp.getChildren()){
                GenericTypeIndicator<Integer> gti = new GenericTypeIndicator<Integer>() {};
                imageID = child.getValue(gti);
            }

            ccList.add(new CategoryCard(imageID, catName));
        }


        CategoryCardAdapter ccAdapter = new CategoryCardAdapter(this.getContext(), ccList);

        home_middle_category.setAdapter(ccAdapter);
    }

}
