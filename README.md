# FormKit · Android 表单组件库

[![](https://jitpack.io/v/asrobot/FormKit.svg)](https://jitpack.io/#asrobot/FormKit)

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
