//  ----------------------------------------------------------------------
//  Copyright (C) 2017  即时通讯网(52im.net) & Jack Jiang.
//  The MobileIMSDK_X (MobileIMSDK v3.x) Project.
//  All rights reserved.
//
//  > Github地址: https://github.com/JackJiang2011/MobileIMSDK
//  > 文档地址: http://www.52im.net/forum-89-1.html
//  > 即时通讯技术社区：http://www.52im.net/
//  > 即时通讯技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
//
//  "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
//
//  如需联系作者，请发邮件至 jack.jiang@52im.net 或 jb2011@163.com.
//  ----------------------------------------------------------------------
//
//  LoginViewController.m
//  MibileIMSDK4iDemo_X (A demo for MobileIMSDK v3.0 at Summer 2017)
//
//  Created by JackJiang on 15/11/8.
//  Copyright © 2017年 52im.net. All rights reserved.
//

#import "LoginViewController.h"
#import "AppDelegate.h"
#import "UIViewController+Ext.h"
#import "ConfigEntity.h"
#import "ClientCoreSDK.h"
#import "ChatBaseEventImpl.h"
#import "LocalUDPDataSender.h"
#import "OnLoginProgress.h"
#import "IMClientManager.h"


////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - 私有API
////////////////////////////////////////////////////////////////////////////////////////////

@interface LoginViewController ()

/* 登陆进度提示 */
@property (nonatomic) OnLoginProgress *onLoginProgress;

/* 收到服务端的登陆完成反馈时要通知的观察者（因登陆是异步实现，本观察者将由
 *  ChatBaseEvent 事件的处理者在收到服务端的登陆反馈后通知之）*/
@property (nonatomic, copy) ObserverCompletion onLoginSucessObserver;// block代码块一定要用copy属性，否则报错！

@end


/////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - 本类的代码实现
/////////////////////////////////////////////////////////////////////////////////////////////

@implementation LoginViewController

@synthesize onLoginProgress;

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // 界面UI基本设置
    [self initForGUI];
    // 登陆有关的初始化工作
    [self initForLogin];
}

- (void)initForGUI
{
    // 设置顶部导航标题栏为不透明，否则在ios7以上系统会挡住下方的页面内容
    self.navigationController.navigationBar.translucent = NO;
    self.title = @"MobileIMSDK_X Demo登陆";
    
    // 显示Demo程序的版本号
    NSBundle *mainBundle = [NSBundle mainBundle];
    self.versionView.text = [NSString stringWithFormat:@"%@%@"
            , [[mainBundle infoDictionary] objectForKey:@"CFBundleShortVersionString"]
            , [[mainBundle infoDictionary] objectForKey:@"CFBundleVersion"]];
}

- (void)initForLogin
{
    // 为了在block代码中安全地使用本类“self”，请在block代码中使用safeSelf
    __weak LoginViewController *safeSelf = self;
    // 实例化登陆进度提示封装类
    self.onLoginProgress = [[OnLoginProgress alloc] init];
    // 设置登陆超时回调（将在登陆进度提示封装类中使用）
    [self.onLoginProgress setOnLoginTimeoutObserver:^(id observerble ,id data) {
        [[[UIAlertView alloc] initWithTitle:@"超时了"
                                    message:@"登陆超时，可能是网络故障或服务器无法连接，是否重试？"
                                   delegate:safeSelf
                          cancelButtonTitle:@"取消"
                          otherButtonTitles:@"重试！", nil]
         show];
    }];
    // 准备好异步登陆结果回调block（将在登陆方法中使用）
    self.onLoginSucessObserver = ^(id observerble ,id data) {
        // * 已收到服务端登陆反馈则当然应立即取消显示登陆进度条
        [safeSelf.onLoginProgress showProgressing:NO onParent:safeSelf.view];
        // 服务端返回的登陆结果值
        int code = [(NSNumber *)data intValue];
        // 登陆成功
        if(code == 0)
        {
            // TODO 提示：登陆MobileIMSDK服务器成功后的事情在此实现即可
            // TODO 提示：登陆MobileIMSDK服务器成功后的事情在此实现即可
            // TODO 提示：登陆MobileIMSDK服务器成功后的事情在此实现即可
            // TODO 提示：登陆MobileIMSDK服务器成功后的事情在此实现即可
            // TODO 提示：登陆MobileIMSDK服务器成功后的事情在此实现即可
            
            // 进入主界面
            [CurAppDelegate switchToMainViewController];
        }
        // 登陆失败
        else
        {
            [[[UIAlertView alloc] initWithTitle:@"友情提示"
                                        message:[NSString stringWithFormat:@"Sorry，登陆失败，错误码=%d", code]
                                       delegate:safeSelf
                              cancelButtonTitle:@"知道了"
                              otherButtonTitles:nil]
             show];
        }
        
        //## try to bug FIX ! 20160810：此observer本身执行完成才设置为nil，解决之前过早被nil而导致有时怎么也无法跳过登陆界面的问题
        // * 取消设置好服务端反馈的登陆结果观察者（当客户端收到服务端反馈过来的登陆消息时将被通知）【1】
        [[[IMClientManager sharedInstance] getBaseEventListener] setLoginOkForLaunchObserver:nil];
    };
}

