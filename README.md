# FormKit · Android 表单组件库

[![](https://jitpack.io/v/r0it0129/FormKit.svg)](https://jitpack.io/#r0it0129/FormKit)

轻量化、易扩展的 Android 表单组件库，基于 MVVM 设计理念，支持 API 24~36，内置常用输入组件如文本输入、文本域、日期选择器，支持圆角、边框、自定义背景、只读状态、图标插入等通用 UI 控制。

---

## ✨ 特性 Features

- 🎯 统一的表单组件命名与样式管理
- 🧩 支持 `hint`、`readonly`、`required`、边框/圆角/背景控制
- 🕹️ 自定义图标、输入类型
- 🧮 TextArea 支持最大字数与字数计数器
- 📅 日期选择器支持底部滑出年月日滚轮
- 📐 所有属性均可在 XML 中动态配置

---

## 🚀 快速开始 Quick Start

### 1️⃣ 添加 JitPack 仓库

在 `settings.gradle.kts` 或 `build.gradle.kts` 中：

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // ✅ 添加这一行
    }
}


---

## 🧩 使用方式 / Usage

首先，确保已正确引入依赖：

```kotlin
dependencies {
    implementation("com.github.r0it0129:FormKit:1.0.0")
}
```

### 1. 📅 `FormDatePickerView` - 日期选择组件

#### 示例布局：

```xml
<cn.asrobot.form.components.FormDatePickerView
    android:id="@+id/formDatePicker"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:hint="选择日期"
    app:required="true"
    app:iconWidth="20dp"
    app:iconHeight="20dp"
    app:iconPadding="6dp" />
```

#### 常用方法：

```java
formDatePicker.setValue("2025-07-01");
String date = formDatePicker.getValue();
boolean valid = formDatePicker.validate();
formDatePicker.setReadonly(true);
```

---

### 2. ⬇️ `FormSinglePickerView` - 单项下拉选择器

#### 示例布局：

```xml
<cn.asrobot.form.components.FormSinglePickerView
    android:id="@+id/formPicker"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:hint="选择性别"
    app:required="true"
    app:iconWidth="20dp"
    app:iconHeight="20dp"
    app:iconPadding="6dp" />
```

#### 常用方法：

```java
formPicker.setOptions(Arrays.asList("男", "女", "其他"));
formPicker.setValue("女");
String selected = formPicker.getValue();
boolean valid = formPicker.validate();
formPicker.setReadonly(true);
```

---

### 3. 📝 `FormTextInputView` - 单行输入框（如你已有）

#### 示例布局：

```xml
<cn.asrobot.form.components.FormTextInputView
    android:id="@+id/formText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:hint="输入姓名"
    app:required="true"
    app:maxLength="50"
    app:readonly="false" />
```

#### 常用方法：

```java
formText.setValue("张三");
String text = formText.getValue();
boolean valid = formText.validate();
formText.setReadonly(true);
```

---

### 4. ✍️ `FormTextAreaView` - 多行文本输入框

#### 示例布局：

```xml
<cn.asrobot.form.components.FormTextAreaView
    android:id="@+id/formTextArea"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:hint="请输入备注"
    app:required="false"
    app:maxLength="200"
    app:showCounter="true" />
```

#### 常用方法：

```java
formTextArea.setValue("这是一段备注");
String value = formTextArea.getValue();
boolean valid = formTextArea.validate();
formTextArea.setReadonly(true);
```

---

### 可用通用属性（所有组件支持）

| 属性名                   | 类型          | 描述      |
| --------------------- | ----------- | ------- |
| `app:hint`            | `string`    | 提示文字    |
| `app:required`        | `boolean`   | 是否为必填项  |
| `app:readonly`        | `boolean`   | 是否为只读模式 |
| `app:borderColor`     | `color`     | 边框颜色    |
| `app:borderWidth`     | `dimension` | 边框宽度    |
| `app:cornerRadius`    | `dimension` | 圆角半径    |
| `app:backgroundColor` | `color`     | 背景色     |

---
