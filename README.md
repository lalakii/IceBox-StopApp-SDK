# IceBox&StopApp-SDK (kotlin实现，包含冻结、解冻，查询App状态)

冰箱、小黑屋SDK冻结、解冻应用实现

+ 冰箱 SDK，可以在已安装并启用了冰箱的设备上，为第三方 App 提供冻结/解冻的功能。

	冻结/解冻需要冰箱版本号 >= 3.6.0。
+ 小黑屋...

## 使用方法

### 依赖

添加依赖，在项目对应的文件中添加：

+ app/build.gradle.kts
```kotlin
dependencies {
    implementation("cn.lalaki:3rd-party-sdk:1.2")
}
```
也可以直接下载aar导入至项目：[IceBox&StopApp-SDK](https://github.com/lalakii/IceBox-StopApp-SDK/releases)

权限声明：
+ AndroidManifest.xml
```xml
<!--冰箱 IceBox-->
<uses-permission android:name="com.catchingnow.icebox.SDK" />

<!--小黑屋 StopApp -->
<uses-permission android:name="web1n.stopapp.permission.disable_api" />
```
## 代码示例，参考：[MainActivity.kt](sample/src/main/kotlin/cn/lalaki/demo10086/MainActivity.kt)