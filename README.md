# Shiba-Inu-Permission
一个使用AOP思想的Android动态请求框架


## 代码中使用




在需要申请权限得函数上添加注解如定位权限:
``` kontlin
@Permission(Manifest.permission.ACCESS_FINE_LOCATION)//写上需要申请的权限一个或若干用，隔开即可

private fun positioning() {
        //开始你得定位代码
    }

```
用户同意后会执行 positioning()函数里面，开始执行你写得定位代码。同样有同意，就有拒绝。

```kontlin
@PermissionCanceled //点击取消执行这个函数
private fun cancel() {
        showToast("你拒绝了这个权限")
    }


@PermissionDenied//点击取消和不在提醒 执行这个函数 注意 这个函数执行后 会自动跳转到手机系统设置权限得页面
private fun denied() {
        showToast("没有这个权限手机无法正常使用")
    }


```
 
