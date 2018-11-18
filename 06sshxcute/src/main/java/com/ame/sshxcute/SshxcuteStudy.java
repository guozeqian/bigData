package com.ame.sshxcute;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.core.SSHExecUtil;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import java.awt.im.InputContext;

//java远程连接linux服务器执行shell代码
public class SshxcuteStudy {

    public static void main(String[] args) throws TaskExecFailException {
        ConnBean connBean = new ConnBean("node03","root","111111");

        SSHExec instance = SSHExec.getInstance(connBean);
        //连接我们的linux服务器
        instance.connect();
        ExecCommand execCommand = new ExecCommand("echo 'helloworld' >>/export/servers/abchello.txt");

        Result exec = instance.exec(execCommand);

        instance.disconnect();
    }
}
