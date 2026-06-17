# onakawash-backend

语言：[English](./README.md) | [中文](./README.zh-CN.md)

## 项目介绍

onakawash-backend 是 **onakawash** 项目的后端服务。

onakawash 是一个面向日语初学者的五十音学习网站。

这个后端项目使用 **Java Spring Boot** 开发，目前主要负责向前端提供假名数据，并使用 H2 数据库进行本地开发。

项目仍在开发中。

## 当前功能

* Spring Boot 后端项目
* 提供五十音数据接口
* Controller / Service / Repository 分层结构
* 使用 H2 数据库进行本地开发
* 使用 `data.sql` 存放初始化数据
* 平假名和片假名数据都存放在数据库中
* 支持前端通过 CORS 访问后端接口

## 当前数据流

```text
Frontend → Controller → Service → Repository → Database
```

前端向后端发送请求。

后端从数据库读取假名数据，然后以 JSON 格式返回给前端。

当前假名接口：

```text
GET /hiragana  -> 从 kana_items 读取 HIRAGANA 记录
GET /katakana  -> 从 kana_items 读取 KATAKANA 记录
```

这两个接口都会返回按分类整理好的 JSON 数据，供前端页面渲染。

## 技术栈

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Maven

## 数据库说明

当前项目使用 **H2** 作为本地开发数据库。

H2 主要用于学习、测试和本地开发。它不是项目最终上线时计划使用的正式数据库。

当前初始化数据文件位置：

```text
src/main/resources/data.sql
```

目前，`data.sql` 是假名数据的主要来源。

前端旧数据文件、Controller 中曾经写死的数据、Service 中曾经手写的数据，都属于历史版本数据，不再作为当前主数据来源。

假名内容、音频路径、图片路径、分类和显示顺序应在 `data.sql` 中维护。

未来项目如果上线，可能会切换到正式数据库，例如：

* PostgreSQL
* MySQL
* MariaDB

未来更推荐的结构不是同时使用多个数据库，而是在一个正式数据库中使用多张表，例如：

```text
kana_items
example_sentences
users
study_records
```

## API 接口

当前或计划中的接口：

```text
GET /hiragana
GET /katakana
```

这些接口会返回按照分类整理好的假名数据。

示例分类：

```text
Basic
Dakuten / Han-dakuten
Combination
```

## 本地开发

本项目是前后端分离项目，由两个仓库组成。

前端仓库：

```text
onakawash
```

后端仓库：

```text
onakawash-backend
```

本地开发时：

* 前端运行在 `localhost:3000`
* 后端运行在 `localhost:8080`

## 未来计划

* 完成平假名和片假名数据库记录
* 添加例句数据
* 添加用户注册和登录功能
* 添加用户学习记录功能
* 未来切换到正式数据库
* 准备项目部署

## 本地开发方式

当前后端项目主要使用 **VS Code** 进行编辑，使用 **PowerShell** 运行 Spring Boot。

由于 IntelliJ IDEA 在当前环境中出现闪退问题，暂时不使用 IDEA 运行或编辑后端项目。

当前推荐工作流：

```text
VS Code      编辑 Java / SQL / README 文件
PowerShell   运行 Spring Boot 后端
PowerShell   运行 Next.js 前端
PowerShell   执行 Git 命令
```

## 后端运行方式

打开 PowerShell，进入后端仓库：

```powershell
cd D:\Code\onakawash-backend
```

运行 Spring Boot：

```powershell
.\mvnw.cmd spring-boot:run
```

后端启动成功后，控制台会出现类似信息：

```text
Tomcat started on port 8080
Started OnakawashBackendApplication
```

后端接口示例：

```text
http://localhost:8080/hiragana
http://localhost:8080/katakana
```

注意：运行 Spring Boot 的 PowerShell 窗口会被后端进程占用。
如果需要执行 Git 命令或其他命令，应重新打开一个 PowerShell 窗口。

停止后端：

```text
Ctrl + C
```

如果出现确认提示，输入：

```text
Y
```

然后回车。

## 前端运行方式

打开新的 PowerShell，进入前端仓库：

```powershell
cd D:\Code\onakawash
```

运行前端：

```powershell
npm run dev
```

前端页面地址：

```text
http://localhost:3000
```

## Git 操作

建议单独打开一个 PowerShell 执行 Git 命令。

进入后端仓库：

```powershell
cd D:\Code\onakawash-backend
```

查看当前修改：

```powershell
git status
```

提交修改示例：

```powershell
git add .
git commit -m "feat: add hiragana database sections"
git push
```

## 开发注意事项

* 当前不要使用 IDEA 右上角运行按钮启动 Spring Boot。
* 如果修改了 `data.sql` 或 Java 代码，需要重启后端才能看到变化。
* H2 数据库当前用于本地开发，数据会在 Spring Boot 启动时通过 `data.sql` 初始化。
* 如果出现 `Port 8080 already in use`，说明后端已经在运行，需要先停止旧的后端进程。
* 如果出现 `hs_err_pid*.log` 文件，它是 Java / JVM 崩溃日志，不属于项目代码，不应该提交到 Git。


## 作者

马艺源 （Yvonne Buttercup）
