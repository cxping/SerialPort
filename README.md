### 说明
一个串口调试库,处于简单可用状态# SerialPort [![](https://www.jitpack.io/v/cxping/SerialPort.svg)](https://www.jitpack.io/#cxping/SerialPort)
### 步骤 1.将 JitPack 存储库添加到您的构建文件
```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
###  步骤 2.添加依赖项
```
	dependencies {
	        implementation 'com.github.cxping:SerialPort:Tag'
	}
```
### jdk 11
```DSL
compileOptions {
sourceCompatibility JavaVersion.VERSION_11
targetCompatibility JavaVersion.VERSION_11
}

    // For Kotlin projects
    kotlinOptions {
      jvmTarget = "11"
    }

```
### 关于日志输出问题
```Java
    VLogUtil.isDebug= false；//修改此静态变量即可关闭日志输出
```
