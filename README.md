# 📱 Test1Clock（智能时钟应用）

Test1Clock 是一个功能丰富、界面美观的 Android 时钟应用，集成了翻转数字时钟显示、闹钟管理、秒表、天气显示等功能。支持 Kotlin 编写和 Room 数据库存储。

## 🌟 功能亮点

* ⏰ **翻转时钟显示**：采用数字7字体，支持小时、分钟、秒的翻转切换。
* 🗓️ **闹钟管理**：可添加、编辑、删除闹钟，支持设置重复周期和开关控制。
* ⏲️ **秒表功能**：简洁秒表界面，适合日常计时使用。
* ☁️ **天气显示**：通过调用 OpenWeatherMap API 显示实时天气信息（支持中文描述与图标显示）。
* 🎨 **美观界面**：整体采用深色背景与绿色点缀，布局简洁现代。

## 🔧 环境要求

* Android Studio Arctic Fox 或更高版本
* Android SDK 24+
* Kotlin 1.5+
* Room 数据库
* 协程（Coroutine）
* 网络权限（用于天气功能）

## 🚀 运行方式

1. 克隆代码到本地：

   ```bash
   git clone https://github.com/yourusername/Test1Clock.git
   ```

2. 打开 Android Studio，选择 **Open Project**，定位到项目目录。

3. 确保 `AndroidManifest.xml` 包含网络权限：

   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

4. 替换 `MainActivity.kt` 中的 `apiKey` 和 `city` 为你的 OpenWeatherMap 有效 API Key 和目标城市：

   ```kotlin
   val apiKey = "你的APIKEY"
   val city = "北京"
   ```

5. 点击 **Run**（绿色三角按钮）运行应用。

## 📚 主要文件结构

```
Test1Clock/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/test1clock/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── AlarmActivity.kt
│   │   │   │   ├── EditAlarmActivity.kt
│   │   │   │   ├── ClockActivity.kt
│   │   │   │   ├── adapter/AlarmAdapter.kt
│   │   │   │   └── model/AlarmEntity.kt, AlarmDao.kt, AlarmDatabase.kt
│   │   │   └── res/layout/
│   │   │       ├── activity_main.xml
│   │   │       ├── activity_alarm.xml
│   │   │       ├── activity_edit_alarm.xml
│   │   │       ├── activity_clock.xml
│   │   │       └── item_alarm.xml
│   │   │   └── res/drawable/（天气图标资源）
```

## 🏗️ 构建与依赖

* Room：用于本地闹钟数据存储
* Kotlin 协程：处理异步任务和网络请求
* OpenWeatherMap API：获取天气数据
* 自定义字体（Digital7）：美观的数字时钟显示

## 🔮 TODO（未来计划）

* 支持 **自动定位** 获取当地天气
* 提供 **闹钟响铃** 与通知提醒功能
* 添加 **更多主题样式**，如浅色模式
* 集成 **AI 智能推荐起床时间**

## 📄 License

MIT License.
