# 高德Demo
> 重庆邮电大学 红岩网校移动开发部 大一寒假作业

根据高德地图官网提供的api 实现高德部分功能
## 演示预览

## 目录
* [安装](#展示帮助信息)	
* [使用方法](#使用方法)	
* [发展](#发展)
* [感想](#感想)

## 安装

首先安装mapdemo

然后配置环境 将手机中的定位权限打开

```sh
找到手机中的设置
```
![.](https://github.com/ysh1356542512/redrock_winter_homework/blob/master/install_1.png) 
```sh
安全与隐私
```
![.](https://github.com/ysh1356542512/redrock_winter_homework/blob/master/install_2.png) 
```sh
定位服务
```
![.](https://github.com/ysh1356542512/redrock_winter_homework/blob/master/install_3.png)
```sh
找到mapDemo
```
![.](https://github.com/ysh1356542512/redrock_winter_homework/blob/master/install_4.png) 
```sh
允许权限
```
![.](https://github.com/ysh1356542512/redrock_winter_homework/blob/master/install_5.png) 

## 使用方法

## 发展
### 一、创建应用
```sh
第一步往往比较关键 我为了得到这个key还有把这个key给放进项目里 真的花了很多时间 重装jdk 配置环境变量 还有注册表 被迫学习cmd
```
  首先，我们需要一个key，类似于获得使用高德api的资格，因此要去官网注册，点击[高德地图官网](https://developer.amap.com/)进入
![](https://github.com/ysh1356542512/redrock_winter_homework/blob/main/show_1.png)
  点击右上角头像进入应用管理 点击我的应用，创建新应用
![](https://github.com/ysh1356542512/redrock_winter_homework/blob/main/show_3.png)
  新建应用
![](https://github.com/ysh1356542512/redrock_winter_homework/blob/main/show_4.png)
点击添加key
![](https://github.com/ysh1356542512/redrock_winter_homework/blob/main/show_5.png)
你会需要三个值，发布版安全码SHA1、调试版安全码SHA1、PackageName。
#### ①获得packageName
这个在你的android工程中可以找到 具体如下
picture1
#### ②获得调试版安全码
具体的方法有很多种 我就讲我用的吧
* 按window+R  输入cmd 进入控制台
  picture2
* 在弹出的控制台窗口中输入 cd .android 定位到 .android 文件夹
* 调试版本使用 debug.keystore，命令为：keytool -list -v -keystore debug.keystore 发布版本使用 apk 对应的 keystore，命令为：keytool -list -v -keystore apk的keystore 如下所示：
* 提示输入密钥库密码，调试版本默认密码是 android，发布版本的密码是为 apk 的 keystore 设置的密码。输入密钥后回车（如果没设置密码，可直接回车），此时可在控制台显示的信息中获取 Sha1 值，如下图所示：
#### ③获得发布版安全码SHA1
  和调试版本不一样的是 发布版还需要一个jks文件 需要在android工程里新建
 * 点击Build 选择点击Generate Signed Bundle / APK…
 * 选择APK，然后Next
 * 然后是配置，这里需要填写jks的路径，但是我没有这个jks，因此点击**Create new,**按钮去创建一个。
 * 首先要指定这个jks的文件存放路径和文件名。
 * 这里我存放在D盘下的APK文件夹中，然后设置jks的名字为GaodeMapDemo，然后点击OK。
 * 会弹出这样一个窗口，不用管它，点击OK。
 * 勾选记住密码，然后点击Next。
 * 选择release，然后两个都勾选上，最后点击Finish。
 * 在你的AS中查看这个apk，你可以复制它通过电脑QQ发给你的手机，然后在手机上直接打开安装
 * 然后点击确认后 会显示你的key
#### ④关于bug和可能遇到的错误
  大部分都是我自己实操的时候遇到的错误 还有我自己去网上找解决方法 最终还是自己赌一把给搞定了 hai 具体的等我以后再写 现在先把readme
  写完
### 二、配置Android Studio工程
```sh
定位服务等等 工作前的铺垫
```
#### ①导入SDK
首先要下载SDK，点击[SDK下载](https://developer.amap.com/api/android-location-sdk/download)
复制这些文件到你的libs下。然后点击这个小象图标进行工程的资源配置同步。
最终如下图所示，你可以看到你的这个jar现在是可以打开的。
然后打开你的app下的build.gradle文件，在android闭包下添加
#### ② 配置AndroidManifest.xml
打开AndroidManifest.xml，首先在application标签下添加定位服务
然后添加在manifest标签下添加如下权限。
最后在application标签下添加高德的访问key
这个值和你创建的key的值一致
### 三、获取当前定位信息
```sh
获取当前定位很有用 比如有一个功能是点按钮回到我的位置 就需要 以及路线规划的起点等等 还有可以通过定位得到很多信息 citycode等等
```
#### ① 动态权限请求
打开app下的build.gradle，在dependencies闭包下添加如下依赖：
#### ② 初始化定位
首先在newActivity中新增两个成员变量
然后新增一个initLocation()方法
#### ③ 获取定位结果
### 四、显示地图
```sh
显示地图就不用说了 要给用户看得嘛
```
添加mapview
<com.amap.api.maps.MapView
        android:id="@+id/map_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
        
 然后增加地图生命周期的管理方法。
### 五、显示当前定位地图
```sh
将定位结果和地图结合
```
现在newActivity中新增两个成员变量
然后新增一个initMap()方法，用于初始化地图
然后重写里面的两个方法在activate()和deactivate()。
### 六、地图设置
主要是设置一些自定义图标 比例尺 缩放比例
### 七、获取POI数据
```sh
是实现搜索功能的关键 通过给edittext设置监听器 来得到关键词 再通过poi和recyclerview实现poi搜索
```
实现poi搜索
首先先在app的build.gradle中添加依赖
然后添加一个queryPOI()方法，这个方法对应了xml中浮动按钮的onClick的值。
下面就要实现PoiSearch.OnPoiSearchListener
然后重写里面的onPoiSearched和onPoiItemSearched，方法如下
### 八、地理编码和逆地理编码
```sh
逆地理编码 让用户得到目的地的具体信息 比如省份等等
地理编码 与poi搜索结合 实现搜索并定位
```
#### ①逆地理编码
逆地理编码就是将坐标转为地址，坐标刚才已经拿到了，就是经纬度
首先在newActivity中创建两个对象。
然后在initMap()中构建对象，然后设置监听。之后实现这个GeocodeSearch.OnGeocodeSearchListener
重写里面的两个方法。一个是地址转坐标，一个是坐标转地址
通过经纬度构建LatLonPoint对象，然后构建RegeocodeQuery时，传入，并且输入另外两个参数，范围和坐标系。最后通过geocodeSearch发起一个异步的地址获取请求。
#### ② 地理编码
同理 进入到onGeocodeSearched方法
### 九、marker和改变中心点
```sh
marker 让用户知道目的地在地图上的具体位置
```
#### ① 添加标点Marker
就是一段代码
#### ② 删除标点Marker
也是一段代码
#### ③ 花里胡哨的动画效果
在addMarker方法中，添加如下代码 第一个参数就是转多少度
#### ④ 改变中心点
改变中心点 我们还要用到camera 
还有一个动画移动 优化用户移动地图的视觉体验
### 十、出行规划
```sh
出行规划 让用户知道如何到达目的地
```
这个功能需要我们提供两个点 起点和终点 剩下的就交给官方的接口了 hhh
