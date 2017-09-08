<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../include/lib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>智慧农业|农业物联网|现代农业物联网登录</title>
    <meta name="keywords" content="智慧农业，农业物联网，现代农业物联网"/>
    <meta name="description" content="公司成立有技术服务部专门对客户提供现场、定制、定期等全面售后技术服务，对客户进行及时服务响应。我们承诺以“诚信经营、高效服务”为准则，为客户提供优质的服务，让每一个客户满意是我们坚持不懈的追求。我们坚持“以人为本、人尽其才”，为员工提供充分发挥才能的平台，让每一位员工有归属感，能把工作玩起来亦是我们永恒的目标。" />
    <link rel="stylesheet" href="${ctxStatic}/login/css/reset.css">
    <link rel="stylesheet" href="${ctxStatic}/login/css/login.css">
    <script src="${ctxStatic}/login/js/jquery.min.js"></script>
    <script src="${ctxStatic}/login/js/main.js"></script>
    <script src="${ctxStatic}/login/js/jquery.SuperSlide.2.1.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/login/util.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
</head>
<body>
<div class="m_login">
    <div class="m_logo"></div>
    <div class="m_bg">
        <div id="slideBox" class="slideBox">
            <div class="bd">
                <ul>
                    <li><img src="${ctxStatic}/login/image/bg.jpg" /></li>
                    <li><img src="${ctxStatic}/login/image/bg2.jpg" /></li>
                    <li><img src="${ctxStatic}/login/image/bg3.jpg" /></li>
                </ul>
            </div>
        </div>
        <script type="text/javascript">
            jQuery(".slideBox").slide({mainCell:".bd ul",autoPlay:true,delayTime:1000,effect:'fold',mouseOverStop:false});
        </script>
        <div class="m_login_detail m_login_login">
            <form name="form1" id="form1" method="post" action="">
                <div class="m_login_details clearfix">
                    <div class="m_login_title clearfix">
                        <h2 class="account">账户登录</h2>
                        <h2 class="scan">扫码登录</h2>
                    </div>
                    <div id="m_main" class="m_main">
                        <em class="triangle"></em>
                        <div class="m_login_info">
                            <div class="username">
                                <i class="user"></i>
                                <input type="text"  placeholder="用户名" validata="用户名输入错误" name="userCode" id="usercode" class="pic logininput" />
                            </div>
                            <div class="password">
                                <i class="pwd"></i>
                                <input type="password"  placeholder="密码"  validata="密码输入错误" name="userPwd" id="user_password"  class="pic logininput"/>
                            </div>
                            <div class="m_code">
                                <div class="code code_input">
                                    <i class="code_code"></i>
                                    <input type="text"  name="verifycode" validata="请输入验证码" id="verifycode"  placeholder="验证码" class="pic logincode"/>
                                </div>
                                <div class="code code_re_input">
                                    <!-- <input type="text" readonly="readonly" class="re_code" placeholder="VXNF"/> -->
                                    <i class="m_re_code"><img  id="imgcode" onclick="refreshImgCode()" alt="看不清，换一张" title="看不清，换一张" src="${ctx}/captcha"/></i>
                                    <i class="code_re_code" onclick="refreshImgCode()"></i>
                                </div>
                            </div>
                        </div>
                        <div class="error" id="errorMsg"></div>
                        <div class="forget_pwd ">忘记密码？</div>
                        <div id="loginbtn"  class="m_btn">登录</div>
                    </div>
                    <div class="m_main2">
                        <em class="triangle"></em>
                        <div class="m_main1 m_main_main">
                            <div class="m_main3">
                                <img src="${ctx}/qrCode" alt="二维码" />
                                <span class="m_phone">手机打开 <em>智慧农业</em> 扫描二维码</span>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="m_c_code clearfix">
                <div class="m_code_code">
                    <i></i>
                    <span>扫码下载APP</span>
                </div>
                <div class="m_detail">
                    <p>技术支持： 图博科技</p>
                    <p>联系电话： 17693018882</p>
                </div>
            </div>
            <div class="m_footer">Copyright &copy; 2016 图博科技 版权所有 All Rights Reserved. <br/><br/>陇ICP备16001818号</div>
        </div>
        <div class="m_state">
            <ul>
                <li><a href=""><i class="ssjk"></i><em title="智慧农业|实时监控">实时监控</em><span>实时监测空气温湿度、土壤温湿度、光照强度、流量、蒸发量、辐射量、紫外线等温室大棚关键。</span></a></li>
                <li><a href=""><i class="tt"></i><em title="智慧农业|远程控制">远程控制</em><span>手动远程控制风机、卷帘、水泵、阀门、施肥机等设备。</span></a></li>
                <li><a href=""><i class="ee"></i><em title="智慧农业|计划管理">计划管理</em><span>可根据传感器数据自动生成轮灌计划，也可手动设置轮灌计划。</span></a></li>
                <li><a href=""><i class="ss"></i><em title="智慧农业|实时数据">实时数据</em><span>具备数据采集、分析、存储及报表统计等功能</span></a></li>
            </ul>
        </div>
    </div>
