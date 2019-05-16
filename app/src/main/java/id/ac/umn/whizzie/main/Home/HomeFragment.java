package id.ac.umn.whizzie.main.Home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.ac.umn.whizzie.R;
import id.ac.umn.whizzie.main.Activity.MainActivity;
import id.ac.umn.whizzie.main.Search.SearchCard;
import id.ac.umn.whizzie.main.Search.SearchCardAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView home_middle_category, home_bottom_grid, home_bottom_featured_products;

    private List<CategoryCard> ccList;
    private List<FeaturedGenieCard> fgList;
    private List<SearchCard> scList;
    private ArrayMap<String, String> unamePair;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // Banner Slider //
    private ViewPager bannerSliderViewPager;
    private List<Slider> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    // Banner Slider //

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        home_middle_category = view.findViewById(R.id.home_middle_category);
        home_bottom_grid = view.findViewById(R.id.home_bottom_featured_genies);
        home_bottom_featured_products = view.findViewById(R.id.home_bottom_featured_products);
        // Button onClick for setting

        ((MainActivity) getActivity()).showActionBar();

        // Banner Slider //
        bannerSliderViewPager =  view.findViewById(R.id.banner_slider_view_pager);
        sliderModelList = new ArrayList<Slider>();

        sliderModelList.add(new Slider(R.mipmap.slider1));
        sliderModelList.add(new Slider(R.mipmap.slider2));
        sliderModelList.add(new Slider(R.mipmap.slider3));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

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
        home_middle_category.setItemViewCacheSize(15);
        home_middle_category.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        ccList = new ArrayList<>();

        // Query Must be done using Event Listeners
        dbRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadCategoryCard(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        home_bottom_grid.setHasFixedSize(true);
        home_bottom_grid.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        unamePair = new ArrayMap<>();

        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadUsernameList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        fgList = new ArrayList<>();

        dbRef.child("featured").child("featuredGenies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadFeaturedGenie(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        home_bottom_featured_products.setHasFixedSize(true);
        home_bottom_featured_products.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        scList = new ArrayList<>();

        dbRef.child("featured").child("featuredProducts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadFeaturedProduct(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    private void loadCategoryCard(DataSnapshot dataSS){
        for(DataSnapshot temp : dataSS.getChildren()) {
            String catName = temp.getKey();
            String imageID = "";

            for(DataSnapshot child : temp.getChildren()){
                imageID = child.getValue().toString();
            }

            ccList.add(new CategoryCard(imageID, catName));
        }

        CategoryCardAdapter ccAdapter = new CategoryCardAdapter(this.getContext(), ccList);
        home_middle_category.setAdapter(ccAdapter);
    }

    private void loadUsernameList(DataSnapshot ds){
        for(DataSnapshot uid : ds.getChildren()){
            String user_id = uid.getKey();
            String username = uid.child("name").getValue().toString();

            unamePair.put(user_id, username);
        }
    }

    private void loadFeaturedGenie(DataSnapshot dataSS){
        for(DataSnapshot temp : dataSS.getChildren()){
            fgList.add(new FeaturedGenieCard(temp.getKey(),
                    unamePair.get(temp.getKey()),
                    temp.getKey() + "/profile.jpg" ));
        }

        FeaturedGenieCardAdapter fgAdapter = new FeaturedGenieCardAdapter(this.getContext(), fgList);
        home_bottom_grid.setAdapter(fgAdapter);
    }

    private void loadFeaturedProduct(DataSnapshot ds){
        for(DataSnapshot temp : ds.getChildren()){
            // Fetch Products
            dbRef.child("products/" + temp.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot product) {
                    final String itemKey = product.getKey();
                    final String uid = product.child("uidUpProduct").getValue().toString();
                    final String prodName = product.child("nameProduct").getValue().toString();
                    final String prodImg = product.child("pictureProduct").getValue().toString();

                    final long prodPrice = Long.parseLong(product.child("priceProduct").getValue().toString());

                    dbRef.child("productRelation").child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            scList.add(new SearchCard(
                                    unamePair.get(uid),
                                    uid + "/profile.jpg",
                                    prodName,
                                    prodImg,
                                    dataSnapshot.getChildrenCount(),
                                    prodPrice,
                                    true,
                                    itemKey
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }

        SearchCardAdapter sca = new SearchCardAdapter(this.getContext(), scList);
        home_bottom_featured_products.setAdapter(sca);
    }
}
