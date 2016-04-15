package drawerLayoutActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.mechrevo.toolbartest.R;

/**
 * Created by mechrevo on 2016/4/15.
 */
public class ColudMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cloud_music);
    }
}