</div>
<div class="m_masking">
    <div class="m_change_pwd">
        <div class="m_change_title">找回密码</div>
        <div class="m_change_close"></div>
        <div class="m_change">
            <dl>
                <dt>手 机 号：</dt>
                <dd><input type="text" placeholder="请输入手机号" /></dd>
            </dl>
            <dl>
                <dt>验  证 码：</dt>
                <dd><input type="text" placeholder="短信验证码 " class="m_change_code"/></dd>
                <dd><a href="javascript:;">获取短信验证码</a></dd>
            </dl>
            <dl>
                <dt>新  密 码：</dt>
                <dd><input type="text" placeholder="输入密码" /></dd>
            </dl>
            <dl>
                <dt>确认密码：</dt>
                <dd><input type="text" placeholder="输入新密码 " /></dd>
            </dl>
            <dl>
                <dd><button class="m_change_btn">确认</button></dd>
            </dl>
        </div>
    </div>
</div>
<script type="text/javascript">
    var login = {
        validate:function(obj){
            if ($(obj).val() === "") {
                $("#errorMsg").html($(obj).attr("validata"));
                return false;
            }
            else{
                $("#errorMsg").html('');
                return true;
            }
        },
        login:function(){
            if($("#loginbtn").attr("disabled") || $("#loginbtn").attr("disabled") == "disabled")return;
            var html = $("#loginbtn").html();
            var k = 0;
            $(".logininput,.logincode").each(function() {
                if(!login.validate(this)){
                    k++;
                }
            });
            if (k > 0)
                return;

            $("#loginbtn").html("登陆中...");
            $("#loginbtn").attr("disabled", true);
            var url = '${ctx}/login';
            util.post(url,$("#form1").serialize(),function(data){
                if(!data.code){
                    window.location ='${ctx}/index';
                    return;
                }else{
                    layer.msg(data.msg || '登录失败', {
                        icon : 5,
                        skin : 'layer-ext-moon',
                        time : 1500
                    }, function(index) {
                        layer.close(index);
                    })
                    $("#loginbtn").html(html);
                    $("#imgcode").trigger("click");//换取验证码
                    $("#loginbtn").attr("disabled", false);
                }
                console.log(data);
            });
        }
    };
    $(document).ready(function() {

        $(".logininput,.logincode").blur(function() {
            login.validate(this);
        });
        $("#loginbtn").click(function() {
            login.login();
        });
    });
    document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){ // enter 键
            login.login();
        }
    }
    function refreshImgCode() {
        $("#imgcode").attr("src",'${ctx}/captcha?_='+(new Date().getTime()));
    }
    qrInterval=setInterval(function () {
        $.ajax({
            url:"${ctx}/checkQrCode",
            async:false,
            success:function (res) {
                if (!res.code){
                    window.location ='${ctx}/index';
                    clearInterval(qrInterval);
                    return;
                }
            }
        })
    },2000);
</script>
</body>
</html>