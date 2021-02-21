package com.ysh.mapdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.ysh.mapdemo.newFragment.FirstFragment;

public class newActivity extends AppCompatActivity {

    NavController navController;

    MotionLayout homeLayout;
    MotionLayout searchLayout;
    MotionLayout userLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
//        Intent intent = getIntent();
//        if(intent==null){
//
//        }else {
//            Bundle bundle = intent.getBundleExtra("index");
//            if(bundle==null){
//
//            }else {
//                String test = bundle.getString("data");
//
//                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("data", test);
//                intent.putExtra("index",bundle1);
//                FirstFragment fragment = new FirstFragment();
//                fragment.setArguments(bundle1);//数据传递到fragment中
//
//
//            }
//        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.framelayout,fragment);
//        fragmentTransaction.commit();


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment);
        NavController navController = navHostFragment.getNavController();
//        navController= Navigation.findNavController(newActivity.this,R.id.fragment);
        homeLayout = findViewById(R.id.homeLayout);
        searchLayout = findViewById(R.id.searchLayout);
        userLayout = findViewById(R.id.userLayout);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选中直接转换到对应页面
                navController.navigate(R.id.firstFragment);
            }
        });
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.secondFragment);
            }
        });
        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.thirdFragment);
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                //禁止控制器存储栈
                controller.popBackStack();
                //每次点击检测到改变时，每一个MotionLayout动画重置，实现单选
                homeLayout.setProgress(0f);
                searchLayout.setProgress(0f);
                userLayout.setProgress(0f);
                //目标MotionLayout进行动画播放直至结束
                switch (destination.getId()){
                    case R.id.firstFragment:
                        homeLayout.transitionToEnd();
                        break;
                    case R.id.secondFragment:
                        searchLayout.transitionToEnd();
                        break;
                    case R.id.thirdFragment:
                        userLayout.transitionToEnd();
                        break;
                }
            }
        });
        navController.navigate(R.id.secondFragment);
        Intent intent = getIntent();
        if(intent==null){

        }else {
            Bundle bundle = intent.getBundleExtra("index");
            if(bundle==null){

            }else {
                String test = bundle.getString("data");

                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                Bundle bundle1 = new Bundle();
                bundle1.putString("data", test);
                intent.putExtra("index",bundle1);
                FirstFragment fragment = new FirstFragment();
                fragment.setArguments(bundle1);//数据传递到fragment中
                navController.navigate((R.id.firstFragment));


            }
        }
//        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
//        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
//        NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavController navController = Navigation.findNavController(findViewById(R.id.fragment));
        navController.navigateUp();
    }
}