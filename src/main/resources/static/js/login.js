$('.select__label').click(function () {
    $('.select__label').removeClass('select__label--active');
    $(this).addClass('select__label--active');
});

$('#js-usr-rtn').click(function () {
    $('#js-btn, .pointer, #js-field__pass').removeClass('--usr-new --usr-rst ui-field--hidden');
    $('#js-btn, .pointer').addClass('--usr-rtn');
    $('#js-field__r-pass').addClass('ui-field--hidden');
});
$('#js-usr-new').click(function () {
    $('#js-btn, .pointer, #js-field__r-pass, #js-field__pass').removeClass('--usr-rtn --usr-rst ui-field--hidden');
    $('#js-btn').addClass('--usr-new');
});
$('#js-usr-rst').click(function () {
    $('#js-btn, .pointer').removeClass('--usr-rtn --usr-new');
    $('#js-btn, .pointer').addClass('--usr-rst');
    $('#js-field__r-pass, #js-field__pass').addClass('ui-field--hidden');
});

//修剪空格
String.prototype.trim = function () {
    return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
}

// 判断字符串是否为空,空格
function isNull(str) {
    if (str == "") return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}

var flag = false;

// 邮箱正则
function checkEmail() {
    var flag = false;
    var email = $("#js-field__email").val();
    var reg = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+(\.[a-zA-Z]{2,3})+$/;
    if (email != "") {
        // 不正确就提示
        if (!reg.test(email)) {
            document.getElementById("hint").innerHTML = " 请输入正确的邮箱格式!";
            document.getElementById("hint").style.color = "red";
        }else {
            flag = true;
        }
    }
    else {
        document.getElementById("hint").innerHTML = " 请输入邮箱!";
        document.getElementById("hint").style.color = "red";
    }
    return flag;
}

$("#js-btn").click(function () {
    var data = {};
    /*获取js-btn的样式*/
    var name = $("#js-btn").attr('class');
    var url = "";
    /*如果是登陆*/
    if (name.indexOf("rtn") > 0) {
        var userEmail = $("#js-field__email").val();
        var userPass = $("#js-field__pass").val();
        if (!isNull(userEmail) && !isNull(userPass)) {
            if (checkEmail()) {
                flag = true;
                data.userEmail = userEmail;
                data.userPass = userPass;
                url = "/user/login";
            }
        }else {
            document.getElementById("hint").innerHTML = "邮箱或密码不能为空!";
            document.getElementById("hint").style.color = "red";
        }

        /*注册*/
    } else if (name.indexOf("new") > 0) {
        var userEmail = $("#js-field__email").val();
        var userPass = $("#js-field__pass").val();
        var userRepass = $("#js-field__r-pass").val();
        if (checkEmail()) {
            if (!isNull(userPass) && !isNull(userRepass) && userPass === userRepass) {
                flag = true;
                data.userEmail = $("#js-field__email").val();
                data.userPass = $("#js-field__pass").val();
                data.userRepass = $("#js-field__r-pass").val();
                url = "/user/register";
            } else {
                document.getElementById("hint").innerHTML = "请核对密码!";
            }
        }

        /*重置*/
    } else if (name.indexOf("rst") > 0) {
        var userEmail = $("#js-field__email").val();
        if (checkEmail()) {
            data.userEmail = userEmail;
            url = "/user/forget";
        }
    }
    if (flag) {
        document.getElementById("hint").innerHTML = "";
        submit(data, url);
    }
    // 阻止页面莫名的自动刷新
    window.event.returnValue = false;
});

function submit(data, url) {
    $.ajax({
        cache: !1,
        type: "POST",
        url: url,
        data: data,
        async: !0,
        beforeSend: function () {
            $("#js-btn").html("提交中..."), $("#js-btn").attr("disabled", "disabled")
        },
        success: function (data) {
            var result = jQuery.parseJSON(data);
            // 比较result和成功连接是否相等
            if ("/static/success.html" === (result.message)) {
                location.href = result.message;
            } else {
                layer.alert(result.message);
            }
        },
        complete: function () {
            $("#js-btn").html("success"), $("#js-btn").removeAttr("disabled")
        }
    })
}