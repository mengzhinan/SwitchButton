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
    \<declare-styleable name="SwitchButtonView">
        \<!--圆角矩形与内圆的间距-->
        \<attr name="innerPaddingWidth" format="dimension|reference" />
        \<!--底纹颜色-->
        \<attr name="bgColor1" format="color|reference" />
        \<!--填充颜色-->
        \<attr name="bgColor2" format="color|reference" />
        \<!--边框粗细-->
        \<attr name="bgWidth" format="dimension|reference" />
        \<!--选中颜色-->
        \<attr name="coverColor" format="color|reference" />
        \<!--圆默认颜色-->
        \<attr name="circleDefaultColor" format="color|reference" />
        \<!--圆按下颜色-->
        \<attr name="circleSelectColor" format="color|reference" />
        \<!--是否打开-->
        \<attr name="isToggleOn" format="boolean" />
    \</declare-styleable>
