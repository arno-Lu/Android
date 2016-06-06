package com.lu.kuaichuan.wifidirect.provider;

import java.util.Calendar;

/**
 * every sended file has a unique record
 */
public class Record {

    public final static int DIRECTION_OUT = 1;
    public final static int DIRECTION_IN = 0;


    public static final int STATE_FINISHED = 0; //���
    public static final int STATE_FAILED = 1; //ʧ��
    public static final int STATE_WAIT_FOR_TRANSPORT = 2; //�ȴ�
    public static final int STATE_TRANSPORTING = 3; //����
    public static final int STATE_PAUSED = 4; //��ͣ

    private long id;
    private String name;
    private String path;
    private long length;
    private long transported_len;
    private int state;
    private int transport_direction;
    private String mac;

    private double speed; //������ٶ�

    private OnStateChangeListener listener;

    public Record(String name, String path, long length, long transported_len, int state, int transport_direction, String mac) {
        this.path = path;
        this.length = length;
        this.transported_len = transported_len;
        this.state = state;
        this.transport_direction = transport_direction;
        this.mac = mac;
        //ʹ�õ�ǰʱ������Ϊid
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_YEAR);
        long ms = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        buffer.append(day).append(ms);
        this.id = Long.valueOf(buffer.toString()) + length;
        this.name = name;
        if(path == null) {
            this.path = "";
        }
        if(name == null) {
            this.name = "";
        }
        if(mac == null) {
            this.mac = "";
        }
    }

    public Record(long id, String name, String path, long length, long transported_len, int state, int transport_direction, String mac) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.length = length;
        this.transported_len = transported_len;
        this.state = state;
        this.transport_direction = transport_direction;
        this.mac = mac;
        if(path == null) {
            this.path = "";
        }
        if(name == null) {
            this.name = "";
        }
        if(mac == null) {
            this.mac = "";
        }
    }

    public boolean isSend() {
        return transport_direction == DIRECTION_OUT;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTransported_len() {
        return transported_len;
    }

    public void setTransported_len(long transported_len) {
        this.transported_len = transported_len;
        if(listener != null) {
            listener.onDataChanged(this);
        }
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        int old = state;
        this.state = state;
        if(this.state == STATE_FINISHED) {
            transported_len = length;
            speed = 0;
        }
        if(listener != null) {
            listener.onStateChanged(this, old, state);
        }
    }

    public int getTransport_direction() {
        return transport_direction;
    }

    public void setTransport_direction(int transport_direction) {
        this.transport_direction = transport_direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OnStateChangeListener getListener() {
        return listener;
    }

    public void setListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnStateChangeListener {
        public abstract void onStateChanged(Record record, int state_olad, int state_new);
        public abstract void onDataChanged(Record record);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeedAndTranportLen(double speed, long tranportLen) {
        this.speed = speed;
        this.transported_len = tranportLen;
        if(listener != null) {
            listener.onDataChanged(this);
        }
    }
}
