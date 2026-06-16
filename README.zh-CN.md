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
* 平假名数据已开始存入数据库
* 支持前端通过 CORS 访问后端接口

## 当前数据流

```text
Frontend → Controller → Service → Repository → Database
```

前端向后端发送请求。

后端从数据库读取假名数据，然后以 JSON 格式返回给前端。

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

## 作者

Yvonne Buttercup
