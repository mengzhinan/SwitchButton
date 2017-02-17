# SwitchButton
a simple slide layout library,just one java class file! 自定义控件，一个类的事
##先来张图找找感觉  
![](https://github.com/mengzhinan/SwitchButton/blob/master/switchbutton.gif "还不错")

##特点说明：  
* Just one class。自定义控件SwitchButtonView,属性配置attrs.xml。
* 支持点击 或者 滑动切换状态。
* 可以设定switchbutton大小，设置layout_width属性值即可，程序按比例计算出layout_height值。
* 如图，分别是指定android:layout_width="200dp"和android:layout_width="wrap_content"效果图。
* 提供可配置各种想要的颜色接口。

###attrs.xml文件：
    <declare-styleable name="SwitchButtonView">
        <!--圆角矩形与内圆的间距-->
        <attr name="innerPaddingWidth" format="dimension|reference" />
        <!--底纹颜色-->
        <attr name="bgColor1" format="color|reference" />
        <!--填充颜色-->
        <attr name="bgColor2" format="color|reference" />
        <!--边框粗细-->
        <attr name="bgWidth" format="dimension|reference" />
        <!--选中颜色-->
        <attr name="coverColor" format="color|reference" />
        <!--圆默认颜色-->
        <attr name="circleDefaultColor" format="color|reference" />
        <!--圆按下颜色-->
        <attr name="circleSelectColor" format="color|reference" />
        <!--是否打开-->
        <attr name="isToggleOn" format="boolean" />
    </declare-styleable>

###layout.xml文件
    <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout
        xmlns:dk="http://schemas.android.com/apk/res-auto"
        ...>

        <com.duke.switchbutton_test.SwitchButtonView
            android:id="@+id/myswitchbutton"
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            dk:isToggleOn="true" />
    
        <com.duke.switchbutton_test.SwitchButtonView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/myswitchbutton"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="20dp"
            dk:isToggleOn="true" />
    </RelativeLayout>

###actiivity.java代码：

    public class MainActivity extends AppCompatActivity {
        private SwitchButtonView myswitchbutton;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            myswitchbutton = (SwitchButtonView) findViewById(R.id.myswitchbutton);
            myswitchbutton.setOnToggleChangeListener(new SwitchButtonView.OnToggleChangeListener() {
                @Override
                public void onChange(boolean isToggleOn) {
                    ...
                }
            });
            //代码设置状态
            myswitchbutton.setIsToggleOn(false);
        }
    }
    
    都是自定义控件那点事，没得多说的。
    更多资料请关注[我的博客](http://blog.csdn.net/fesdgasdgasdg?viewmode=contents)