- (IBAction)signIn:(id)sender
{
    [self doLogin];
}

/*
 * 登陆处理。
 *
 * @see doLoginImpl:
 */
- (void)doLogin
{
    //** 设置服务器地址和端口号
    NSString *serverIP = [self.addrField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString *serverPort = self.portField.text;
    //	int serverPort = [self.portField.text intValue];;
    if(!([serverIP length] <= 0)
       && !([serverPort length] <= 0))
    {
        // 设置好服务端的连接地址
        [ConfigEntity setServerIp:serverIP];
        // 设置好服务端的UDP监听端口号
        [ConfigEntity setServerPort:[serverPort intValue]];
    }
    else
    {
        [self E_showToastInfo:@"提示" withContent:@"请确保服务端地址和端口号都不为空！" onParent:self.view];
        //        [self showIMInfo_red: @"请确保服务端地址和端口号都不为空！"];
        return;
    }
    
    //** 登陆名和密码
    NSString *loginUserIdStr = self.loginName.text;
    if ([loginUserIdStr length] == 0)
    {
        [self E_showToastInfo:@"提示" withContent:@"请输入登陆名！" onParent:self.view];
        return;
    }
    NSString *loginTokenStr = self.loginPsw.text;
    if ([loginTokenStr length] == 0)
    {
        [self E_showToastInfo:@"提示" withContent:@"请输入登密码！" onParent:self.view];
        return;
    }
    
    //** 向服务端发送登陆信息
    [self doLoginImpl:loginUserIdStr withToken:loginTokenStr];
}

/*
 * 真正的登陆信息发送实现方法。
 */
- (void)doLoginImpl:(NSString *)loginUserIdStr withToken:(NSString *)loginTokenStr
{
    // * 立即显示登陆处理进度提示（并将同时启动超时检查线程）
    [self.onLoginProgress showProgressing:YES onParent:self.view];
    // * 设置好服务端反馈的登陆结果观察者（当客户端收到服务端反馈过来的登陆消息时将被通知）【2】
    [[[IMClientManager sharedInstance] getBaseEventListener] setLoginOkForLaunchObserver:self.onLoginSucessObserver];
    
    // * 发送登陆数据包(提交登陆名和密码)
    int code = [[LocalUDPDataSender sharedInstance] sendLogin:loginUserIdStr withToken:loginTokenStr];
    if(code == COMMON_CODE_OK)
    {
        [self E_showToastInfo:@"提示" withContent:@"登陆请求已发出。。。" onParent:self.view];
    }
    else
    {
        NSString *msg = [NSString stringWithFormat:@"登陆请求发送失败，错误码：%d", code];
        [self E_showToastInfo:@"错误" withContent:msg onParent:self.view];
        
        // * 登陆信息没有成功发出时当然无条件取消显示登陆进度条
        [self.onLoginProgress showProgressing:NO onParent:self.view];
    }
}


#pragma mark - UIAlertView delegate

/* 
 * 在这里处理登陆超时时的UIAlertView提示对话框中的按钮被单击事件。
 */
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex)
    {
        // 点击了取消按钮
        case 0:
            // 不需要重试则要停止“登陆中”的进度提示哦
            [self.onLoginProgress showProgressing:NO onParent:self.view];
            break;
            
        // 点确了确认按钮
        case 1:
            // 确认要重试时（再次尝试登陆哦）
            [self doLogin];
            break;
            
        default:
            break;
    }
}

@end
