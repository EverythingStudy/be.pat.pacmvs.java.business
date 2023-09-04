package cn.staitech.anno.netty.global;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatGroup {
    
    public static ConcurrentMap<String, ChannelGroup> chatGroupMap = new ConcurrentHashMap<>();
    
    
    public static final ConcurrentMap<Integer, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();
    
    
}
