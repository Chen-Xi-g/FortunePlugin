# 组件化开发插件

![Build](https://github.com/Chen-Xi-g/FortunePlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

## 功能说明

- [x] 一键创建`Activity/Fragment`
- [x] 一键创建`ListActivity/ListFragment`
- [x] 一键创建`ViewModel`
- [x] 一键创建`XML`布局
- [x] 一键添加创建的`Activity`到`AndroidManifest`中

<!-- Plugin description -->
配合`AndroidLibrary`组件化工程, 快速创建Activity/Fragment/ViewModel, 减少重复操作, 加快开发流程.

## 使用说明

1. 创建Activity: 在需要生成文件的目录点击右键, 选择 <kbd>New</kbd> > <kbd>Activity</kbd> > <kbd>Android MVVM Activity</kbd>
2. 创建Fragment: 在需要生成文件的目录点击右键, 选择 <kbd>New</kbd> > <kbd>Fragment</kbd> > <kbd>Android MVVM Fragment</kbd>
3. 创建ViewPager2: 在需要生成文件的目录点击右键, 选择 <kbd>New</kbd> > <kbd>Other</kbd> > <kbd>Android MVVM ViewPager2</kbd>

<!-- Plugin description end -->

## Installation

- ~~Using the IDE built-in plugin system:~~

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "FortunePlugin"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/Chen-Xi-g/FortunePlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation