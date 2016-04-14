package translucentbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.mechrevo.toolbartest.R;

/**
 * Created by mechrevo on 2016/4/14.
 */
public class ImageTranslucentBarActivity extends AppCompatActivity{

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_translucent_bar);
    }
}
