package com.sso.redis.bean;

import java.io.Serializable;
import java.util.HashSet;

public class ToDoWorkunit implements Serializable{
    public HashSet<String> toAccept = new HashSet();
    public HashSet<String> toExec = new HashSet();
    public HashSet<String> toDistribute = new HashSet();
    public HashSet<String> toEvaluate = new HashSet();
    public HashSet<String> toSupervise = new HashSet();
}
