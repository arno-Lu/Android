package drawerLayoutActivity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mechrevo.toolbartest.R;

/**
 * Created by mechrevo on 2016/4/15.
 */
public class SimpleDrawerActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_simple_drawer);

        mDrawer = (DrawerLayout) findViewById(R.id.simple_navigation_drawer);
        mDrawer .setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(SimpleDrawerActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(SimpleDrawerActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDrawerStateChanged(int newState) {


            }
        });
        RadioGroup scrimColorGroup = (RadioGroup) findViewById(R.id.rg_scrim_color);
        scrimColorGroup.setOnCheckedChangeListener(this);

        RadioGroup openDrawerGroup = (RadioGroup) findViewById(R.id.rg_open_drawer);
        openDrawerGroup.setOnCheckedChangeListener(this);

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbtn_pink) {
            mDrawer.setScrimColor(getResources().getColor(R.color.color_e81d62));//设置滑动时渐变的阴影颜色

        } else if (checkedId == R.id.rbtn_green) {
            mDrawer.setScrimColor(getResources().getColor(R.color.color_4bae4f));

        } else if (checkedId == R.id.rbtn_blue) {
            mDrawer.setScrimColor(getResources().getColor(R.color.color_2095f2));

        } else if (checkedId == R.id.rbtn_from_left) {
            mDrawer.openDrawer(Gravity.LEFT);//打开菜单栏

        } else if (checkedId == R.id.rbtn_from_right) {
            mDrawer.openDrawer(Gravity.RIGHT);

        }
    }

}
