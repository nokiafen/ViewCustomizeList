package com.heli.providerapp.acticity;

import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.heli.providerapp.R;

/**
 * <pre>
 *     author : lin
 *     e-mail :
 *     time   : 2019/07/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Test1Activity extends AppCompatActivity {
  View rootView;
  View contentLeft;
  View contentRight;
  int primitiveDegree=180;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test1);
    rootView=  findViewById(R.id.root_view);
    contentLeft=  findViewById(R.id.content1);
    contentRight=  findViewById(R.id.content2);
    init();
  }

  private void init() {
    rootView.setScaleX(-1);
    //contentLeft.setRotationY(180);
    contentRight.setRotationY(180);
  }

  private void rotates(View view){
    for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++) {
      ((ViewGroup)view).getChildAt(i).animate().rotationY(primitiveDegree).setDuration(500).start();
    }
    primitiveDegree+=180;

  }

  public void rotate(View view) {
    rotates(contentLeft);
    Toast.makeText(this,"left",Toast.LENGTH_SHORT).show();
  }

  public void rotate1(View view) {
    rotates(contentRight);
    Toast.makeText(this,"right",Toast.LENGTH_SHORT).show();
  }
}
