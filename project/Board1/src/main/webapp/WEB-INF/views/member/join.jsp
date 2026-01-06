<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, user-scalable=yes">
    <meta name="format-detection" content="telephone=no, address=no, email=no">
    <meta name="keywords" content="">
    <meta name="description" content="">
    <title>회원가입</title>
    <link rel="stylesheet" href="/project/css/reset.css"/>
    <link rel="stylesheet" href="/project/css/contents.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script>
    function goSave() {
    	let email = $("#email").val();
    	if (email == '') {
    		alert('이메일을 입력해주세요');
    		return;
    	}
    	// 이메일 중복체크
    	let con = true;
    	$.ajax({
    		url : 'emailCheck',
    		data : {email:email},
    		async : false,
    		success : function(res) {
    			if (res == '0') {
    				
    			} else {
    				alert('중복된 이메일 입니다.');
    				con = false;
    				return;
    			}
    		}
    	})
    	if (con == false) return;
    	if ($("#pwd").val() == '') {
    		alert('비밀번호를 입력하세요');
    		return;
    	}
    	// 유효성체크 추가
    	
    	// form 전송
    	$("#frm").submit();
    }
    
    function emailCheck() {
    	let email = $("#email").val();
    	if (email == '') {
    		alert('이메일을 입력해주세요');
    		return;
    	}
    	$.ajax({
    		url : 'emailCheck',
    		data : {email:email},
    		success : function(res) {
    			if (res == '0') {
    				alert('사용가능한 이메일입니다.');
    			} else {
    				alert('중복된 이메일 입니다.');
    			}
    		}
    	})
    }
    </script>
</head>
<body>
    
        <div class="sub">
            <div class="size">
                <h3 class="sub_title">회원가입</h3>
                <form name="frm" id="frm" action="join" method="post" enctype="multipart/form-data">
                <table class="board_write">
                    <caption>회원가입</caption>
                    <colgroup>
                        <col width="20%" />
                        <col width="*" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>*이메일</th>
                            <td>
                                <input type="text" name="email" id="email" class="inNextBtn" style="float:left;">
                                <span class="email_check"><a href="javascript:emailCheck();"  class="btn bgGray" style="float:left; width:auto; clear:none;">중복확인</a></span>
                            </td>
                        </tr>
                        <tr>
                            <th>*비밀번호</th>
                            <td><input type="password" name="pwd" id="pwd" style="float:left;"> <span class="ptxt">비밀번호는 숫자, 영문 조합으로 8자 이상으로 입력해주세요.</span> </td>
                        </tr>
                        <tr>
                            <th>*비밀번호<span>확인</span></th>
                            <td><input type="password" name="pwd_check" id="pwd_check" style="float:left;"></td>
                        </tr>
                        <tr>
                            <th>*이름</th>
                            <td><input type="text" name="name" id="name" style="float:left;"> </td>
                        </tr>
                        <tr>
                            <th>*성별</th>
                            <td>
                            <select name="gender" id="gender">
                            <option value="1">남성</option>
                            <option value="2">여성</option>
                            </select> 
                            </td>
                        </tr>
                        <tr>
                            <th>*생년월일</th>
                            <td><input type="text" name="birthday" id="birthday" style="float:left;"> </td>
                        </tr>
                        <tr>
                            <th>*휴대폰 번호</th>
                            <td>
                                <input type="text" name="hp" id="hp" value=""  maxlength="15" style="float:left;">
                            </td>
                        </tr>
                    </tbody>
                </table>
                        <input type="hidden" name="checkEmail" id="checkEmail" value="0"/>
                </form>
                <!-- //write--->
                <div class="btnSet clear">
                    <div><a href="javascript:;" class="btn" onclick="goSave();">가입</a> <a href="javascript:;" class="btn" onclick="history.back();">취소</a></div>
                </div>
            </div>
        </div>
        
</body>
</html>