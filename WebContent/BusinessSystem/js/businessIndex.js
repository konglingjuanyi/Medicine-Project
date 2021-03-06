﻿angular.module("doctor_app", ["doctor_app_header"]).run(function () {
    if (document.cookie == "") { alert("用户未登录！"); location.href = "./businessLogin.html"; }
    resize();
}).controller('my', ['$scope', function (scope) {
    scope.pageContent = 'modules/content/index_resourceManagement.html';
    scope.searchKeyword="药品名称/分类名称";
	scope.resourceManagement_seach=function(){ResourceMangement_search(1);};
    scope.sidebarClick = function (value, pageName) {
        $(".aside_sidebar li").removeClass("sidebar_click");
        $(".aside_sidebar li:nth-child(" + value + ")").addClass("sidebar_click");
        $(".aside_sidebar li .aside_sidebar_icon").removeClass("aside_sidebar_click");
        $(".aside_sidebar li:nth-child(" + value + ") .aside_sidebar_icon").addClass("aside_sidebar_click");
        scope.pageContent = pageName;
        if (value == 1) {
            scope.resourceMangementPageHtml = 'modules/content/index_modules/button/resourceManagement_drugListButton.html';
            scope.resourceManagementList = 'modules/content/index_modules/list/resourceManagement_drugList.html';
        }
    };
    scope.changeInformatinFunction=function(){
    	scope.pageContent='modules/content/index_changeInformation.html';
    	close_header_userFunction();
    };
    scope.resourceMangementPageHtml = 'modules/content/index_modules/button/resourceManagement_drugListButton.html';
    scope.resourceManagementList = 'modules/content/index_modules/list/resourceManagement_drugList.html';
    scope.resourceManagementPageClick = function (value, pageButton, pageList) {//页面加载  1：药品页面 2：会议页面 3：视频页面
        $(".resourceContent_nav li").removeClass("content_nav_onclick").addClass("bg_color");
        $(".resourceContent_nav li:nth-child(" + value + ")").addClass("content_nav_onclick").removeClass("bg_color");
        scope.resourceMangementPageHtml = pageButton;
        scope.resourceManagementList = pageList;
        if(value==1){
        	scope.searchKeyword="药品名称/分类名称";
        }else if(value==2){
        	scope.searchKeyword="会议名称/科目名称";
        }else if(value==3){
        	scope.searchKeyword="视频名称/科目名称";       	
        }
        scope.resourceManagement_seach=function(){ResourceMangement_search(value);};
    };
    /*
    资源管理添加按钮------isAdd取值
    0：取消显示添加页面
    1：药品列表添加
    2：会议列表添加
    3：视频列表添加
    */
    scope.addDrugForm = "";
    scope.addMeetingForm = "";
    scope.addVideoForm="";
    scope.resoure_add = function (isAdd) {
    	var addParentUrl="modules/content/index_modules/add/";
        switch (isAdd) {
            case 0: {
                scope.addDrugForm = "";
                scope.addMeetingForm = "";
                scope.addVideoForm="";
                break;
            }
            case 1: { 
            	scope.addDrugForm =addParentUrl+ 'resourceManagement_addDrugList.html'; 
            	break; 
            }
            case 2: {
                scope.addMeetingForm =addParentUrl+ 'resourceManagement_addMeeting.html';
                break;
            }
            case 3:{
                scope.addVideoForm = addParentUrl+'resourceManagement_addVideo.html';
                break;
            }
        }
    };
}]);
window.onresize = function () {
    resize();
    $(".content_content_content").css({
        "height": window.innerHeight - 290 + "px"
    });
};
function resize() {
    $("aside").css({
        "height": window.innerHeight - 55 + "px"
    });
    $("body").css({
        "width": window.innerWidth
    });
    $(".index_content").css({
        "width": window.innerWidth - 280 + "px"
    });
    if (window.innerWidth < 1230) {
        $("body").css({
            "width": "1230px"
        });
    } else {
        $(".content_footer").css({
            "width": window.innerWidth - 280 + "px"
        });
    }
}
