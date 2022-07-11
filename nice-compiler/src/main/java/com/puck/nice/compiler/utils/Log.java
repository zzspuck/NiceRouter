package com.puck.nice.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author: zzs
 * @date: 2022/7/11
 */

public class Log {

    private Messager messager;

    private Log(Messager messager) {
        this.messager = messager;
    }

    public static Log newLog(Messager messager) {
        return new Log(messager);
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
