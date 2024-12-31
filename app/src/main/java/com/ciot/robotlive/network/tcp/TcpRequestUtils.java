package com.ciot.robotlive.network.tcp;

import com.ciot.robotlive.bean.ProtocolBean;
import com.ciot.robotlive.constant.NetConstant;
import com.ciot.robotlive.utils.ByteUtils;
import com.ciot.robotlive.utils.GsonUtils;
import com.ciot.robotlive.utils.MyLog;

public class TcpRequestUtils {
    /**
     * 协议实体转换为字节数组
     */
    public static byte[] bean2Bytes(ProtocolBean protocolBean) {
        int head = protocolBean.getHead();
        byte qa = protocolBean.getQa();
        int seq = protocolBean.getSeq();
        int type = protocolBean.getType();
        short cmd = protocolBean.getCmd();
        short ver = protocolBean.getVer();
        short cflag = protocolBean.getCflag();
        short rflag = protocolBean.getRflag();
        String body = GsonUtils.getGsonR(cmd, true).toJson(protocolBean.getBody(), GsonUtils.getType(cmd, qa));
        int length = body.getBytes().length;
        byte[] beanBytes = new byte[23 + length];
        byte[] bytes = ByteUtils.int2bytes(head);
        System.arraycopy(bytes, 0, beanBytes, 0, 4);
        beanBytes[4] = qa;

        bytes = ByteUtils.int2bytes(seq);
        System.arraycopy(bytes, 0, beanBytes, 5, 4);

        bytes = ByteUtils.short2bytes((short) type);
        System.arraycopy(bytes, 0, beanBytes, 9, 2);

        bytes = ByteUtils.short2bytes(cmd);
        System.arraycopy(bytes, 0, beanBytes, 11, 2);

        bytes = ByteUtils.short2bytes(ver);
        System.arraycopy(bytes, 0, beanBytes, 13, 2);

        bytes = ByteUtils.short2bytes(cflag);
        System.arraycopy(bytes, 0, beanBytes, 15, 2);

        bytes = ByteUtils.short2bytes(rflag);
        System.arraycopy(bytes, 0, beanBytes, 17, 2);

        bytes = ByteUtils.int2bytes(length);
        System.arraycopy(bytes, 0, beanBytes, 19, 4);

        bytes = body.getBytes();
        System.arraycopy(bytes, 0, beanBytes, 23, bytes.length);
        return beanBytes;
    }

    /**
     * 字节数组转换为协议实体
     */
    public static ProtocolBean bytes2Bean(byte[] beanBytes) {
        ProtocolBean protocolBean = new ProtocolBean();
        byte[] bytes = new byte[4];
        byte[] shortbytes = new byte[2];

        // 头部校验
        int head;
        int start = 0;
        while (true) {
            System.arraycopy(beanBytes, start, bytes, 0, 4);
            head = ByteUtils.bytes2int(bytes);
            if (head == 0xFFFEEEEF) {
                break;
            }
            start++;
        }
        if (head != protocolBean.getHead()) {
            return protocolBean;
        }

        System.arraycopy(beanBytes, 11 + start, shortbytes, 0, 2);
        short cmd = ByteUtils.bytes2short(shortbytes);
        protocolBean.setCmd(cmd);
        //MyLog.d("body_tag", "cmd:"+cmd);
        // 心跳包应答不解析
        if (cmd == NetConstant.CONTROL_STATUS_HEART_BEAT) {
            return protocolBean;
        }

        System.arraycopy(beanBytes, 19 + start, bytes, 0, 4);
        int length = ByteUtils.bytes2int(bytes);
        //MyLog.d("body_tag", "length: " + length);
        protocolBean.setLen(length);

        // 检查 length 的值是否合理
        final int MAX_BODY_LENGTH = 1024;
        if (length > MAX_BODY_LENGTH) {
            MyLog.e("body_tag", "body length out of memory!!!, length = " + length);
            return protocolBean;
        }
        byte[] bodyBytes = new byte[length];
        System.arraycopy(beanBytes, 23 + start, bodyBytes, 0, length);
        String body = new String(bodyBytes);
        //MyLog.d("body_tag", bytes2Bean: "+ body);

        System.arraycopy(beanBytes, 9 + start, shortbytes, 0, 2);
        short type = ByteUtils.bytes2short(shortbytes);
        protocolBean.setType(type);

        byte qa = beanBytes[4 + start];
        protocolBean.setQa(qa);
        protocolBean.setBody(GsonUtils.getGson().fromJson(body, GsonUtils.getType(cmd, qa)));

        System.arraycopy(beanBytes, 5 + start, bytes, 0, 4);
        int seq = ByteUtils.bytes2int(bytes);
        protocolBean.setSeq(seq);
        System.arraycopy(beanBytes, 13 + start, shortbytes, 0, 2);
        short ver = ByteUtils.bytes2short(shortbytes);
        protocolBean.setVer(ver);
        System.arraycopy(beanBytes, 15 + start, shortbytes, 0, 2);
        short cFlag = ByteUtils.bytes2short(shortbytes);
        protocolBean.setCflag(cFlag);

        System.arraycopy(beanBytes, 17 + start, shortbytes, 0, 2);
        short rFlag = ByteUtils.bytes2short(shortbytes);
        protocolBean.setRflag(rFlag);

        return protocolBean;
    }
}
